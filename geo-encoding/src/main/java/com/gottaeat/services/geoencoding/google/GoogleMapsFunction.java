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

import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;

import com.google.maps.GeoApiContext;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LocationType;
import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.geography.GeoEncodedAddress;
import com.gottaeat.domain.geography.GeoEncodedAddress.Builder;
import com.gottaeat.domain.geography.LatLon;

public class GoogleMapsFunction implements Function<Address, Void> {

	private Logger logger;
	boolean initalized = false;
	String apiKey;
//	String resultTopic;
	String failureNotificationTopic;
	int maxRetries, retryTimeout;
	
	GeoApiContext geoContext;
	
	@Override
	public Void process(Address addr, Context ctx) throws Exception {
		
		if (!initalized) {
			init(ctx);
		}
		
		logger.info("Encoding address for order # " + 
			ctx.getCurrentRecord().getProperties().get("order-id"));
		
		Builder result = GeoEncodedAddress.newBuilder()
				.setAddress(addr);
				
		try {
			GeocodingResult[] results = null;
//				GeocodingApi.geocode(geoContext, formatAddress(addr)).await();

			Geometry geo = getMostPrecise(results);
			
			if (geo != null) {
				LatLon ll = new LatLon();
				ll.setLatitude(geo.location.lat);
				ll.setLongitude(geo.location.lng);
				result.setLatLong(ll);
			}
			
			ctx.newOutputMessage(ctx.getOutputTopic(), AvroSchema.of(GeoEncodedAddress.class))
				.value(result.build())
				.properties(ctx.getCurrentRecord().getProperties())
				.send();
			
		} catch (Exception ex) {
			ctx.getCurrentRecord().fail();
			ctx.getLogger().error(ex.getMessage());
			ctx.newOutputMessage(failureNotificationTopic, AvroSchema.of(Address.class)).send();
		}
		
		return null;
	}

	private void init(Context ctx) {
		
		logger = ctx.getLogger();
		
		failureNotificationTopic = (String) ctx.getUserConfigValue("failure-notification-topic").get();
//		resultTopic = (String) ctx.getUserConfigValue("result-topic").get();
		apiKey = (String) ctx.getUserConfigValue("service-api-key").get();
		
		maxRetries = Integer.parseInt((String) 
			ctx.getUserConfigValue("service-max-retries").get());
		
		
		retryTimeout = Integer.parseInt( (String)
			ctx.getUserConfigValue("service-retry-timeout-ms").get());
		
		geoContext = new GeoApiContext.Builder()
			    .apiKey(apiKey)
			    .maxRetries(maxRetries)
			    .retryTimeout(retryTimeout, TimeUnit.MILLISECONDS)
			    .build();
		
		initalized = true;
	}

	@SuppressWarnings("unused")
	private String formatAddress(Address addr) {
		return new StringBuilder()
				.append(addr.getStreet())
				.append(" ")
				.append(addr.getCity())
				.append(" ")
				.append(addr.getState())
				.append(" ")
				.append(addr.getZip())
				.toString();
	}
	
	/**
	 * We will base this on the LocationType. 
	 *  ROOFTOP > RANGE_INTERPOLATED > GEOMETRIC_CENTER > APPROXIMATE > UNKNOWN
	 * 
	 * @see https://www.javadoc.io/static/com.google.maps/google-maps-services/0.11.0/com/google/maps/model/LocationType.html
	 * @param results
	 * @return
	 */
	private Geometry getMostPrecise(GeocodingResult[] results) {
		if (results == null || results.length < 1)
			return null;
		
		Geometry best = results[0].geometry;
		for (int idx = 1; idx < results.length; idx++) {
			switch (best.locationType) {
			case UNKNOWN : 
				best = results[idx].geometry;
				break;

			case APPROXIMATE: 
				if (results[idx].geometry.locationType != LocationType.UNKNOWN) {
					best = results[idx].geometry;
					break;
				}
			case GEOMETRIC_CENTER:
				if (results[idx].geometry.locationType == LocationType.RANGE_INTERPOLATED ||
					results[idx].geometry.locationType == LocationType.ROOFTOP) {
					best = results[idx].geometry;
					break;
				}
			case RANGE_INTERPOLATED: 	
				if (results[idx].geometry.locationType == LocationType.ROOFTOP) {
					best = results[idx].geometry;
					break;
				}
			case ROOFTOP: break;

			}
		}
		return null;
	}
}
