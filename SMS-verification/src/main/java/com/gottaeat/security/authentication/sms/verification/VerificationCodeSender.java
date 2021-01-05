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

import com.gottaeat.domain.user.ActiveUser;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VerificationCodeSender extends VerificationCodeBase implements Function<ActiveUser, Void> {

	private static final String BASE_URL = "https://" + RAPID_API_HOST + "/send-verification-code";
	private static final String REQUEST_ID_HEADER = "x-amzn-requestid";
	
	@Override
	public Void process(ActiveUser input, Context ctx) throws Exception {
		if (!isInitalized()) {
			apiKey = (String) ctx.getUserConfigValue(API_KEY).orElse(null);
			datagridUrl = ctx.getUserConfigValue(DATAGRID_KEY).orElse("localhost:10800").toString();
			cacheName = ctx.getUserConfigValue(CACHENAME_KEY).orElse("com.gottaeat.data.location").toString();
		}
		
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(BASE_URL + "?phoneNumber=" + toE164FormattedNumber(input.getDetails().getPhoneNumber().toString()) + 
						        "&brand=GottaEat")
				.post(EMPTY_BODY)
				.addHeader("x-rapidapi-key", apiKey)
				.addHeader("x-rapidapi-host", RAPID_API_HOST)
				.build();
		
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			String msg = response.message();  
			System.out.println(msg);
			String requestID = response.header(REQUEST_ID_HEADER);
			if (StringUtils.isNotBlank(requestID)) {
		  	  getCache().put(input.getUser().getRegisteredUserId(), requestID);
			}
		}
		return null;
	}
	
	private String toE164FormattedNumber(String s) {
		if (s.startsWith("+")) {
			return s;
		}
		
		StringBuffer sb = new StringBuffer().append("+1").append(s);
		return sb.toString();
	}
	
}
