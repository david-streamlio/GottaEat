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
package com.gottaeat.services.geoencoding.google;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.geography.GeoEncodedAddress;
import com.gottaeat.domain.geography.GeoEncodedAddress.Builder;
import com.gottaeat.domain.geography.LatLon;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @see https://developers.google.com/maps/documentation/geocoding/intro 
 *
 */
public class GoogleGeoEncodingService implements Function<Address, Void> {
	
	private static List<String> TRANSIENT_ERRORS = Arrays.asList("UNKNOWN_ERROR");
	private static List<String> NON_TRANSIENT_ERRORS = Arrays.asList("OVER_DAILY_LIMIT", "OVER_QUERY_LIMIT", "REQUEST_DENIED");
	
	private String apiEndpoint;
	private String apiKey;
	private boolean initalized = false;
	private RateLimiterConfig config;
	private RateLimiterRegistry rateLimiterRegistry;
	private RateLimiter rateLimiter;

	@Override
	public Void process(Address addr, Context ctx) throws Exception {
		
		if (!initalized) {
			init(ctx);
		}
		
		CheckedFunction0<String> decoratedFunction =
			Decorators.ofCheckedSupplier(getFunction(addr))
				.withRateLimiter(rateLimiter)
				.decorate();
		
		LatLon ll = getLocation(Try.of(decoratedFunction)
			 .onFailure((Throwable t) -> ctx.getLogger().error(t.getMessage()))
			 .getOrNull());

		Builder result = GeoEncodedAddress.newBuilder()
			.setAddress(addr);
		
		if (ll != null) {
			result.setLatLong(ll);
		} 
		
		ctx.newOutputMessage(ctx.getOutputTopic(), AvroSchema.of(GeoEncodedAddress.class))
		  .properties(ctx.getCurrentRecord().getProperties())
		  .value(result.build())
		  .send();
	
		return null;
	}

	private void init(Context ctx) {
		
		int refrshPeriod = Integer.parseInt(ctx.getUserConfigValueOrDefault(
			"rateLimiter.limitRefreshPeriod", "1").toString());
				
		int limitForPeriod = Integer.parseInt(ctx.getUserConfigValueOrDefault(
			"rateLimiter.limitForPeriod", "60").toString());
					
		int timeout = Integer.parseInt(ctx.getUserConfigValueOrDefault(
			"rateLimiter.timeoutDuration", "1").toString());
		
		config = RateLimiterConfig.custom()
			.limitRefreshPeriod(Duration.ofMinutes(refrshPeriod))
			.limitForPeriod(limitForPeriod)
			.timeoutDuration(Duration.ofSeconds(timeout))
			.build();
		
		rateLimiterRegistry = RateLimiterRegistry.of(config);
		rateLimiter = rateLimiterRegistry.rateLimiter(ctx.getUserConfigValueOrDefault(
			"rateLimiter.name", "name").toString());
		
		apiEndpoint = ctx.getUserConfigValue("api-Endpoint").get().toString();
		apiKey = ctx.getUserConfigValue("api-key").get().toString();
		
		initalized = true;
	}
	
	private CheckedFunction0<String> getFunction(Address addr) {
		CheckedFunction0<String> fn = () -> { 
			OkHttpClient client = new OkHttpClient();
			StringBuilder sb = new StringBuilder()
				.append(apiEndpoint)
				.append(URLEncoder.encode(addr.getStreet().toString(), 
						StandardCharsets.UTF_8.toString())).append(",")
				.append(URLEncoder.encode(addr.getCity().toString(), 
						StandardCharsets.UTF_8.toString())).append(",")
				.append(URLEncoder.encode(addr.getState().toString(), 
						StandardCharsets.UTF_8.toString()))
				.append("&key=").append(apiKey);

			Request request = new Request.Builder()
					.url(sb.toString())
					.build();

			try (Response response = client.newCall(request).execute()) {
				if (response.isSuccessful()) {
					return response.body().string();
				} else {
					String reason = getErrorStatus(response.body().string());
					/* React based on the code
					 *    Input errors: ZERO_RESULTS or INVALID_REQUEST
					 */
					if (NON_TRANSIENT_ERRORS.stream().anyMatch(s -> reason.contains(s))) {
						throw new NonTransientException();
					} else if (TRANSIENT_ERRORS.stream().anyMatch(s -> reason.contains(s))) {
						throw new TransientException();
					}
					return null;
				}
						
			}

		};
		
		return fn;
	}

	private LatLon getLocation(String json) {
		LatLon ll = null;
		try {
			JsonElement jsonTree = new JsonParser().parse(json);

			if (jsonTree.isJsonObject()) {
				JsonObject jsonObject = jsonTree.getAsJsonObject();
				JsonObject geometry = jsonObject.getAsJsonObject("geometry");
				JsonObject location = geometry.getAsJsonObject("location");
				Double lat = location.get("lat").getAsDouble();
				Double lon = location.get("lat").getAsDouble();

				ll = new LatLon();
				ll.setLatitude(lat);
				ll.setLongitude(lon);  
			}
		} catch (Throwable t) {
			// Trap all parsing errors
		}
		
		return ll;
	}
	
	private String getErrorStatus(String json) {
		JsonElement jsonTree = new JsonParser().parse(json);
		if (jsonTree.isJsonObject()) {
			JsonObject jsonObject = jsonTree.getAsJsonObject();
		    JsonElement status = jsonObject.get("status");
		    return status.getAsString();
		}
		return "UNKNOWN_ERROR";
	}

}
