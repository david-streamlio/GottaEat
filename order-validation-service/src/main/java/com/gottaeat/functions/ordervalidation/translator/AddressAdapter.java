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
package com.gottaeat.functions.ordervalidation.translator;

import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.ValidatedFoodOrder;

public class AddressAdapter implements Function<Address, Void> {

	@Override
	public Void process(Address addr, Context ctx) throws Exception {
		ValidatedFoodOrder result = new ValidatedFoodOrder();
		result.setDeliveryLocation(addr);
		
		ctx.newOutputMessage(ctx.getOutputTopic(), AvroSchema.of(ValidatedFoodOrder.class))
		.properties(ctx.getCurrentRecord().getProperties())
		.value(result)
		.send();
		
		return null;
	}

}
