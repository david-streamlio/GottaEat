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
package com.gottaeat.services.device.validation;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
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

import com.gottaeat.domain.geography.LatLon;
import com.gottaeat.domain.user.ActiveUser;
import com.gottaeat.domain.user.DeviceInfo;
import com.gottaeat.domain.user.User;
import com.gottaeat.domain.user.UserType;
import com.gottaeat.services.device.validation.DeviceValidationService;

public class DeviceValidationServiceLocalRunnerTest {

	private final static String BROKER_URL = "pulsar://localhost:6650";
	private final static String STATE_STORE_URL = "bk://localhost:4181";
	private final static String IN = "persistent://public/default/devices-in"; 
	private final static String OUT = "persistent://public/default/valid-devices";
	
	private static ExecutorService executor;
	private static LocalRunner localRunner;
	private static PulsarClient client;
	private static Producer<ActiveUser> producer;
	private static Consumer<ActiveUser> consumer;
	
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
				.stateStorageServiceUrl(STATE_STORE_URL)
				.functionConfig(getFunctionConfig())
				.build();
		localRunner.start(false);
	}

	private static void init() throws PulsarClientException {
		executor = Executors.newFixedThreadPool(2);
		client = PulsarClient.builder()
			    .serviceUrl(BROKER_URL)
			    .build();
		
		producer = client.newProducer(Schema.AVRO(ActiveUser.class)).topic(IN).create();
		consumer = client.newConsumer(Schema.AVRO(ActiveUser.class)).topic(OUT).subscriptionName("validation-sub").subscribe();
	}
	
	private static FunctionConfig getFunctionConfig() {
		Map<String, ConsumerConfig> inputSpecs = new HashMap<String, ConsumerConfig> ();
		inputSpecs.put(IN, ConsumerConfig.builder()
				             .schemaType(Schema.AVRO(ActiveUser.class).getSchemaInfo().getType().toString())
				             .build());

		Map<String, Object> userConfig = new HashMap<String, Object>();
		userConfig.put(DeviceValidationService.DB_DRIVER_KEY, "com.mysql.cj.jdbc.Driver");
		userConfig.put(DeviceValidationService.DB_URL_KEY, "jdbc:mysql://localhost:3306/GottaEat");
		userConfig.put(DeviceValidationService.REGISTRATION_TOPIC_KEY, "persistent://public/default/device-registration");
		
		Map<String, Object> secrets = new HashMap<String, Object>();
		secrets.put(DeviceValidationService.DB_USER_KEY, "orbit");
		secrets.put(DeviceValidationService.DB_PASS_KEY, "orbit");
		
		return FunctionConfig.builder()
				.className(DeviceValidationService.class.getName())
				.cleanupSubscription(true)
				.inputs(Collections.singleton(IN))
				.inputSpecs(inputSpecs)
				.output(OUT)
				.outputSchemaType(Schema.AVRO(ActiveUser.class).getSchemaInfo().getType().toString())
				.name("device-validation-service")
				.tenant("public")
				.namespace("default")
				.runtime(FunctionConfig.Runtime.JAVA)
				.secrets(secrets)
				.subName("device-validation-service-sub")
		        .userConfig(userConfig)
				.build();
	}
	
	private static void startConsumer() {
		Runnable runnableTask = () -> {
			while (true) {
			  Message<ActiveUser> msg = null;
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
		Random rnd = new Random();
		
		for (int idx = 0; idx < num; idx++) {
		   producer.send(ActiveUser.newBuilder()
				  .setDevice(DeviceInfo.newBuilder()
						  .setDeviceId(UUID.randomUUID().toString())
						  .setGlobalIPv4("98.99.100.101")
						  .setGlobalIPv6("")
						  .build())
				  .setUser(User.newBuilder()
						  .setRegisteredUserId(rnd.nextInt(50))
						  .setUserRole(UserType.CUSTOMER)
						  .build())
				  .setLocation(LatLon.newBuilder()
						  .setLatitude(rnd.nextDouble() * rnd.nextInt(180))
						  .setLongitude(rnd.nextDouble() * rnd.nextInt(180))
						  .build())	
				  .build());
		}
	}
	
	private static void shutdown() throws Exception {
		Thread.sleep(30000);
	    executor.shutdown();
	    localRunner.stop();
	    System.exit(0);
	}
}
