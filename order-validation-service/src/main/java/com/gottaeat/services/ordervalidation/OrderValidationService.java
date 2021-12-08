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

import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;

import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.order.FoodOrderMeta;
import com.gottaeat.domain.payment.Payment;

/**
 * Use a Splitter to break out the composite message into a series of 
 * individual messages, each containing data related to one item.
 * 
 * @see https://www.enterpriseintegrationpatterns.com/patterns/messaging/Sequencer.html
 *
 */
public class OrderValidationService implements Function<FoodOrder, Void> {
	
	private Logger logger;
	private boolean initalized;
	private String geoEncoderTopic;
	private String paymentTopic;
	private String restaurantTopic;
	private String orderTopic;

	@Override
	public Void process(FoodOrder order, Context ctx) throws Exception {
	  
	  if (!initalized) {
	    init(ctx);
	  }
	  
	  logger.info("Validating Food Order " + order.getMeta().getOrderId());
			
	  ctx.newOutputMessage(geoEncoderTopic, AvroSchema.of(Address.class))
		  .property("order-id", order.getMeta().getOrderId() + "")
	      .value(order.getDeliveryLocation()).sendAsync();
		
	  ctx.newOutputMessage(paymentTopic, AvroSchema.of(Payment.class))
		  .property("order-id", order.getMeta().getOrderId() + "")
		  .value(order.getPayment()).sendAsync();

	  ctx.newOutputMessage(orderTopic, AvroSchema.of(FoodOrderMeta.class))
		  .property("order-id", order.getMeta().getOrderId() + "")
		  .value(order.getMeta()).sendAsync();

	  ctx.newOutputMessage(restaurantTopic, AvroSchema.of(FoodOrder.class))
		  .property("order-id", order.getMeta().getOrderId() + "")
		  .value(order).sendAsync();

	  return null;
	}
	
	private void init(Context ctx) {
	  logger = ctx.getLogger();
	  geoEncoderTopic = (String) ctx.getUserConfigValue("geo-topic").get();
	  paymentTopic = (String) ctx.getUserConfigValue("payment-topic").get();
	  restaurantTopic = (String) ctx.getUserConfigValue("restaurant-topic").get();
	  orderTopic = (String) ctx.getUserConfigValue("order-topic").get();
	  
	  logger.info("Initalized");
	  logger.info("geoEncoderTopic = " + geoEncoderTopic);
	  logger.info("paymentTopic = " + paymentTopic);
	  logger.info("restaurantTopic = " + restaurantTopic);
	  logger.info("orderTopic = " + orderTopic);
	  
	  initalized = true;
	}

}
