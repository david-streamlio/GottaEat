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
		
		return getUserDetails(customerId);
	}
	
	private UserDetails getUserDetails(Long customerId) {
		UserDetails details = null;
		
		try (Connection con = getDbConnection();
			 PreparedStatement ps = con.prepareStatement("select ru.user_id, ru.first_name,"
			 		+ " ru.last_name, ru.email, "
					+ "a.address, a.postal_code, a.phone, a.district,"
					+ "c.city, c2.country from GottaEat.RegisteredUser ru "
					+ "join GottaEat.Address a on a.address_id = c.address_id "
					+ "join GottaEat.City c on a.city_id = c.city_id "
					+ "join GottaEat.Country c2 on c.country_id = c2.country_id "
					+ "where ru.user_id = ?");) {
			
			ps.setLong(1, customerId);	
			try (ResultSet rs = ps.executeQuery()) {
				if (rs != null && rs.next()) {
					details = UserDetails.newBuilder()
							   .setEmail(rs.getString("ru.email"))
							   .setFirstName(rs.getString("ru.first_name"))
							   .setLastName(rs.getString("ru.last_name"))
							   .setPhoneNumber(rs.getString("a.phone"))
							   .setUserId(rs.getInt("ru.user_id"))
							   .build();
				}
			} catch (Exception ex) {
				// Ignore
			}
			
		} catch (Exception ex) {
			// Ignore
		}
		
		return details;
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
