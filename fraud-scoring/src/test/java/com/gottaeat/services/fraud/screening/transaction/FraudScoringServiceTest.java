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
package com.gottaeat.services.fraud.screening.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.pulsar.functions.api.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gottaeat.domain.fraud.TransactionScreeningResult;
import com.gottaeat.services.fraud.scoring.ipqualityscore.FraudScore;
import com.gottaeat.services.fraud.screening.transaction.FraudScoringService;

public class FraudScoringServiceTest {

	private ObjectMapper objectMapper = new ObjectMapper();
	private FraudScoringService service;
	
	@Mock
	private Context mockedContext;
	
	@Before
	public final void init() {
		MockitoAnnotations.initMocks(this);
		service = spy(new FraudScoringService());
		when(mockedContext.getUserConfigValue("apiKey"))
		  .thenReturn(Optional.of(System.getProperty("API-KEY")));
	}
	
	@Test
	public final void fraudTest() throws Exception {
		TransactionScreeningResult result = service.process(MockOrderProvider.getOrder(), mockedContext);
		FraudScore score = objectMapper.readValue(result.getFraudScoreJSON().toString(), FraudScore.class);
		
		assertNotNull(result);
		assertEquals(100, score.getTransaction_details().getRisk_score());
	}

}
