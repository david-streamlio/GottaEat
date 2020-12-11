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

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.gottaeat.domain.order.FoodOrder;

public class CustomerFeaturesLookup implements Function<FoodOrder, CustomerFeatures> {

	public static final String HOSTNAME_KEY = "db-hostname";
	public static final String PORT_KEY = "db-port";
	
	private String hostName;
	private int port = -1;
	private CqlSession session;
	private InetAddress node;
	private InetSocketAddress address;
	private SimpleStatement queryStatement;
	
	@Override
	public CustomerFeatures process(FoodOrder input, Context ctx) throws Exception {
		if (!initalized()) {
			hostName = ctx.getUserConfigValueOrDefault(HOSTNAME_KEY, "127.0.0.1").toString();
			port = (int) ctx.getUserConfigValueOrDefault(PORT_KEY, 9042);
			queryStatement = SimpleStatement.newInstance("select * from customer where customerid = ?");
		}
		
		return getCustomerFeatures(input.getMeta().getCustomerId());
	}

	private boolean initalized() {
		return StringUtils.isNotBlank(hostName) && (port > 0) && (queryStatement != null);
	}
	
	private CustomerFeatures getCustomerFeatures(Long customerId) {
		ResultSet rs = executeStatement(customerId);
		
		Row row = rs.one(); // There is only one row per customerID
		if (row != null) {
			return CustomerFeatures.newBuilder()
					.setCustomerId(customerId)
					.setAvgOrderPrice(row.getDouble(CqlIdentifier.fromCql("avg_order_spend")))
					.setPercentageOfHomeDeliveries(row.getDouble(CqlIdentifier.fromCql("percent_orders_to_home_addr")))
					.build();
		}
		return null;
	}
	
	private ResultSet executeStatement(Long customerId) {
		PreparedStatement pStmt = getSession().prepare(queryStatement);
        return getSession().execute(pStmt.bind(customerId));
    }
	
	private CqlSession getSession() {
		if (session == null || session.isClosed()) {
			CqlSessionBuilder builder = 
				CqlSession.builder()
				  .addContactPoint(getAddress())
				  .withLocalDatacenter("datacenter1")
				  .withKeyspace(CqlIdentifier.fromCql("featurestore"));
			
			session = builder.build();
		}
		return session;
	}
	
	private InetSocketAddress getAddress() {
		if (address == null) {
			address = new InetSocketAddress(node, port);
		}
		return address;
	}
}
