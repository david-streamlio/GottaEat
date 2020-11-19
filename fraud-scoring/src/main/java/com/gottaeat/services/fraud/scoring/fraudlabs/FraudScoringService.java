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

import com.fraudlabspro.FraudLabsPro;
import com.fraudlabspro.Order;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.gottaeat.domain.fraud.fraudlabs.FraudScoringRequest;
import com.gottaeat.domain.fraud.fraudlabs.OrderScoringData;

/**
 * 
 * @see https://www.fraudlabspro.com/developer/api/screen-order
 *
 */
public class FraudScoringService implements Function<OrderScoringData, FraudScoringRequest> {
	
	private JSONParser parser = new JSONParser();
	private String apiKey;

	@Override
	public FraudScoringRequest process(OrderScoringData input, Context ctx) throws Exception {
		
		if (!isInitalized()) {
			apiKey = (String) ctx.getUserConfigValue("apiKey").orElse(null);
		}
		
		// Configures FraudLabs Pro API key
        FraudLabsPro.APIKEY = apiKey;

        // Screen Order API
        Order order = new Order();

        // Sets order details
        Hashtable<String, String> data = new Hashtable<>();
        
        data.put("ip", null); // Get this from IDMG ?
        data.put("format", "json");
        
        data.put("first_name", input.getCustomer().getFirstName().toString());
        data.put("last_name", input.getCustomer().getLastName().toString());
        data.put("email", input.getCustomer().getEmail().toString());
        data.put("user_phone", input.getCustomer().getPhoneNumber().toString());

        // Billing information
        data.put("bill_addr", input.getCustomer().getBillingAddress().getStreet().toString());
        data.put("bill_city", input.getCustomer().getBillingAddress().getCity().toString());
        data.put("bill_state", input.getCustomer().getBillingAddress().getState().toString());
        data.put("bill_country", input.getCustomer().getBillingAddress().getCountry().toString());
        data.put("bill_zip_code", input.getCustomer().getBillingAddress().getZip().toString());
        // data.put("number", ""); // Credit card number ?

        // Order information
        data.put("amount", Float.toString(input.getOrder().getPayment().getAmount().getTotal()));
        data.put("currency", "USD");
        data.put("payment_mode", order.CREDIT_CARD);  // Please refer reference section for full list of payment methods

        // Shipping information
        data.put("ship_addr", input.getOrder().getDeliveryLocation().getStreet().toString());
        data.put("ship_city", input.getOrder().getDeliveryLocation().getCity().toString());
        data.put("ship_state", input.getOrder().getDeliveryLocation().getState().toString());
        data.put("ship_zip_code", input.getOrder().getDeliveryLocation().getZip().toString());
        data.put("ship_country", input.getOrder().getDeliveryLocation().getCountry().toString());
        
         // Sends order details to FraudLabs Pro
        String result = order.validate(data);
        JSONObject response = (JSONObject)parser.parse(result);
        
        return FraudScoringRequest.newBuilder()
        		 .setOrder(input)
        		 .setTransactionId(response.get("fraudlabspro_score").toString())
                 .build();
	}

	private boolean isInitalized() {
		return StringUtils.isNotBlank(apiKey);
	}

}
