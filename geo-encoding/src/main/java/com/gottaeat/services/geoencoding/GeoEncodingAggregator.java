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
package com.gottaeat.services.geoencoding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.geography.GeoEncodedAddress;

public class GeoEncodingAggregator implements Function<GeoEncodedAddress, Void> {

	private Map<String, List<GeoEncodedAddress>> responses = 
		new HashMap<String, List<GeoEncodedAddress>> ();
	
	@Override
	public Void process(GeoEncodedAddress addr, Context ctx) throws Exception {
		
		String orderId = ctx.getCurrentRecord().getProperties().get("order-id");
		ctx.getLogger().info("Processing encoded address for order id : " + orderId);
		
		GeoEncodedAddress best = null;
		
		if (addr == null) {
			// We received the "times up" message, so pick the best from what we have
			best = winner(orderId);
		} else {
			best = addToResponses(ctx.getCurrentRecord().getProperties(), addr);
		}
		
		if (best != null) {
			ctx.newOutputMessage(ctx.getOutputTopic(), AvroSchema.of(GeoEncodedAddress.class))
				.value(best)
				.property("order-id", orderId.toString())
				.sendAsync();
			
			// Clear out the cache
			responses.remove(orderId);
		}
		
		return null;
	}
	
	private GeoEncodedAddress winner(String correlationId) {
		
		if (responses.containsKey(correlationId)) {
			return responses.get(correlationId).get(0);
		}
		
		return null;
	}

	private GeoEncodedAddress addToResponses(Map<String, String> map, GeoEncodedAddress addr) {
		
		int expectedResponses = map.containsKey("requests-snet") ?
			Integer.parseInt(map.get("requests-sent")) : 1;
		String orderId = map.get("order-id");
		
		if (!responses.containsKey(orderId)) {
			responses.put(orderId, new ArrayList<GeoEncodedAddress>());
		}
		responses.get(orderId).add(addr);
		return (responses.get(orderId).size() >= expectedResponses) ? winner(orderId) : null;
	}

}
