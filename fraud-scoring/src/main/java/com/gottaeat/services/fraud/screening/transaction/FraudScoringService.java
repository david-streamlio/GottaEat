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

import java.nio.charset.StandardCharsets;
import java.net.UnknownHostException;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;

import com.google.common.hash.Hashing;
import com.gottaeat.domain.fraud.OrderScoringData;
import com.gottaeat.domain.fraud.TransactionScreeningResult;
import com.gottaeat.domain.payment.CreditCard;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FraudScoringService implements Function<OrderScoringData, TransactionScreeningResult> {
	
	private static final String BASE_URL = "https://ipqualityscore.com/api/json/ip/";
	private String apiKey;
	private String url;
	
	@Override
	public TransactionScreeningResult process(OrderScoringData input, Context ctx) throws Exception {
		if (!isInitalized()) {
			apiKey = (String) ctx.getUserConfigValue("apiKey").orElse(null);
		}

		input.getCustomer().getUserDetails().getUserId();  // Use this to lookup IP address in Ignite Cache?
		HttpUrl.Builder httpBuilder = HttpUrl.parse(getUrl()).newBuilder();
		
		// TODO Add StringUtil.isNotBlank() test to these fields before adding? 
		httpBuilder.addQueryParameter("billing_first_name", input.getCustomer().getUserDetails().getFirstName().toString());
		httpBuilder.addQueryParameter("billing_last_name", input.getCustomer().getUserDetails().getLastName().toString());
		httpBuilder.addQueryParameter("billing_country", input.getCustomer().getBillingAddress().getCountry().toString());
		httpBuilder.addQueryParameter("billing_address_1", input.getCustomer().getBillingAddress().getStreet().toString());
		httpBuilder.addQueryParameter("billing_city", input.getCustomer().getBillingAddress().getCity().toString());
		httpBuilder.addQueryParameter("billing_region", input.getCustomer().getBillingAddress().getState().toString());
		httpBuilder.addQueryParameter("billing_postcode", input.getCustomer().getBillingAddress().getZip().toString());
		httpBuilder.addQueryParameter("billing_email", input.getCustomer().getUserDetails().getEmail().toString());
		httpBuilder.addQueryParameter("billing_phone", input.getCustomer().getUserDetails().getPhoneNumber().toString());
		
		httpBuilder.addQueryParameter("shipping_first_name", input.getCustomer().getUserDetails().getFirstName().toString());
		httpBuilder.addQueryParameter("shipping_last_name", input.getCustomer().getUserDetails().getLastName().toString());
		httpBuilder.addQueryParameter("shipping_country", input.getOrder().getDeliveryLocation().getCountry().toString());
		httpBuilder.addQueryParameter("shipping_address_1", input.getOrder().getDeliveryLocation().getStreet().toString());
		httpBuilder.addQueryParameter("shipping_city", input.getOrder().getDeliveryLocation().getCity().toString());
		httpBuilder.addQueryParameter("shipping_region", input.getOrder().getDeliveryLocation().getState().toString());
		httpBuilder.addQueryParameter("shipping_postcode", input.getOrder().getDeliveryLocation().getZip().toString());
		httpBuilder.addQueryParameter("shipping_email", input.getCustomer().getUserDetails().getEmail().toString());
		httpBuilder.addQueryParameter("shipping_phone", input.getCustomer().getUserDetails().getPhoneNumber().toString());
		httpBuilder.addQueryParameter("order_amount", Float.toString(input.getOrder().getPayment().getAmount().getTotal()));
		httpBuilder.addQueryParameter("order_quantity", input.getOrder().getFood().size() + "");
		httpBuilder.addQueryParameter("recurring", "false");
		httpBuilder.addQueryParameter("strictness", "1");
		
		if (input.getOrder().getPayment().getMethodOfPayment().getType() instanceof CreditCard) {
			CreditCard cc = (CreditCard) input.getOrder().getPayment().getMethodOfPayment().getType();
			httpBuilder.addQueryParameter("credit_card_bin", cc.getAccountNumber().toString().substring(0, 5));
			httpBuilder.addQueryParameter("credit_card_hash", Hashing.sha256()
					  .hashString(cc.getAccountNumber().toString(), StandardCharsets.UTF_8)
					  .toString());
			httpBuilder.addQueryParameter("credit_card_expiration_month", cc.getExpMonth().toString());
			httpBuilder.addQueryParameter("credit_card_expiration_year", cc.getExpYear().toString());
		}
		
		Request request = new Request.Builder().url(httpBuilder.build()).build();
		
		OkHttpClient client = new OkHttpClient().newBuilder().build();
        Call call = client.newCall(request);
        
        Response response = call.execute();
		
		return TransactionScreeningResult.newBuilder()
       		 .setOrder(input)
       		 .setFraudScoreJSON(response.body().string())
             .build();
	}
	
	protected boolean isInitalized() {
		return StringUtils.isNotBlank(apiKey);
	}

	// TODO Append user IP address to the url. Scoring is based on the IPv6 address
	protected String getUrl() throws UnknownHostException {
		if (url == null) {
			url = HttpUrl.parse(BASE_URL + apiKey + "/" ).newBuilder().build().toString(); 
		}
		return url;
	}
}