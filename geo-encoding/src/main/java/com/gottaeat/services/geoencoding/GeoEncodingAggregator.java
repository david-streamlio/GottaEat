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
import java.util.UUID;

import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.geography.Address;

public class GeoEncodingAggregator implements Function<Address, Void> {

	private Map<UUID, List<Address>> responses = new HashMap<UUID, List<Address>> ();
	
	@Override
	public Void process(Address addr, Context ctx) throws Exception {
		
		UUID correlationId = UUID.fromString(ctx.getCurrentRecord().getProperties().get("correlation-ID"));
		Address best = null;
		
		if (addr == null) {
			// We received the "times up" message, so pick the best from what we have
			best = winner(correlationId);
		} else {
			best = addToResponses(ctx.getCurrentRecord().getProperties(), addr);
		}
		
		if (best != null) {
			ctx.newOutputMessage("", AvroSchema.of(Address.class))
				.value(best)
				.property("correlation-ID", correlationId.toString())
				.sendAsync();
		}
		
		return null;
	}
	
	private Address winner(UUID correlationId) {
		
		if (responses.containsKey(correlationId)) {
			return responses.get(correlationId).get(0);
		}
		return null;
	}

	private Address addToResponses(Map<String, String> map, Address addr) {
		UUID uuid = UUID.fromString(map.get("correlation-ID"));
		int expectedResponses = Integer.parseInt(map.get("requests-sent"));
		
		if (!responses.containsKey(uuid)) {
			responses.put(uuid, new ArrayList<Address>());
		}
		responses.get(uuid).add(addr);
		return (responses.get(uuid).size() >= expectedResponses) ? winner(uuid) : null;
	}

}
