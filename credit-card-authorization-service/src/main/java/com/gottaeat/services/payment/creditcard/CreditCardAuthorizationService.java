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

import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;

import com.gottaeat.domain.payment.AuthorizedPayment;
import com.gottaeat.domain.payment.Payment;
import com.gottaeat.services.payment.creditcard.authorization.AuthorizationStrategy;


public class CreditCardAuthorizationService implements Function<Payment, Void> {
	
	private static final String DEFAULT_STRATEGY = "com.gottaeat.services.payment.creditcard.authorization.RandomAuthorizationStrategy";
	private Logger logger;
	private boolean initalized = false;
	private AuthorizationStrategy authorizer;
	
	@Override
	public Void process(Payment card, Context ctx) throws Exception {
		
		if (!initalized) {
			init(ctx);
		}
		
		logger.info("Authorization payment for order " + 
			ctx.getCurrentRecord().getProperties().get("order-id"));
		
		AuthorizedPayment result = authorizer.authorize(card);
		
		ctx.newOutputMessage(ctx.getOutputTopic(), AvroSchema.of(AuthorizedPayment.class))
			.properties(ctx.getCurrentRecord().getProperties())
			.value(result)
			.send();
		
		return null;
	}
	
	private void init(Context ctx) throws Exception {
		logger = ctx.getLogger();
		
		String authorizeClassName = ctx.getUserConfigValueOrDefault(
			"authorizer-classname", DEFAULT_STRATEGY).toString();
			
		authorizer = (AuthorizationStrategy) Class.forName(authorizeClassName)
			.getDeclaredConstructor().newInstance();
			
		authorizer.initalize(ctx);
		
		initalized = true;
	}

}
