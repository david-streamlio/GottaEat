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
package com.gottaeat.services.payment.paypal;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.TypedMessageBuilder;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Record;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gottaeat.domain.payment.AuthorizedPayment;
import com.gottaeat.domain.payment.Payment;

public class PaypalAuthorizationServiceTest {
	
	private String RANDOM = "com.gottaeat.services.payment.paypal.authorization.RandomAuthorizationStrategy";
	
	@Mock
	private Context mockContext;
	
	@SuppressWarnings("rawtypes")
	@Mock
	private Record mockRecord;
	
	@Mock
	private TypedMessageBuilder<AuthorizedPayment> mockedBuilder;

	private PaypalAuthorizationService service;

	private Map<String, String> map = new HashMap<String, String> ();

	
	@Before
	public final void init() throws PulsarClientException {
		
		MockitoAnnotations.initMocks(this);
		
		when(mockContext.getUserConfigValueOrDefault(
				"authorizer-classname", RANDOM))
		  .thenReturn(RANDOM);
		
		when(mockContext.getUserConfigValueOrDefault(
			"simulation.approval.percentage", "90"))
		  .thenReturn("100");
		
		when(mockContext.newOutputMessage(RANDOM, AvroSchema.of(AuthorizedPayment.class)))
			.thenReturn(mockedBuilder);
		
		when(mockContext.getCurrentRecord())
			.thenReturn(mockRecord);
		
		when(mockRecord.getProperties()).thenReturn(map);
		
		service = new PaypalAuthorizationService();
	}
	
	@Ignore
	@Test
	public final void concurrentTest() throws Exception {
		Payment pay = new Payment();
		long start = System.currentTimeMillis();
		
		for (int idx = 0; idx < 5; idx++) {
			service.process(pay, mockContext);
		}
		long end = System.currentTimeMillis();
		
		System.out.println("Five calls took " + (end-start) + " milliseconds");
	}
}
