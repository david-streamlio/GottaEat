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
package com.gottaeat.services.payment.creditcard.authorization;

import java.util.Random;
import java.util.UUID;

import org.apache.pulsar.functions.api.Context;

import com.gottaeat.domain.payment.AuthorizedPayment;
import com.gottaeat.domain.payment.CreditCard;
import com.gottaeat.domain.payment.Payment;
import com.gottaeat.domain.payment.PaymentMethod;
import com.gottaeat.domain.payment.PaymentStatus;

public class RandomAuthorizationStrategy implements AuthorizationStrategy {

	private Random rnd = new Random();
	private int approvalPercentage;
	
	@Override
	public AuthorizedPayment authorize(Payment pay) {
		String approvalCode = (rnd.nextInt(100) < approvalPercentage) ?
			UUID.randomUUID().toString() : null;
		
		AuthorizedPayment auth = AuthorizedPayment.newBuilder()
			.setPayment(pay)
			.setStatus( (approvalCode != null) ? PaymentStatus.AUTHORIZED : PaymentStatus.REJECTED)
			.setApprovalCode(approvalCode)
		.build();
				
		return auth;
	}

	@Override
	public void initalize(Context ctx) {
		approvalPercentage = Integer.parseInt(ctx.getUserConfigValueOrDefault(
			"simulation.approval.percentage", "90").toString());
	}

}
