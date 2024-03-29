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
package com.gottaeat.services.payment.paypal.authorization;

import java.io.IOException;
import java.util.function.Consumer;

import org.apache.pulsar.functions.api.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gottaeat.domain.payment.AuthorizedPayment;
import com.gottaeat.domain.payment.CreditCard;
import com.gottaeat.domain.payment.PayPal;
import com.gottaeat.domain.payment.Payment;
import com.gottaeat.domain.payment.PaymentMethod;
import com.gottaeat.domain.payment.PaymentStatus;
import com.gottaeat.services.payment.paypal.UnauthorizedException;

import io.github.resilience4j.decorators.Decorators;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebServiceAuthorizationStrategy implements AuthorizationStrategy {

	private String apiEndpoint, tokenEndpoint, clientId, secret, accessToken;
	
	@Override
	public void initalize(Context ctx) {
		apiEndpoint = ctx.getUserConfigValueOrDefault("api-endpoint", 
		    "https://api.sandbox.paypal.com/v1/payments/payment").toString();
		
		tokenEndpoint = ctx.getUserConfigValueOrDefault("token-endpoint",
			"https://api.sandbox.paypal.com/v1/oauth2/token?grant_type=client_credentials").toString();
		
		clientId = ctx.getUserConfigValue("credentials.client").toString();
		secret = ctx.getUserConfigValue("credentials.secret").toString();
		
	}
	
	@Override
	public AuthorizedPayment authorize(Payment pay) {
		
		String approvalCode = getAuthorizationCode((PayPal)pay.getMethodOfPayment().getType());
		
		AuthorizedPayment auth = AuthorizedPayment.newBuilder()
				.setPayment(pay)
				.setStatus( (approvalCode != null) ? PaymentStatus.AUTHORIZED : PaymentStatus.REJECTED)
				.setApprovalCode(approvalCode)
			.build();
					
		return auth;
	}
	
	private String getAuthorizationCode(PayPal pay) {
		return Try.of(getAuthorizationFunction(pay))
				.onFailure(UnauthorizedException.class, refreshToken())
				.recover(UnauthorizedException.class, 
						(exc) -> Try.of(getAuthorizationFunction(pay)).get())
				.getOrNull();
	}
	
	/**
	 * @see https://developer.paypal.com/docs/integration/direct/payments/authorize-and-capture-payments/#authorize-the-payment
	 * @param pay
	 * @return
	 */
	private CheckedFunction0<String> getAuthorizationFunction(PayPal pay) {
		CheckedFunction0<String> fn = () -> {
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
			RequestBody body = RequestBody.create(buildRequestBody(pay), mediaType);
			
			Request request = new Request.Builder()
				.url(apiEndpoint)
				.addHeader("Authorization", accessToken)
				.post(body)
				.build();

			try (Response response = client.newCall(request).execute()) {
				if (response.isSuccessful()) {
					return response.body().string();
				} else {
					// determine error type
					if (response.code() == 500) {
						throw new UnauthorizedException();
					}
				}
				return null;
			}

		};
		
		return fn;
	}
	
	private Consumer<UnauthorizedException> refreshToken() {
		Consumer<UnauthorizedException> a = (ex) -> {
			// Refresh to access token
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
			RequestBody body = RequestBody.create("", mediaType);

			Request request = new Request.Builder()
				.url(tokenEndpoint)
				.addHeader("Accept-Language", "en_US")
				.addHeader("Authorization", Credentials.basic(clientId, secret))
				.post(body)
				.build();

			try (Response response = client.newCall(request).execute()) {
				if (response.isSuccessful()) {
				   parseToken(response.body().string());
				} 	
			} catch (IOException e) {
				e.printStackTrace();
			}	
		};
		return a ;
	}
	
	private void parseToken(String json) {
		JsonParser parser = new JsonParser();
		JsonElement jsonTree = parser.parse(json);
		JsonObject jsonObject = jsonTree.getAsJsonObject();
	    JsonElement token = jsonObject.get("access_token");
	    accessToken = token.getAsString();
	}
	
	private String buildRequestBody(PayPal pay) {
		StringBuilder sb = new StringBuilder();
			sb.append("{\"intent\": \"authorize\",");
			sb.append("\"payer\":\n" + 
					"  {\n" + 
					"    \"payment_method\": \"paypal\"\n" + 
					"  },");
			sb.append("\"transactions\": [{");
			sb.append("\"amount\": {");
			
			sb.append("}],}\n" + 
					"}");
		return sb.toString();
	}

}
