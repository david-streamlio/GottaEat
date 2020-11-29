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
package com.gottaeat.services.validation;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;

import com.gottaeat.domain.user.ActiveUser;

public class DeviceValidationService implements Function<ActiveUser, ActiveUser> {

	public static final String DB_DRIVER_KEY = "dbDriverClass";
	public static final String DB_PASS_KEY = "dbPass";
	public static final String DB_URL_KEY = "dbUrl";
	public static final String DB_USER_KEY = "dbUser";
	public static final String REGISTRATION_TOPIC_KEY = "registrationTopic";
	private static final String EMPTY_STRING = "";
	
	private Connection con;
	private PreparedStatement stmt;
	private String dbUrl;
	private String dbUser;
	private String dbPass;
	private String dbDriverClass;
	private String deviceRegistrationTopic;
	
	/**
	 * As an extra level of security, we want to validate that the user is connecting with a "known" device, 
	 * that they have previously registered with our application. Therefore we will compare the device id 
	 * provided by the message with the most recently used device for this user, and if they match we will 
	 * continue. Otherwise, we will look in the database for the device id and if we still can't find it then
	 * we will send a notification to the user asking them to register the new device.
	 */
	@Override
	public ActiveUser process(ActiveUser input, Context ctx) throws Exception {
		
		boolean mustRegister = false;
		
		if (!isInitalized()) {
			deviceRegistrationTopic = (String) ctx.getUserConfigValue(REGISTRATION_TOPIC_KEY).orElse(null);
			dbUrl = (String) ctx.getUserConfigValue(DB_URL_KEY).orElse(null);
			dbDriverClass = (String) ctx.getUserConfigValue(DB_DRIVER_KEY).orElse(null);
			dbUser = (String) ctx.getSecret(DB_USER_KEY);
			dbPass = (String) ctx.getSecret(DB_PASS_KEY);
		}
		
		if (StringUtils.isBlank(input.getDevice().getDeviceId())) {
			// No deviceID was provided by the user, so we need to register it.
			mustRegister = true;
		} 
		
		String previousDeviceId = getPreviousDeviceId(input.getLocation().getRegisteredUserId(), ctx);
		
		if (StringUtils.equals(previousDeviceId, EMPTY_STRING)) {
			// There is no previous device registered for this user
		    mustRegister = true;
		} else if (StringUtils.equals(previousDeviceId, input.getDevice().getDeviceId())) {
			return input;
		} else if (isRegisteredDevice(input.getLocation().getRegisteredUserId(), 
				input.getDevice().getDeviceId().toString())) {
			// Update the previous device value for this user
			ByteBuffer value = ByteBuffer.allocate(input.getDevice().getDeviceId().length());
			value.put(input.getDevice().getDeviceId().toString().getBytes());
			ctx.putState("UserDeviceTable-" + input.getDevice().getDeviceId(), value);
		}
		
		if (mustRegister) {
			ctx.newOutputMessage(deviceRegistrationTopic, Schema.AVRO(ActiveUser.class))
			   .value(input)
			   .sendAsync();
			
			return null;
		} else {
			return input;
		}
	}

	private String getPreviousDeviceId(int registeredUserId, Context ctx) {
		ByteBuffer buf = ctx.getState("UserDeviceTable-" + registeredUserId);
		return (buf != null) ? new String(buf.asReadOnlyBuffer().array()) : EMPTY_STRING;
	}
	
	private boolean isRegisteredDevice(int userId, String deviceId) {
		try {
			PreparedStatement ps = getSql();
			ps.setInt(1, userId);
			ps.setNString(2, deviceId);
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.next()) {
			  return (rs.getInt(1) > 0);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// Ignore these
		}
		return false;
	}
	
	private PreparedStatement getSql() throws ClassNotFoundException, SQLException {
		if (stmt == null) {
		  stmt = getDbConnection().prepareStatement("select count(*) from RegisteredDevice where user_id = ? AND device_id = ?");
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
				&& StringUtils.isNotBlank(dbPass) && StringUtils.isNotBlank(dbDriverClass)
				&& StringUtils.isNotBlank(deviceRegistrationTopic);
	}

}
