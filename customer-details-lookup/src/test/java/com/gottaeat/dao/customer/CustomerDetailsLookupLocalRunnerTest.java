package com.gottaeat.dao.customer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.common.functions.ConsumerConfig;
import org.apache.pulsar.common.functions.FunctionConfig;
import org.apache.pulsar.functions.LocalRunner;

import com.gottaeat.domain.fraud.fraudlabs.Transaction;
import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.order.FoodOrderMeta;
import com.gottaeat.domain.order.OrderStatus;

public class CustomerDetailsLookupLocalRunnerTest {
	final static String BROKER_URL = "pulsar://localhost:6650";
	final static String IN = "persistent://public/default/customers-in"; 
	final static String OUT = "persistent://public/default/customer-details";
	
	private static ExecutorService executor;
	private static LocalRunner localRunner;
	private static PulsarClient client;
	private static Producer<FoodOrder> producer;
	private static Consumer<Transaction> consumer;
	
	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
//			keyword = args[0];
		}
		startLocalRunner();
		init();
		startConsumer();
	    sendData(50);
	    shutdown();
	}
	
	private static void startLocalRunner() throws Exception {
		localRunner = LocalRunner.builder()
				.brokerServiceUrl(BROKER_URL)
				.functionConfig(getFunctionConfig())
				.build();
		localRunner.start(false);
	}

	private static void init() throws PulsarClientException {
		executor = Executors.newFixedThreadPool(2);
		client = PulsarClient.builder()
			    .serviceUrl(BROKER_URL)
			    .build();

		producer = client.newProducer(Schema.AVRO(FoodOrder.class)).topic(IN).create();	
		consumer = client.newConsumer(Schema.AVRO(Transaction.class)).topic(OUT).subscriptionName("validation-sub").subscribe();
	}
	
	private static FunctionConfig getFunctionConfig() {
		Map<String, ConsumerConfig> inputSpecs = new HashMap<String, ConsumerConfig> ();
		inputSpecs.put(IN, ConsumerConfig.builder()
				.schemaType(Schema.STRING.getSchemaInfo().getName())
				.build());

		Map<String, Object> userConfig = new HashMap<String, Object>();
		userConfig.put(CustomerDetailsLookup.DB_URL_KEY, "");
		userConfig.put(CustomerDetailsLookup.DB_USER_KEY, "");
		
		return FunctionConfig.builder()
				.className(CustomerDetailsLookup.class.getName())
				.inputs(Collections.singleton(IN))
				.inputSpecs(inputSpecs)
				.output(OUT)
				.name("customer-details-lookup")
				.tenant("public")
				.namespace("default")
				.runtime(FunctionConfig.Runtime.JAVA)
				.subName("customer-details-lookup-sub")
		        .userConfig(userConfig)
				.build();
	}
	
	private static void startConsumer() {
		Runnable runnableTask = () -> {
			while (true) {
			  Message<Transaction> msg = null;
			  try {
			    msg = consumer.receive();
			    System.out.printf("Message received: %s \n", msg.getValue());
			    consumer.acknowledge(msg);
			  } catch (Exception e) {
			    consumer.negativeAcknowledge(msg);
			  }
			}
		};
		executor.execute(runnableTask);
	}
	
	private static void sendData(int num) throws IOException {
		for (int idx = 0; idx < num; idx++) {
			producer.send(FoodOrder.newBuilder()
					.setMeta(FoodOrderMeta.newBuilder()
							.setCustomerId(10)
							.setOrderId(idx)
							.setOrderStatus(OrderStatus.NEW)
							.build())
					.setDeliveryLocation(Address.newBuilder()
							.setCity("Dallas")
							.setState("TX")
							.setStreet("123 Main St")
							.setZip("75043")
							.build())
					.build());
		}
	}
	
	private static void shutdown() throws Exception {
		Thread.sleep(30000);
	    executor.shutdown();
	    localRunner.stop();
	    producer.close();
	    consumer.close();
	    System.exit(0);
	}
	
}
