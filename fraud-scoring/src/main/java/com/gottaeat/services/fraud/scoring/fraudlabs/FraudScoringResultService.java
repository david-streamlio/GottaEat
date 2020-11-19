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
package com.gottaeat.services.fraud.scoring.fraudlabs;

import java.util.Hashtable;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fraudlabspro.FraudLabsPro;
import com.fraudlabspro.Order;
import com.gottaeat.domain.fraud.fraudlabs.FraudScoringRequest;
import com.gottaeat.domain.fraud.fraudlabs.FraudScoringResult;

public class FraudScoringResultService implements Function<FraudScoringRequest, FraudScoringResult> {

	private JSONParser parser = new JSONParser();
	private String apiKey;
	
	@Override
	public FraudScoringResult process(FraudScoringRequest request, Context ctx) throws Exception {
		
		if (!isInitalized()) {
			apiKey = (String) ctx.getUserConfigValue("apiKey").orElse(null);
		}
		
		FraudLabsPro.APIKEY = apiKey;

        // Get Order Result API
        Order orderResults = new Order();

        // Sets order ID to return all available information regarding the order
        Hashtable<String, String> data = new Hashtable<>();
        data.put("id", request.getTransactionId().toString());
        data.put("id_type", orderResults.FLP_ID);

        String result = orderResults.getTransaction(data); 
        JSONObject response = (JSONObject)parser.parse(result);
        
		return FraudScoringResult.newBuilder()
				.setOrder(request.getOrder())
				.setScore(Integer.parseInt(response.get("fraudlabspro_score").toString()))
				.build();
	}
	
	private boolean isInitalized() {
		return StringUtils.isNotBlank(apiKey);
	}

}
