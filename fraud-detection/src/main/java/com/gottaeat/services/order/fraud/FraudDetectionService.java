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

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gottaeat.domain.fraud.TransactionScreeningResult;
import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.services.fraud.scoring.ipqualityscore.FraudScore;

/**
 * Takes in Fraud Score object which represents the probability that an order is fraudulent,
 * and determines whether it crosses the threshold we set for "fraud"
 *
 */
public class FraudDetectionService implements Function<TransactionScreeningResult, FoodOrder> {

	public static final String FRAUD_TOPIC_KEY = "fraudulent-orders";
	public static final String RISK_THRESHOLD = "riskThreshold";
	private String fraudOrderTopic;
	private Integer riskThreshold = -1;
	
	@Override
	public FoodOrder process(TransactionScreeningResult input, Context ctx) throws Exception {

		if (!isInitalized()) {
		   fraudOrderTopic = (String) ctx.getUserConfigValue(FRAUD_TOPIC_KEY).get();
		   riskThreshold = (Integer) ctx.getUserConfigValue(RISK_THRESHOLD).get();
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		FraudScore result = objectMapper.readValue(input.getFraudScoreJSON().toString(), FraudScore.class);
		
		if (result.getTransaction_details().getRisk_score() <= riskThreshold) {
		   return input.getOrder().getOrder();
		} else {
		   ctx.newOutputMessage(fraudOrderTopic, Schema.AVRO(TransactionScreeningResult.class))
		     .value(input)
		     .sendAsync();
		   
		   return null;	
		}
	}

	private boolean isInitalized() {
      return StringUtils.isNotBlank(fraudOrderTopic) && riskThreshold != -1;
	}
}
