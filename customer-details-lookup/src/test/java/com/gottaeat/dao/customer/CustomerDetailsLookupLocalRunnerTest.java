/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.gottaeat.dao.customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

import com.gottaeat.domain.customer.CustomerDetails;
import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.order.FoodOrderMeta;
import com.gottaeat.domain.order.OrderStatus;
import com.gottaeat.domain.payment.CardType;
import com.gottaeat.domain.payment.CreditCard;
import com.gottaeat.domain.payment.Payment;
import com.gottaeat.domain.payment.PaymentAmount;
import com.gottaeat.domain.payment.PaymentMethod;
import com.gottaeat.domain.restaurant.FoodOrderDetail;
import com.gottaeat.domain.restaurant.MenuItem;

public class CustomerDetailsLookupLocalRunnerTest {
	final static String BROKER_URL = "pulsar://localhost:6650";
	final static String IN = "persistent://public/default/customers-in"; 
	final static String OUT = "persistent://public/default/customer-details";
	
	private static ExecutorService executor;
	private static LocalRunner localRunner;
	private static PulsarClient client;
	private static Producer<FoodOrder> producer;
	private static Consumer<CustomerDetails> consumer;
	
	public static void main(String[] args) throws Exception {
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
		consumer = client.newConsumer(Schema.AVRO(CustomerDetails.class)).topic(OUT).subscriptionName("validation-sub").subscribe();
	}
	
	private static FunctionConfig getFunctionConfig() {
		Map<String, ConsumerConfig> inputSpecs = new HashMap<String, ConsumerConfig> ();
		inputSpecs.put(IN, ConsumerConfig.builder()
				             .schemaType(Schema.AVRO(FoodOrder.class).getSchemaInfo().getType().toString())
				             .build());

		Map<String, Object> userConfig = new HashMap<String, Object>();
		userConfig.put(CustomerDetailsLookup.DB_DRIVER_KEY, "com.mysql.cj.jdbc.Driver");
		userConfig.put(CustomerDetailsLookup.DB_URL_KEY, "jdbc:mysql://localhost:3306/GottaEat");
		
		Map<String, Object> secrets = new HashMap<String, Object>();
		secrets.put(CustomerDetailsLookup.DB_USER_KEY, "orbit");
		secrets.put(CustomerDetailsLookup.DB_PASS_KEY, "orbit");
		
		return FunctionConfig.builder()
				.className(CustomerDetailsLookup.class.getName())
				.cleanupSubscription(true)
				.inputs(Collections.singleton(IN))
				.inputSpecs(inputSpecs)
				.output(OUT)
				.outputSchemaType(Schema.AVRO(CustomerDetails.class).getSchemaInfo().getType().toString())
				.name("customer-details-lookup")
				.tenant("public")
				.namespace("default")
				.runtime(FunctionConfig.Runtime.JAVA)
				.secrets(secrets)
				.subName("customer-details-lookup-sub")
		        .userConfig(userConfig)
				.build();
	}
	
	private static void startConsumer() {
		Runnable runnableTask = () -> {
			while (true) {
			  Message<CustomerDetails> msg = null;
			  try {
			    msg = consumer.receive();
			    System.out.printf("Message received: %s \n", msg);
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
							.setTimePlaced("2020-11-18 09:32:04.354")
							.build())
					.setDeliveryLocation(Address.newBuilder()
							.setCity("Dallas")
							.setState("TX")
							.setStreet("123 Main St")
							.setZip("75043")
							.setCountry("US")
							.build())
					.setFood(getFood())
					.setPayment(Payment.newBuilder()
							.setAmount(PaymentAmount.newBuilder()
									.setFoodTotal(8.99f)
									.setTax(0.0f)
									.setTotal(8.99f)
									.build())
							.setMethodOfPayment(PaymentMethod.newBuilder()
									.setType(CreditCard.newBuilder()
											.setAccountNumber("123456789012")
											.setBillingZip("75043")
											.setCardType(CardType.VISA)
											.setCcv("123")
											.setExpMonth("11")
											.setExpYear("2025")
											.build())
									.build())
							.build())
					.build());
		}
	}
	
	private static List<FoodOrderDetail> getFood() {
		List<FoodOrderDetail> food = new ArrayList<FoodOrderDetail>();
		List<CharSequence> cust = new ArrayList<CharSequence>();
		cust.add("pickles");
		
		food.add(FoodOrderDetail.newBuilder()
				.setFoodItem(MenuItem.newBuilder()
						.setCustomizations(cust )
						.setItemDescription("Chicken")
						.setItemId(13)
						.setItemName("Chicken")
						.setPrice(8.99f)
						.setTaxable(false)
						.build())
				.setQuantity(1)
				.build());
		return food;
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
