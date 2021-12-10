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
package com.gottaeat.service.order.solicititation;

import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.commons.state.LRUCacheStateStore;
import com.gottaeat.commons.state.StateStore;
import com.gottaeat.domain.restaurant.SolicitationResponse;

/**
 * 
 * @see https://www.enterpriseintegrationpatterns.com/patterns/messaging/Aggregator.html
 * 
 * Assumes response will be <restaurant, eta-pickup> tuple.
 */
public class OrderSolicititationAggregator implements Function<SolicitationResponse, Void> {
	
	private StateStore storage = new LRUCacheStateStore();
	
	@Override
	public Void process(SolicitationResponse response, Context ctx) throws Exception {
		
		String orderId = ctx.getCurrentRecord().getProperties().get("order-id");
		
		ctx.getLogger().info("Received response for order # " + orderId);
		
		if (response.equals(OrderSolicititationService.NO_WINNER)) {
			sendWinner(response, ctx);
		} else if (storage.getState(orderId) == null) {
			// storage.putState(orderId, response);
			// First response wins
			sendWinner(response, ctx);
			notifyWinner(ctx);
		} else {
			// Ignore all late bidders.
			notifyLoser(ctx);
		}
		
		return null;
	}
	
	/*
	 * Send the "winning" bid to the order validation aggregator
	 */
	private void sendWinner(SolicitationResponse response, Context ctx) throws PulsarClientException {
		closeBidding(ctx);
		ctx.newOutputMessage(ctx.getOutputTopic(), AvroSchema.of(SolicitationResponse.class))
		  .properties(ctx.getCurrentRecord().getProperties())
		  .value(response)
		  .sendAsync();
	}

	private void closeBidding( Context ctx) {
		// Record the restaurant that was awarded the order.
//		ByteBuffer bb = ByteBuffer.allocate(32);
//		bb.asLongBuffer().put(Long.parseLong(ctx.getCurrentRecord().getProperties().get("restaurant-id")));	
//		ctx.putState(ctx.getCurrentRecord().getProperties().get("order-id"), bb);
	}
	
	private void notifyWinner(Context ctx) throws PulsarClientException {
//		ctx.newOutputMessage(ctx.getCurrentRecord().getProperties().get("return-addr"), Schema.STRING)
//			.property("order-id", ctx.getCurrentRecord().getProperties().get("order-id"))
//			.value(WINNER)
//			.sendAsync();
	}

	private void notifyLoser(Context ctx) throws PulsarClientException {
//		ctx.newOutputMessage(ctx.getCurrentRecord().getProperties().get("return-addr"), Schema.STRING)
//			.property("order-id", ctx.getCurrentRecord().getProperties().get("order-id"))
//			.value(LOSER)
//			.sendAsync();
		
	}
}
