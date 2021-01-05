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

import org.apache.commons.lang3.StringUtils;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class VerificationCodeBase {

	public static final String API_KEY = "apiKey";
	public static final String CACHENAME_KEY = "cacheName";
	public static final String DATAGRID_KEY = "datagridUrl";
	
	protected static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	protected static final RequestBody EMPTY_BODY = RequestBody.create("", JSON);
	
	protected static final String RAPID_API_HOST = "nexmo-nexmo-sms-verify-v1.p.rapidapi.com";
	
	protected String apiKey;
	protected IgniteClient client;
	protected ClientCache<Long, String> cache;
	
	protected String datagridUrl;
	protected String cacheName;
	
	protected boolean isInitalized() {
		return StringUtils.isNotBlank(apiKey);
	}
	
	protected ClientCache<Long, String> getCache() {
		if (cache == null) {
			cache = getClient().getOrCreateCache(cacheName);
		}
		return cache;
	}
	
	protected IgniteClient getClient() {
		if (client == null) {
			ClientConfiguration cfg = new ClientConfiguration().setAddresses(datagridUrl);
			client = Ignition.startClient(cfg);
		}
		return client;
	}
}
