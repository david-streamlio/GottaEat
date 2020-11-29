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
package com.gottaeat.services.order.fraud;

import java.util.ArrayList;
import java.util.List;

import com.gottaeat.domain.customer.CustomerDetails;
import com.gottaeat.domain.fraud.OrderScoringData;
import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.order.FoodOrderMeta;
import com.gottaeat.domain.order.OrderStatus;
import com.gottaeat.domain.payment.CardType;
import com.gottaeat.domain.payment.CreditCard;
import com.gottaeat.domain.payment.Payment;
import com.gottaeat.domain.payment.PaymentAmount;
import com.gottaeat.domain.payment.PaymentMethod;
import com.gottaeat.domain.resturant.FoodOrderDetail;
import com.gottaeat.domain.resturant.MenuItem;

public class MockOrderProvider {
	
	public static OrderScoringData getOrder() {
		return OrderScoringData
				.newBuilder()
				.setCustomer(CustomerDetails
						.newBuilder()
						.setBillingAddress(getAddress())
						.setEmail("foo@work.com")
						.setFirstName("Foo")
						.setLastName("Bar")
						.setPhoneNumber("762-831-5507")
						.setUserId(123)
						.build())
				.setOrder(FoodOrder
						.newBuilder()
						.setDeliveryLocation(getAddress())
						.setMeta(FoodOrderMeta
								.newBuilder()
								.setCustomerId(5235)
								.setOrderId(456)
								.setOrderStatus(OrderStatus.NEW)
								.setTimePlaced("2020-11-24 11:12:13.987")
								.build())
						.setPayment(Payment
								.newBuilder()
								.setAmount(PaymentAmount
										.newBuilder()
										.setFoodTotal(8.99f)
										.setTax(0.0f)
										.setTotal(8.99f)
										.build())
								.setMethodOfPayment(PaymentMethod
										.newBuilder()
										.setType(CreditCard
												.newBuilder()
												.setAccountNumber("123456789012")
												.setBillingZip("75001")
												.setCardType(CardType.VISA)
												.setCcv("321")
												.setExpMonth("10")
												.setExpYear("2025")
												.build())
										.build())
								.build())
						.setFood(getFood())
						.build())
				.build();
	}
	
	private static List<FoodOrderDetail> getFood() {
		List<FoodOrderDetail> food = new ArrayList<FoodOrderDetail>();
		food.add(FoodOrderDetail
				.newBuilder()
				.setFoodItem(MenuItem
						.newBuilder()
						.setItemDescription("Chicken Nuggets")
						.setItemId(45)
						.setItemName("Chicken Nuggets")
						.setPrice(8.99f)
						.setTaxable(false)
						.build())
				.setQuantity(1)
				.build());
		return food;
	}

	private static Address getAddress() {
		return Address.newBuilder()
		.setCity("Dallas")
		.setCountry("USA")
		.setState("TX")
		.setStreet("123 Main St")
		.setZip("75001")
		.build();
	}
}
