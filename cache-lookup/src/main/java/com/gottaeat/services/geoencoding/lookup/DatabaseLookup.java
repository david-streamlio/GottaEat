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
package com.gottaeat.services.geoencoding.lookup;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.order.FoodOrder;

import io.github.resilience4j.decorators.Decorators;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

public class DatabaseLookup implements Function<String, FoodOrder> {
	
	private String sql = "select * from food_orders where id=?";
	private String primary = "jdbc:mysql://load-balancer:3306/food";
	private String backup = "jdbc:mysql://backup:3306/food";
	private String user = "";
	private String pass = "";
	private boolean initalized = false;

	@Override
	public FoodOrder process(String id, Context ctx) throws Exception {
		if (!initalized) {
			init(ctx);
		}
		
		CheckedFunction0<ResultSet> decoratedFunction = 
			Decorators.ofCheckedSupplier( () -> {
				try (Connection con = DriverManager.getConnection(primary, user, pass)) {
					PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setLong(1, Long.parseLong(id));
					return stmt.executeQuery();
				}
			})
			
			.withFallback(SQLException.class, ex -> {
				try (Connection con = DriverManager.getConnection(backup, user, pass)) {
					PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setLong(1, Long.parseLong(id));
					return stmt.executeQuery();
				}
			})
			.decorate();
		
		ResultSet rs = Try.of(decoratedFunction).get();
		return ORMapping(rs);
	}


	private void init(Context ctx) {
		Driver myDriver;
		try {
			myDriver = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
			DriverManager.registerDriver(myDriver);
			initalized = true;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private FoodOrder ORMapping(ResultSet rs) {
		return new FoodOrder();
	}
	
}
