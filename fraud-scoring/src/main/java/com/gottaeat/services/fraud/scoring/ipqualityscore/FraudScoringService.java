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
package com.gottaeat.services.fraud.scoring.ipqualityscore;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;

import com.google.common.hash.Hashing;
import com.gottaeat.domain.fraud.FraudScoringResult;
import com.gottaeat.domain.fraud.OrderScoringData;
import com.gottaeat.domain.payment.CreditCard;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FraudScoringService implements Function<OrderScoringData, FraudScoringResult> {
	
	private static final String BASE_URL = "https://ipqualityscore.com";
	private String apiKey;
	private String url;
	
	@Override
	public FraudScoringResult process(OrderScoringData input, Context ctx) throws Exception {
		if (!isInitalized()) {
			apiKey = (String) ctx.getUserConfigValue("apiKey").orElse(null);
		}

		HttpUrl.Builder httpBuilder = HttpUrl.parse(getUrl()).newBuilder();
		httpBuilder.addQueryParameter("billing_first_name", input.getCustomer().getFirstName().toString());
		httpBuilder.addQueryParameter("billing_last_name", input.getCustomer().getLastName().toString());
		httpBuilder.addQueryParameter("billing_country", input.getCustomer().getBillingAddress().getCountry().toString());
		httpBuilder.addQueryParameter("billing_address_1", input.getCustomer().getBillingAddress().getStreet().toString());
		httpBuilder.addQueryParameter("billing_city", input.getCustomer().getBillingAddress().getCity().toString());
		httpBuilder.addQueryParameter("billing_region", input.getCustomer().getBillingAddress().getState().toString());
		httpBuilder.addQueryParameter("billing_postcode", input.getCustomer().getBillingAddress().getZip().toString());
		httpBuilder.addQueryParameter("billing_email", input.getCustomer().getEmail().toString());
		httpBuilder.addQueryParameter("billing_phone", input.getCustomer().getPhoneNumber().toString());
		
		httpBuilder.addQueryParameter("shipping_first_name", input.getCustomer().getFirstName().toString());
		httpBuilder.addQueryParameter("shipping_last_name", input.getCustomer().getLastName().toString());
		httpBuilder.addQueryParameter("shipping_country", input.getOrder().getDeliveryLocation().getCountry().toString());
		httpBuilder.addQueryParameter("shipping_address_1", input.getOrder().getDeliveryLocation().getStreet().toString());
		httpBuilder.addQueryParameter("shipping_city", input.getOrder().getDeliveryLocation().getCity().toString());
		httpBuilder.addQueryParameter("shipping_region", input.getOrder().getDeliveryLocation().getState().toString());
		httpBuilder.addQueryParameter("shipping_postcode", input.getOrder().getDeliveryLocation().getZip().toString());
		httpBuilder.addQueryParameter("shipping_email", input.getCustomer().getEmail().toString());
		httpBuilder.addQueryParameter("shipping_phone", input.getCustomer().getPhoneNumber().toString());
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
		
		return FraudScoringResult.newBuilder()
       		 .setOrder(input)
       		 .setFraudScoreJSON(response.body().string())
             .build();
	}
	
	private boolean isInitalized() {
       return StringUtils.isNotBlank(apiKey);
	}
	
	private String getUrl() throws UnknownHostException {
       if (url == null) {
    	 url = HttpUrl.parse(BASE_URL + "/api/json/ip/" + apiKey + "/" + 
            getIp6Address().getHostAddress()).newBuilder().build().toString(); 
       }
       return url;
	}
	
	private static Inet6Address getIp6Address() throws UnknownHostException {
		InetAddress[] addresses = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
		
		List<Inet6Address> addrs = new ArrayList<Inet6Address> ();
	    for (InetAddress addr : addresses) {
	        if (addr instanceof Inet6Address) {
	        	Inet6Address ip6 = (Inet6Address)addr;
	        	if ( !ip6.isLinkLocalAddress() && !ip6.isLoopbackAddress() && 
	        		!ip6.isSiteLocalAddress()) {
	              addrs.add((Inet6Address) addr);
	        	}
	        }
	    }
	    return addrs.get(0);
	}
}