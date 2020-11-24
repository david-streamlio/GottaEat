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
package com.gottaeat.services.order.fraud;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gottaeat.domain.fraud.FraudScoringResult;
import com.gottaeat.domain.order.FoodOrder;

public class FraudDetectionServiceTest {

	private FraudDetectionService service;
	private FraudScoringResult input;
	
	@Mock
	private Context mockContext;
	
	@Before
	public final void init() {
		MockitoAnnotations.initMocks(this);
		when(mockContext.getUserConfigValue(FraudDetectionService.FRAUD_TOPIC_KEY))
		  .thenReturn(Optional.of("some-topic"));
		
		when(mockContext.getUserConfigValue(FraudDetectionService.RISK_THRESHOLD))
		  .thenReturn(Optional.of(new Integer(50)));

		service = spy(new FraudDetectionService());
	}
	
	@Test
	public final void validOrderTest() throws Exception {
		input = FraudScoringResult
				    .newBuilder()
				    .setOrder(MockOrderProvider.getOrder())
				    .setRiskScore(30)
				    .build();

		FoodOrder result = service.process(input, mockContext);
		assertNotNull(result);
	}
	
	@Test
	public final void invalidOrderTest() throws Exception {
		input = FraudScoringResult
			    .newBuilder()
			    .setOrder(MockOrderProvider.getOrder())
			    .setRiskScore(51)
			    .build();

		FoodOrder result = service.process(input, mockContext);
		assertNull(result);
		Mockito.verify(mockContext, times(1))
		  .newOutputMessage("some-topic", Schema.AVRO(FraudScoringResult.class));
	}
}
