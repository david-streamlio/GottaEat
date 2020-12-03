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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.pulsar.client.api.TypedMessageBuilder;
import org.apache.pulsar.functions.api.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.fraud.TransactionScreeningResult;
import com.gottaeat.services.fraud.scoring.ipqualityscore.FraudScore;
import com.gottaeat.services.fraud.scoring.ipqualityscore.TransactionDetails;

public class FraudDetectionServiceTest {

	private ObjectMapper objectMapper = new ObjectMapper();
	private FraudDetectionService service;
	private TransactionScreeningResult input;
	
	@Mock
	private Context mockContext;
	
	@Mock
	private TypedMessageBuilder<Object> mockedMessageBuilder;
	
	@Before
	public final void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(mockContext.getUserConfigValue(FraudDetectionService.FRAUD_TOPIC_KEY))
		  .thenReturn(Optional.of("some-topic"));
		
		when(mockContext.getUserConfigValue(FraudDetectionService.RISK_THRESHOLD))
		  .thenReturn(Optional.of(new Integer(50)));
		
		when(mockContext.newOutputMessage(anyString(), any()))
		   .thenReturn(mockedMessageBuilder);
		
		when(mockedMessageBuilder.value(any())).thenReturn(mockedMessageBuilder);

		service = new FraudDetectionService();
	}
	
	@Test
	public final void validOrderTest() throws Exception {
		FraudScore score = new FraudScore();
		TransactionDetails details = new TransactionDetails();
		details.setRisk_score(30);
		
		score.setTransaction_details(details);
		
		input = TransactionScreeningResult
				    .newBuilder()
				    .setOrder(MockOrderProvider.getOrder())
				    .setFraudScoreJSON(objectMapper.writeValueAsString(score))
				    .build();

		FoodOrder result = service.process(input, mockContext);
		assertNotNull(result);
	}
	
	@Test
	public final void invalidOrderTest() throws Exception {
		FraudScore score = new FraudScore();
		TransactionDetails details = new TransactionDetails();
		details.setRisk_score(51);
		score.setTransaction_details(details);
		
		input = TransactionScreeningResult
			    .newBuilder()
			    .setOrder(MockOrderProvider.getOrder())
			    .setFraudScoreJSON(objectMapper.writeValueAsString(score))
			    .build();

		FoodOrder result = service.process(input, mockContext);
		assertNull(result);
		Mockito.verify(mockContext, times(1))
		  .newOutputMessage(anyString(), any());
	}
}
