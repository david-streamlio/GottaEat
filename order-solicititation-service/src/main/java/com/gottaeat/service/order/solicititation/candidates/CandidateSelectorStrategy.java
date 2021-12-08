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
package com.gottaeat.service.order.solicititation.candidates;

import java.util.List;

import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;

public interface CandidateSelectorStrategy {

	/**
	 * Determines which restaurants are "best suited" to fulfill the order
	 * based on the menu items, proximity to the delivery address, etc.
	 * 
	 * @param order
	 * @param deliveryAddr
	 * @return A list of Pulsar topic names. Each topic name is own exclusively 
	 * by the restaurant and is used for communicating directly with them.
	 */
	public List<String> getCandidates(FoodOrder order, Address deliveryAddr);
	
}
