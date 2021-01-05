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
package com.gottaeat.security.authentication.sms.verification;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.user.ActiveUser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// TODO Define a schema that contains a userID, device id, and the response code to validate
public class VerificationCodeValidator extends VerificationCodeBase implements Function<SMSVerificationResponse, Void> {

	public static final String VALIDATED_TOPIC_KEY = "";
	private static final String BASE_URL = "https://" + RAPID_API_HOST + "/check-verification-code";
	private String validatedDeviceTopic;
	
	@Override
	public Void process(SMSVerificationResponse input, Context ctx) throws Exception {
		if (!isInitalized()) {
			apiKey = (String) ctx.getUserConfigValue(API_KEY).orElse(null);
			validatedDeviceTopic = ctx.getUserConfigValue(VALIDATED_TOPIC_KEY).orElse("").toString();
		}
		
		String requestID = getCache().get(input.getRegisteredUserId());
		
		if (requestID == null) {
		   return null;
		}
		
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(BASE_URL + "?request_id=" + requestID + "&code=" + input.getResponseCode())  // TODO Replace with field in schema
				.post(EMPTY_BODY)
				.addHeader("x-rapidapi-key", apiKey)
				.addHeader("x-rapidapi-host", RAPID_API_HOST)
				.build();
		
		Response response = client.newCall(request).execute();
		System.out.println(response.message());
		if (response.isSuccessful()) {
			
			// Insert the device id into the RegisteredDevice table, and notify the user of success (send message?)
			ctx.newOutputMessage(validatedDeviceTopic, Schema.AVRO(SMSVerificationResponse.class))
			   .value(input)
			   .sendAsync();
			
		}
		return null;
	}
	
}
