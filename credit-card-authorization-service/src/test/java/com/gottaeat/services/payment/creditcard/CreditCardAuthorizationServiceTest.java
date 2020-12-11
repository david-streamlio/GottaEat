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
package com.gottaeat.services.payment.creditcard;

import org.junit.Before;
import org.junit.Test;

import com.gottaeat.domain.payment.CreditCard;

public class CreditCardAuthorizationServiceTest {

	private CreditCardAuthorizationService service;
	
	@Before
	public final void init() {
		service = new CreditCardAuthorizationService();
	}
	
	@Test
	public final void simpleTest() throws Exception {
		CreditCard pay = new CreditCard();
		pay.setAccountNumber("4242424242424242");
		pay.setBillingZip("90210");
		pay.setCcv("123");
		pay.setExpMonth("08");
		pay.setExpYear("2020");
//		service.process(pay, null);
	}
}
