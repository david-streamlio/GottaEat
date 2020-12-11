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
package com.gottaeat.features.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.pulsar.functions.api.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gottaeat.domain.fraud.PaymentType;
import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.order.FoodOrderMeta;
import com.gottaeat.domain.order.OrderStatus;
import com.gottaeat.domain.payment.Payment;
import com.gottaeat.domain.payment.PaymentAmount;
import com.gottaeat.domain.payment.PaymentMethod;
import com.gottaeat.domain.restaurant.FoodOrderDetail;

public class CustomerFeaturesLookupTest {

	@Mock
	private Context mockContext;
	
	private CustomerFeaturesLookup service;
	
	@Before
	public final void init() {
		MockitoAnnotations.initMocks(this);
		when(mockContext.getUserConfigValueOrDefault(CustomerFeaturesLookup.HOSTNAME_KEY, "127.0.0.1"))
		  .thenReturn("127.0.0.1");
		
		when(mockContext.getUserConfigValueOrDefault(CustomerFeaturesLookup.PORT_KEY, 9042))
		  .thenReturn(new Integer(9042));
		
		service = new CustomerFeaturesLookup();
	}
	
//	@Test
	public final void lookupTest() throws Exception {
		CustomerFeatures result = service.process(generateFoodOrder(1), mockContext);
		assertNotNull(result);
		assertEquals(1, result.getCustomerId());
		assertEquals(17.99, result.getAvgOrderPrice(), 0.0);
		assertEquals(100.0, result.getPercentageOfHomeDeliveries(), 0.0);
	}
	
//	@Test
	public final void missingRecordTest() throws Exception {
		CustomerFeatures result = service.process(generateFoodOrder(100), mockContext);
		assertNull(result);
	}
	
	private FoodOrder generateFoodOrder(long customerID) {
		return FoodOrder.newBuilder()
				.setDeliveryLocation(Address.newBuilder()
						.setCity("New Orleans")
						.setCountry("USA")
						.setState("LA")
						.setStreet("101 Beale St")
						.setZip("34567")
						.build())
				.setFood(getFood())
				.setMeta(FoodOrderMeta.newBuilder()
						.setCustomerId(customerID)
						.setOrderId(123)
						.setOrderStatus(OrderStatus.NEW)
						.setTimePlaced("2020-12-11")
						.build())
				.setPayment(Payment.newBuilder()
						.setAmount(PaymentAmount.newBuilder()
								.setFoodTotal(11.99f)
								.setTax(1.98f)
								.setTotal(13.97f)
								.build())
						.setMethodOfPayment(PaymentMethod.newBuilder()
								.setType(PaymentType.CREDITCARD)
								.build())
						.build())
				.build();
	}

	private List<FoodOrderDetail> getFood() {
		List<FoodOrderDetail> food = new ArrayList<FoodOrderDetail>();
		return food;
	}
}
