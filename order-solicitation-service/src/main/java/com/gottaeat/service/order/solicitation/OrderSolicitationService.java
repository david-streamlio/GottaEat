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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;

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
public class OrderSolicitationService implements Function<FoodOrder, Void> {

	private String rendevous = "persistent://restaurants/inbound/accepted";
	
	@Override
	public Void process(FoodOrder order, Context ctx) throws Exception {
		
		List<String> cand = getCandidates(order, order.getDeliveryLocation());
		
		if (CollectionUtils.isNotEmpty(cand)) {
			String all = StringUtils.join(cand, ",");
			int delay = 0;
			for (String topic: cand) {
				try {
					ctx.newOutputMessage(topic, AvroSchema.of(FoodOrder.class))
					.property("order-id", order.getMeta().getOrderId() + "")
					.property("all-restaurants", all)
					.property("return-addr", rendevous)
					.value(order).deliverAfter((delay++ * 10), TimeUnit.SECONDS);
				} catch (PulsarClientException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
	
	private List<String> getCandidates(FoodOrder order, Address deliveryAddr) {
		List<String> restaurants = new ArrayList<String> ();
		Collections.addAll(restaurants, "", "");
		return restaurants;
	}

}
