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
package com.gottaeat.dao.user;

import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.user.UserDetails;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;

public class UserDetailsLookup implements Function<Long, UserDetails> {
	
	public static final String DB_DRIVER_KEY = "dbDriverClass";
	public static final String DB_PASS_KEY = "dbPass";
	public static final String DB_URL_KEY = "dbUrl";
	public static final String DB_USER_KEY = "dbUser";
			
	private Connection con;
	private PreparedStatement stmt;
	
	private String dbUrl;
	private String dbUser;
	private String dbPass;
	private String dbDriverClass;

	@Override
	public UserDetails process(Long customerId, Context ctx) throws Exception {
		
		if (!isInitalized()) {
		  dbUrl = (String) ctx.getUserConfigValue(DB_URL_KEY).orElse(null);
		  dbDriverClass = (String) ctx.getUserConfigValue(DB_DRIVER_KEY).orElse(null);
		  dbUser = (String) ctx.getSecret(DB_USER_KEY);
		  dbPass = (String) ctx.getSecret(DB_PASS_KEY);
		}
		
		UserDetails details = null;
		ResultSet rs = getCustomerDetails(customerId);
		
		if (rs != null && rs.next()) {
			Address addr = Address.newBuilder()
					.setStreet(rs.getString("a.address"))
					.setCity(rs.getString("c2.city"))
					.setCountry(rs.getString("c3.country"))
					.setZip(rs.getString("a.postal_code"))
					.setState(rs.getString("a.district"))
					.build();
			
			details = UserDetails.newBuilder()
					   .setEmail(rs.getString("ru.email"))
					   .setFirstName(rs.getString("ru.first_name"))
					   .setLastName(rs.getString("ru.last_name"))
					   .setPhoneNumber(rs.getString("a.phone"))
					   .setUserId(rs.getInt("ru.user_id"))
					   .build();
		}
		rs.close();
		return details;
	}
	
	private ResultSet getCustomerDetails(long customerId) throws SQLException, ClassNotFoundException {
		PreparedStatement ps = getSql();
		ps.setLong(1, customerId);		
		return	ps.executeQuery();
	}
	
	private PreparedStatement getSql() throws ClassNotFoundException, SQLException {
		if (stmt == null) {
			  stmt = getDbConnection().prepareStatement("select ru.user_id, ru.first_name, ru.last_name, ru.email, "
						+ "a.address, a.postal_code, a.phone, a.district,"
						+ "c.city, c2.country from GottaEat.RegisteredUser ru "
						+ "join GottaEat.Address a on a.address_id = c.address_id "
						+ "join GottaEat.City c on a.city_id = c.city_id "
						+ "join GottaEat.Country c2 on c.country_id = c2.country_id "
						+ "where ru.user_id = ?");
		}
		return stmt;
	}
	
	private Connection getDbConnection() throws SQLException, ClassNotFoundException {
	  if (con == null) {
		  Class.forName(dbDriverClass);
		  con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
	  }
	  return con;
	}
	
	private boolean isInitalized() {
		return StringUtils.isNotBlank(dbUrl) && StringUtils.isNotBlank(dbUser) 
				&& StringUtils.isNotBlank(dbPass) && StringUtils.isNotBlank(dbDriverClass);
	}

}
