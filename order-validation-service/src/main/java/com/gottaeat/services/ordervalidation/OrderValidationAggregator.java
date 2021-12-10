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
package com.gottaeat.services.ordervalidation;

import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;

import com.gottaeat.domain.order.ValidatedFoodOrder;
import com.gottaeat.domain.payment.PaymentStatus;

public class OrderValidationAggregator implements Function<ValidatedFoodOrder, Void> {
	
	private Logger logger;
	private boolean initalized = false;
	private String validOrdersTopic;
	private String invalidOrdersTopic;
	private LRUMap<String, ValidatedFoodOrder> lruCache = new LRUMap<String, ValidatedFoodOrder>(200);

	@Override
	public Void process(ValidatedFoodOrder in, Context ctx) throws Exception {
		
		if (!initalized) {
			init(ctx);
		}
		
		Map<String, String> props = ctx.getCurrentRecord().getProperties();
		String orderId = props.get("order-id");
		
		logger.info("Received part of order # " + orderId);
		
		ValidatedFoodOrder order;
		if (lruCache.get(orderId) == null) {
			order = ValidatedFoodOrder.newBuilder().build();
		} else {
			order = lruCache.get(orderId);
		}
		
		updateOrder(order, in);
		
		if (isComplete(order)) {
			
			logger.info("Order # " + orderId + " is " + (isValid(order) ? "valid" : "invalid") );
			ctx.newOutputMessage(isValid(order) ? validOrdersTopic :  invalidOrdersTopic, 
				    AvroSchema.of(ValidatedFoodOrder.class))
				.properties(props)
				.value(order)
				.sendAsync();
			
			lruCache.remove(orderId);
		} else {
			lruCache.put(orderId, order);
		}
		
		return null;
	}
	
	private void init(Context ctx) {
		logger = ctx.getLogger();
		
		validOrdersTopic = ctx.getUserConfigValue("valid-orders-topic").get().toString();
		invalidOrdersTopic = ctx.getUserConfigValue("invalid-orders-topic").get().toString();
		
		logger.info("Initalized");
		logger.info("validOrdersTopic = " + validOrdersTopic);
		logger.info("invalidOrdersTopic = " + invalidOrdersTopic);
		
		initalized = true;
	}

	private boolean isComplete(ValidatedFoodOrder order) {
		return (order != null && order.getDeliveryLocation() != null 
				&& order.getFood() != null && order.getPayment() != null
				&& order.getMeta() != null);
	}
	
	private boolean isValid(ValidatedFoodOrder order) {
		return (order.getPayment().getStatus() == PaymentStatus.AUTHORIZED);
	}
	
	private void updateOrder(ValidatedFoodOrder val, ValidatedFoodOrder response) {
		if (response.getDeliveryLocation() != null && val.getDeliveryLocation() == null) {
			val.setDeliveryLocation(response.getDeliveryLocation());
		}
		
		if (response.getFood() != null && val.getFood() == null) {
			val.setFood(response.getFood());
		}
		
		if (response.getMeta() != null && val.getMeta() == null) {
			val.setMeta(response.getMeta());
		}
		
		if (response.getPayment() != null && val.getPayment() == null) {
			val.setPayment(response.getPayment());
		}
	}

}
