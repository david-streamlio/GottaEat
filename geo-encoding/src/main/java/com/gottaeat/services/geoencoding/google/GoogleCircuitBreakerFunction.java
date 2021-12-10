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
package com.gottaeat.services.geoencoding.google;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;

import com.gottaeat.commons.state.LRUCacheStateStore;
import com.gottaeat.commons.state.StateStore;
import com.gottaeat.domain.geography.Address;
import com.gottaeat.services.geoencoding.CircuitStatus;

/**
 * The consumer should invoke a remote service via a proxy that behaves in a 
 * similar fashion to an electrical circuit breaker. When the number of consecutive 
 * failures crosses a threshold, the circuit breaker trips, and for the duration of 
 * a timeout period, all attempts to invoke the remote service will fail immediately. 
 * After the timeout expires the circuit breaker allows a limited number of test 
 * requests to pass through. If those requests succeed, the circuit breaker resumes 
 * normal operation. Otherwise, if there is a failure, the timeout period begins again. 
 * 
 * This pattern is suited to, prevent an application from trying to invoke a remote 
 * service or access a shared resource if this operation is highly likely to fail.
 *
 */
public class GoogleCircuitBreakerFunction implements Function<Address, Void> {

	static final String CONSECUTIVE_FAILURES = "consecutive-failures";
	static final String CONSECUTIVE_SUCCESSES = "consecutive-successes";
	
	static final BigInteger ZERO = new BigInteger("0");
	static final BigInteger ONE = new BigInteger("1");
	
	private Logger logger;
	private StateStore storage;
	CircuitStatus status = CircuitStatus.CLOSED;
	int threshold = Integer.MAX_VALUE;
	int resetTimer = 2; // How long to remain in closed state (in minutes)
	int resetThreshold = 10; // How many success messages require to transition from HALF_OPEN to OPEN
	boolean initalized = false;
	String sourceControlTopic; // To talk to the upstream Scatter function
	String failureNotificationTopic; // To receive failures from service
	
	@Override
	public Void process(Address addr, Context context) throws Exception {
		
		if (!initalized) {
			init(context);
		}
		
		logger.info("Encoding address for order # " + 
		  context.getCurrentRecord().getProperties().get("order-id"));
		
		if (context.getCurrentRecord().getTopicName().get().equals(failureNotificationTopic)) {
			handleFailureMsg(context);
		} else {
			if (status == CircuitStatus.HALF_OPEN) {
				handleHalfOpenMsg(context);
			}
			
			logger.info("Sending address to " + context.getOutputTopic());
			// Forward Address to Geo-Encoding service
			context.newOutputMessage(context.getOutputTopic(), AvroSchema.of(Address.class))
			  .properties(context.getCurrentRecord().getProperties()) 
			  .value(addr)
			  .send();
		}
		
		return null;
	}

	private void handleFailureMsg(Context context) throws PulsarClientException {
		BigInteger count = new BigInteger(storage.getState(CONSECUTIVE_FAILURES).array()).add(ONE);
		storage.putState(CONSECUTIVE_FAILURES, ByteBuffer.wrap(count.toByteArray()));
		storage.putState(CONSECUTIVE_SUCCESSES, ByteBuffer.wrap(ZERO.toByteArray()));
		
		if (count.intValue() >= threshold) {
			closeCircuit(context);
		}
	}

	private void handleHalfOpenMsg(Context context) throws PulsarClientException {
		BigInteger count = new BigInteger(storage.getState(CONSECUTIVE_SUCCESSES).array()).add(ONE);
		storage.putState(CONSECUTIVE_SUCCESSES, ByteBuffer.wrap(count.toByteArray()));
		if (count.intValue() >= resetThreshold) {
			openCircuit(context);
		}
	}
	
	private void init(Context context) throws PulsarClientException {
		logger = context.getLogger();
		storage = new LRUCacheStateStore();
		resetTimer = Integer.parseInt((String) context.getUserConfigValue("reset-timer").get());
		threshold = Integer.parseInt((String) context.getUserConfigValue("failure-threshold").get());
		sourceControlTopic = (String) context.getUserConfigValue("source-control-topic").get();
		failureNotificationTopic = (String) context.getUserConfigValue("failure-notification-topic").get();
		
		storage.putState(CONSECUTIVE_FAILURES, ByteBuffer.wrap(ZERO.toByteArray()));
		storage.putState(CONSECUTIVE_SUCCESSES, ByteBuffer.wrap(ZERO.toByteArray()));
		openCircuit(context);
		initalized = true;
	}
	
	private void openCircuit(Context context) throws PulsarClientException {
		context.newOutputMessage(sourceControlTopic, AvroSchema.of(Address.class))
		.property("circuit-breaker-topic", context.getInputTopics().iterator().next())
		.property("circuit-breaker-status", CircuitStatus.OPEN.toString())
		.send();
		status = CircuitStatus.OPEN;
	}

	private void closeCircuit(Context context) throws PulsarClientException {
		context.newOutputMessage(sourceControlTopic, AvroSchema.of(Address.class))
		.property("circuit-breaker-topic", context.getInputTopics().iterator().next())
		.property("circuit-breaker-status", CircuitStatus.CLOSED.toString())
		.send();
		
		status = CircuitStatus.CLOSED;
		
		// Send a delayed message for HALF_OPEN
		context.newOutputMessage(sourceControlTopic, AvroSchema.of(Address.class))
		.property("circuit-breaker-topic", context.getInputTopics().iterator().next())
		.property("circuit-breaker-status", CircuitStatus.HALF_OPEN.toString())
		.deliverAfter(resetTimer, TimeUnit.MINUTES);
		
		context.putState(CONSECUTIVE_FAILURES, ByteBuffer.wrap(ZERO.toByteArray()));
	}
}
