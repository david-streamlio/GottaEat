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
package com.gottaeat.services.customer.simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.order.FoodOrderMeta;
import com.gottaeat.domain.order.OrderStatus;
import com.gottaeat.domain.payment.CardType;
import com.gottaeat.domain.payment.CreditCard;
import com.gottaeat.domain.payment.Payment;
import com.gottaeat.domain.payment.PaymentAmount;
import com.gottaeat.domain.payment.PaymentMethod;
import com.gottaeat.domain.restaurant.FoodOrderDetail;
import com.gottaeat.domain.restaurant.MenuItem;


public class FoodOrderGenerator implements DataGenerator<FoodOrder> {
	
	private static Random rnd = new Random();
	
	private static List<Address> ADDRESSES = new ArrayList<Address> ();
	private static List<CreditCard> CREDIT_CARDS = new ArrayList<CreditCard> ();
	private static List<List<MenuItem>> MENUS = new ArrayList<List<MenuItem>> ();
	private static List<List<String>> CUSTOMIZATIONS = new ArrayList<List<String>>();
	private static List<String> DRINKS = new ArrayList<String> ();
	
	private static long orderIdSequence = rnd.nextInt(75000) + 5678;
	
	static {
		ADDRESSES.add(Address.newBuilder().setCity("Chicago").setState("IL").setCountry("USA").setStreet("123 Main St").setZip("66011").build());
		ADDRESSES.add(Address.newBuilder().setCity("Chicago").setState("IL").setCountry("USA").setStreet("709 W 18th St").setZip("66012").build());
		ADDRESSES.add(Address.newBuilder().setCity("Chicago").setState("IL").setCountry("USA").setStreet("3422 Central Park Ave").setZip("66013").build());
		ADDRESSES.add(Address.newBuilder().setCity("Chicago").setState("IL").setCountry("USA").setStreet("844 W Cermark Rd").setZip("66014").build());
		ADDRESSES.add(Address.newBuilder().setCity("Chicago").setState("IL").setCountry("USA").setStreet("651 S Pulaski St").setZip("66015").build());
		CREDIT_CARDS.add(CreditCard.newBuilder().setAccountNumber("1234 5678 9012 3456").setBillingZip("66011").setCardType(CardType.AMEX).setExpMonth(1).setExpYear(2025).setCcv("000").build());
		CREDIT_CARDS.add(CreditCard.newBuilder().setAccountNumber("1111 2222 3333 4444").setBillingZip("66012").setCardType(CardType.DISCOVER).setExpMonth(1).setExpYear(2025).setCcv("789").build());
		CREDIT_CARDS.add(CreditCard.newBuilder().setAccountNumber("5555 6666 7777 8888").setBillingZip("66011").setCardType(CardType.MASTERCARD).setExpMonth(1).setExpYear(2025).setCcv("123").build());
		CREDIT_CARDS.add(CreditCard.newBuilder().setAccountNumber("9999 0000 1111 2222").setBillingZip("66013").setCardType(CardType.VISA).setExpMonth(1).setExpYear(2025).setCcv("555").build());
		
		List<MenuItem> menu = new ArrayList<MenuItem> ();
		menu.add(MenuItem.newBuilder().setItemDescription("Beef").setItemId(1).setItemName("Burrito").setPrice(7.99f).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Carne Asada").setItemId(2).setItemName("Taco").setPrice(1.99f).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Chicken").setItemId(3).setItemName("Fajita").setPrice(6.99f).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Small").setItemId(10).setItemName("Fountain Drink").setPrice(1.00f).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Large").setItemId(11).setItemName("Fountain Drink").setPrice(2.00f).build());
		
		
		MENUS.add(menu);
		List<String> customizations = new ArrayList<String> ();
		customizations.add("Guacamole");
		customizations.add("Sour Cream");
		customizations.add("Extra Cheese");
		CUSTOMIZATIONS.add(customizations);
		
		menu = new ArrayList<MenuItem> ();
		menu.add(MenuItem.newBuilder().setItemDescription("Single").setItemId(1).setItemName("Cheeseburger").setPrice(2.05f).setTaxable(false).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Double").setItemId(2).setItemName("Cheeseburger").setPrice(3.95f).setTaxable(false).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Single").setItemId(3).setItemName("Hamburger").setPrice(1.75f).setTaxable(false).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Double").setItemId(4).setItemName("Hamburger").setPrice(3.25f).setTaxable(false).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Chicken").setItemId(5).setItemName("Sandwich").setPrice(1.95f).setTaxable(false).build());
		menu.add(MenuItem.newBuilder().setItemDescription("20 Piece").setItemId(6).setItemName("Nuggets").setPrice(3.95f).setTaxable(false).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Small").setItemId(7).setItemName("French Fries").setPrice(1.00f).setTaxable(false).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Large").setItemId(8).setItemName("French Fries").setPrice(2.00f).setTaxable(false).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Small").setItemId(10).setItemName("Fountain Drink").setPrice(1.00f).setTaxable(false).build());
		menu.add(MenuItem.newBuilder().setItemDescription("Large").setItemId(11).setItemName("Fountain Drink").setPrice(2.00f).setTaxable(false).build());
		
		MENUS.add(menu);
		
		customizations = new ArrayList<String> ();
		customizations.add("No onions");
		customizations.add("Extra Mayo");
		customizations.add("No Pickles");
		CUSTOMIZATIONS.add(customizations);
		
		DRINKS.add("Coca-Cola");
		DRINKS.add("Diet Coke");
		DRINKS.add("Sprite");
		DRINKS.add("Lemonade");
	}
	
	
	public FoodOrder generate() { 
		
		int restaurantId = rnd.nextInt(MENUS.size());
		Pair<List<FoodOrderDetail>, Float> orderDetails = getRandomOrderDetails(restaurantId, rnd.nextInt(3) + 1);
		
		return FoodOrder.newBuilder()
						.setMeta(FoodOrderMeta.newBuilder()
								.setCustomerId(rnd.nextInt(5000))
								.setOrderId(orderIdSequence++)
								.setOrderStatus(OrderStatus.NEW)
								.setTimePlaced(System.currentTimeMillis() + "")
								.build())
						.setDeliveryLocation(getRandomAddress())
						.setFood(orderDetails.getLeft())
						.setPayment(getRandomCreditCard(orderDetails.getRight()))
						.build();
	}
	
	private Address getRandomAddress() {
		return ADDRESSES.get(rnd.nextInt(ADDRESSES.size()));
	}
	
	private Payment getRandomCreditCard(float amount) {
		
		CreditCard card = CREDIT_CARDS.get(rnd.nextInt(CREDIT_CARDS.size()));
		
		return Payment.newBuilder()
				  .setMethodOfPayment(PaymentMethod.newBuilder()
					.setType(card)
					.build())
				  .setAmount(PaymentAmount.newBuilder()
					.setFoodTotal(amount)
					.setTax(0.0f)
					.setTotal(amount)
					.build())
				.build();
	}

	private Pair<List<FoodOrderDetail>, Float> getRandomOrderDetails(int restaurantId ,int numItems) {
		
		List<MenuItem> menu = MENUS.get(restaurantId);
		List<FoodOrderDetail> details = new ArrayList<FoodOrderDetail>();
		float total = 0.0f;
		for (int idx = 0; idx < numItems; idx++) {
			
			MenuItem item = menu.get(rnd.nextInt(menu.size()));
			addCustomizations(item, CUSTOMIZATIONS.get(restaurantId));
			
			int quantity = rnd.nextInt(10)+1;
			
			FoodOrderDetail od = FoodOrderDetail.newBuilder()
								.setFoodItem(item)
								.setQuantity(quantity)
								.build();
			details.add(od);
			total = total + (item.getPrice() * quantity);
		}
		return Pair.of(details, total);
	}

	private void addCustomizations(MenuItem item, List<String> list) {
		if (item.getItemId() >= 10) { // All drinks need a type
			item.setCustomizations(Collections.singletonList(DRINKS.get(rnd.nextInt(DRINKS.size()))));
		} else if (rnd.nextBoolean()) { // Randomly add others
			item.setCustomizations(Collections.singletonList(list.get(rnd.nextInt(list.size()))));
		}
	}
}
