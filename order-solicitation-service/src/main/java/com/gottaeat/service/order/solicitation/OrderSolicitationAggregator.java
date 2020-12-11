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
package com.gottaeat.service.order.solicitation;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.restaurant.SolicitationResponse;

/**
 * 
 * @see https://www.enterpriseintegrationpatterns.com/patterns/messaging/Aggregator.html
 * 
 * Assumes response will be <restaurant, eta-pickup> tuple.
 */
public class OrderSolicitationAggregator implements Function<SolicitationResponse, Void> {

	@Override
	public Void process(SolicitationResponse response, Context context) throws Exception {
	
		Map<String, String> props = context.getCurrentRecord().getProperties();
		String correlationId = props.get("order-id");
		List<String> bids = Arrays.asList(StringUtils.split(props.get("all-restaurants")));
		
		if (context.getState(correlationId) == null) {
			// First response wins
			context.newOutputMessage(props.get("return-addr"), AvroSchema.of(SolicitationResponse.class))
				.property("order-id", correlationId.toString())
				.value(response)
				.sendAsync();
			
			String winner = props.get("restaurant-id");
			bids.remove(winner);
			notifyWinner(winner, context);
			notifyLosers(bids, context);
			
			// Record the time we received the winning bid.
			ByteBuffer bb = ByteBuffer.allocate(32);
			bb.asLongBuffer().put(System.currentTimeMillis());
			
			context.putState(correlationId, bb);
		} 
		
		return null;
	}

	private void notifyLosers(List<String> bids, Context context) {
		// TODO Auto-generated method stub
		
	}

	private void notifyWinner(String s, Context context) {
		// TODO Auto-generated method stub
	}
}
