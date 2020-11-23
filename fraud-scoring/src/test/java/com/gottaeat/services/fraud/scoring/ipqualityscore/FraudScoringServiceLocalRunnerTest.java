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
package com.gottaeat.services.fraud.scoring.ipqualityscore;

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

import com.gottaeat.domain.fraud.FraudScoringResult;
import com.gottaeat.domain.fraud.OrderScoringData;

public class FraudScoringServiceLocalRunnerTest {

	final static String BROKER_URL = "pulsar://localhost:6650";
	final static String IN = "persistent://public/default/customer-details"; 
	final static String OUT = "persistent://public/default/fraud-score";
	
	private static ExecutorService executor;
	private static LocalRunner localRunner;
	private static PulsarClient client;
	private static Producer<OrderScoringData> producer;
	private static Consumer<FraudScoringResult> consumer;
	
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

		producer = client.newProducer(Schema.AVRO(OrderScoringData.class)).topic(IN).create();	
		consumer = client.newConsumer(Schema.AVRO(FraudScoringResult.class)).topic(OUT).subscriptionName("validation-sub").subscribe();
	}
	
	private static FunctionConfig getFunctionConfig() {
		Map<String, ConsumerConfig> inputSpecs = new HashMap<String, ConsumerConfig> ();
		inputSpecs.put(IN, ConsumerConfig.builder()
	             .schemaType(Schema.AVRO(OrderScoringData.class).getSchemaInfo().getType().toString())
	             .build());

		
		return FunctionConfig.builder()
				.className(FraudScoringService.class.getName())
				.cleanupSubscription(true)
				.inputs(Collections.singleton(IN))
				.inputSpecs(inputSpecs)
				.output(OUT)
				.outputSchemaType(Schema.AVRO(FraudScoringResult.class).getSchemaInfo().getType().toString())
				.name("fraud_scoring_function")
				.tenant("public")
				.namespace("default")
				.runtime(FunctionConfig.Runtime.JAVA)
				.subName("fraud-scoring-function-sub")
				.build();
	}
	
	private static void startConsumer() {
		Runnable runnableTask = () -> {
			while (true) {
			  Message<FraudScoringResult> msg = null;
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
			producer.send(MockOrderProvider.getOrder());
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
