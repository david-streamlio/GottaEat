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
package com.gottaeat.services.location.tracking;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
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
import com.gottaeat.domain.user.User;
import com.gottaeat.domain.user.UserType;

public class LocationTrackingServiceLocalRunnerTest {

	private final static String BROKER_URL = "pulsar://localhost:6650";
	private final static String IN = "persistent://public/default/devices-in"; 
	private final static String OUT = "persistent://public/default/valid-devices";
	
	private static ExecutorService executor;
	private static LocalRunner localRunner;
	private static PulsarClient client;
	private static Producer<ActiveUser> producer;
	private static Consumer<ActiveUser> consumer;
	
	private static IgniteClient igniteClient;
	private static ClientCache<Long, LatLon> cache;
	
	public static void main(String[] args) throws Exception {
		startLocalRunner();
		init();
		startConsumer();
		sendData(250);
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
		
		producer = client.newProducer(Schema.AVRO(ActiveUser.class)).topic(IN).create();
		consumer = client.newConsumer(Schema.AVRO(ActiveUser.class)).topic(OUT).subscriptionName("location-sub").subscribe();
		
		igniteClient = Ignition.startClient(new ClientConfiguration().setAddresses("localhost:10800"));
		cache = igniteClient.getOrCreateCache("com.gottaeat.data.location");
	}
	
	private static FunctionConfig getFunctionConfig() {
		Map<String, ConsumerConfig> inputSpecs = new HashMap<String, ConsumerConfig> ();
		inputSpecs.put(IN, ConsumerConfig.builder()
				             .schemaType(Schema.AVRO(ActiveUser.class).getSchemaInfo().getType().toString())
				             .build());
		
		Map<String, Object> userConfig = new HashMap<String, Object>();
		userConfig.put(LocationTrackingService.CACHENAME_KEY, "com.gottaeat.data.location");
		userConfig.put(LocationTrackingService.DATAGRID_KEY, "localhost:10800");
				            		 
		return FunctionConfig.builder()
				.className(LocationTrackingService.class.getName())
				.cleanupSubscription(true)
				.inputs(Collections.singleton(IN))
				.inputSpecs(inputSpecs)
				.output(OUT)
				.outputSchemaType(Schema.AVRO(ActiveUser.class).getSchemaInfo().getType().toString())
				.name("location-tracking-service")
				.tenant("public")
				.namespace("default")
				.runtime(FunctionConfig.Runtime.JAVA)
				.subName("location-tracking-service-sub")
				.userConfig(userConfig)
				.build();
	}
	
	private static void startConsumer() {
		Runnable runnableTask = () -> {
			while (true) {
			  Message<ActiveUser> msg = null;
			  try {
			    msg = consumer.receive();
			    LatLon location = cache.get(msg.getValue().getUser().getRegisteredUserId());
			    System.out.printf("Cached location for user_id %d is [Lat: %f, Lon: %f] \n", 
			    		msg.getValue().getUser().getRegisteredUserId(), location.getLatitude(), location.getLongitude());
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
					.setUser(User.newBuilder()
							.setRegisteredUserId(rnd.nextInt(10))
							.setUserRole(UserType.DRIVER)
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
