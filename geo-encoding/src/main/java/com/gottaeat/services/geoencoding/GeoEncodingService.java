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
package com.gottaeat.services.geoencoding;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.functions.api.Record;
import org.slf4j.Logger;

import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.geography.GeoEncodedAddress;

/**
 * In a this pattern we have a single point of entry that exposes a well defined API, 
 * and routes requests to downstream services based on endpoints, routes, methods, 
 * and client features.
 * 
 * Having an abstraction layer between clients and downstream services facilitates 
 * incremental updates, rolling releases, and parallel versioning. Downstream services 
 * can be owned by different teams, decoupling release cycles, reducing the need for 
 * cross-team coordination, and improving API lifecycle and evolvability.
 * 
 * This pattern is known as the Robust API, API Gateway or Gateway Router
 * 
 * @see https://www.enterpriseintegrationpatterns.com/patterns/messaging/BroadcastAggregate.html
 * @see https://medium.com/@eduardoromero/serverless-architectural-patterns-261d8743020#96a6
 *
 */
public class GeoEncodingService implements Function<Address, Void> {

	private Logger logger;
	private boolean initalized = false;
	private String controlTopic;
	private String gatherTopic;
	private String[] circuitBreakerTopics;
	
	private Map<String, CircuitStatus> statusMap;
	
	@Override
	public Void process(Address addr, Context context) throws Exception {
		
		if (!initalized) {
			init(context);
		}
		
		logger.info("Encoding delivery address for order # " + 
			context.getCurrentRecord().getProperties().get("order-id"));
		
		if (context.getCurrentRecord().getTopicName().get().equals(controlTopic)) {
			handleControlMsg(context.getCurrentRecord());
		} else {
			context.incrCounter("msg-counter", 1);
			boolean sendToHalfOpen = context.getCounter("msg-counter") % 2 == 0;
			String correlationId = UUID.randomUUID().toString();
			int num = calculateNumRequestsSent(sendToHalfOpen);
			
			if (num == 0) { // All the providers are offline.
				context.getCurrentRecord().fail();
			}
			
			// These all go to the CircuitBreakers and then on to the Web services
			statusMap.forEach( (topic, status) -> {
				if ((status == CircuitStatus.OPEN) || 
					(status == CircuitStatus.HALF_OPEN && sendToHalfOpen)) {
					try {
						context.newOutputMessage(topic, AvroSchema.of(Address.class))
							.property("correlation-ID", correlationId)
							.property("requests-sent", num + "")
							.properties(context.getCurrentRecord().getProperties())
							.value(addr)
							.sendAsync();
					} catch (PulsarClientException e) {
						e.printStackTrace();
					}
				}
			});
			
			// Send a 'times up" message in case all requests timeout, etc.
			context.newOutputMessage(gatherTopic, AvroSchema.of(GeoEncodedAddress.class))
				.value(GeoEncodedAddress.newBuilder().setAddress(addr).build())
				.property("correlation-ID", correlationId)
				.deliverAfter(1, TimeUnit.MINUTES);
		}
		
		return null;
	}
	
	private void init(Context context) {
		logger = context.getLogger();
		statusMap = new HashMap<String, CircuitStatus> ();
		controlTopic = (String) context.getUserConfigValue("control-topic").get();
		gatherTopic = (String) context.getUserConfigValue("aggregator-topic").get();
		circuitBreakerTopics = ((String)context.getUserConfigValue("circuit-breaker-topics")
		   .get()).split(",");
		
		for (String cb : circuitBreakerTopics) {
		  statusMap.put(cb, CircuitStatus.OPEN);
		}
		
		
		logger.info("circuitBreakerTopics = " + circuitBreakerTopics);
		logger.info("controlTopic = " + controlTopic);
		logger.info("gatherTopic = " + gatherTopic);
		initalized = true;
	}
	
	private int calculateNumRequestsSent(boolean half) {
		
		Predicate<CircuitStatus> openPredicate = item -> item == CircuitStatus.OPEN;
		Predicate<CircuitStatus> halfOpenPredicate = item -> item == CircuitStatus.HALF_OPEN;
		
		return statusMap.values().stream().filter(openPredicate).collect(Collectors.toList()).size() + 
				(half ? statusMap.values().stream().filter(halfOpenPredicate).collect(Collectors.toList()).size(): 0);
	}
	
	// CircuitBreakers register with us, and notify us of any status changes
	private void handleControlMsg(Record<?> currentRecord) {
		String destination = currentRecord.getProperties().get("circuit-breaker-topic");
		CircuitStatus status = CircuitStatus.valueOf(currentRecord.getProperties().get("circuit-breaker-status"));
		statusMap.put(destination, status);
	}

}
