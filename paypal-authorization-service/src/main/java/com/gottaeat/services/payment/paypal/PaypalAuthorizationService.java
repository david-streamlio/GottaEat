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
package com.gottaeat.services.payment.paypal;

import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.payment.AuthorizedPayment;
import com.gottaeat.domain.payment.Payment;
import com.gottaeat.services.payment.paypal.authorization.AuthorizationStrategy;


/**
 * @see https://developer.paypal.com/docs/api/get-an-access-token-curl/
 */
public class PaypalAuthorizationService implements Function<Payment, Void> {

	private static final String DEFAULT_STRATEGY = "com.gottaeat.services.payment.paypal.authorization.RandomAuthorizationStrategy";
	private boolean initalized = false;
	private AuthorizationStrategy authorizer;
	
	@Override
	public Void process(Payment pay, Context ctx) throws Exception {

		if (!initalized) {
			init(ctx);
		}
		
		AuthorizedPayment result = authorizer.authorize(pay);
		
		ctx.newOutputMessage(ctx.getOutputTopic(), AvroSchema.of(AuthorizedPayment.class))
			.properties(ctx.getCurrentRecord().getProperties())
			.value(result)
			.send();
		
		return null;
	}

	private void init(Context ctx) throws Exception {
		
		String authorizeClassName = ctx.getUserConfigValueOrDefault(
			"authorizer-classname", DEFAULT_STRATEGY).toString();
		
		authorizer = (AuthorizationStrategy) Class.forName(authorizeClassName)
			.getDeclaredConstructor().newInstance();
		
		authorizer.initalize(ctx);
		
		initalized = true;
	}

}
