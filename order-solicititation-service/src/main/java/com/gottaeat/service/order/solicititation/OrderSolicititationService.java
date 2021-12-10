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

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;

import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.restaurant.SolicitationResponse;
import com.gottaeat.service.order.solicititation.candidates.CandidateSelectorStrategy;
import com.gottaeat.service.order.solicititation.candidates.RandomSelectorStrategy;

/**
 * 
 *  We may want to route an food order to a select list of restaurants to 
 *  obtain a <quote, ETA> for the requested item. Rather than sending the 
 *  request to all vendors, we may want to control which vendors receive 
 *  the request, based on menu item and proximity to the delivery address, etc.
 *  
 * @see https://www.enterpriseintegrationpatterns.com/patterns/messaging/RecipientList.html
 *
 */
public class OrderSolicititationService implements Function<FoodOrder, Void> {
	
	public static final SolicitationResponse NO_WINNER = SolicitationResponse.newBuilder().build();
	
	private boolean initialized = false;
	private String rendevous;
	private CandidateSelectorStrategy selector = new RandomSelectorStrategy();

	private Logger logger;
	
	@Override
	public Void process(FoodOrder order, Context ctx) throws Exception {
	
		if (!initialized) {
			init(ctx);
		}
		
		String orderId = ctx.getCurrentRecord().getProperties().get("order-id");
		
		logger.info("Processing incoming FoodOrder " + orderId);
		
		List<String> cand = selector.getCandidates(order, order.getDeliveryLocation());
		
		if (CollectionUtils.isNotEmpty(cand)) {
			String all = StringUtils.join(cand, ",");
			
			for (String topic: cand) {
				try {
					logger.info("Sending order # " + orderId + " to " + topic);
					ctx.newOutputMessage(topic, AvroSchema.of(FoodOrder.class))
					  .property("order-id", orderId)
					  .property("all-restaurants", all)
					  .property("return-addr", rendevous)
					  .value(order)
					  .send();
					
				} catch (PulsarClientException e) {
					logger.error("Boom", e);
				}
			}
			
			// Send a 'times up" message in case all requests timeout, etc.
			ctx.newOutputMessage(rendevous, AvroSchema.of(SolicitationResponse.class))
				.value(NO_WINNER)
				.property("order-id", order.getMeta().getOrderId() + "")
				.deliverAfter(1, TimeUnit.MINUTES);
						
		} else {
			// TODO Handle the case where we have no candidates
			logger.info("No restauratns available for order # to ");
			ctx.newOutputMessage(rendevous, AvroSchema.of(SolicitationResponse.class))
			   .value(NO_WINNER)
			   .property("order-id", order.getMeta().getOrderId() + "");
		}

		return null;
	}
	
	private void init(Context ctx) {
		logger = ctx.getLogger();
		rendevous = (String) ctx.getUserConfigValue("rendevous-topic").get();
		initialized = true;
		logger.warn("Initialization Complete");
	}
	
}
