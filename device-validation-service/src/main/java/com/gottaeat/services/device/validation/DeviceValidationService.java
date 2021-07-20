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
package com.gottaeat.services.device.validation;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;

import com.gottaeat.domain.user.ActiveUser;
import com.gottaeat.services.device.DeviceServiceBase;

public class DeviceValidationService extends DeviceServiceBase implements Function<ActiveUser, ActiveUser> {

	public static final String REGISTRATION_TOPIC_KEY = "registrationTopic";
	private static final String EMPTY_STRING = "";
	
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
			initalize(ctx);
			deviceRegistrationTopic = (String) ctx.getUserConfigValue(REGISTRATION_TOPIC_KEY).orElse(null);
		}
		
		if (StringUtils.isBlank(input.getDevice().getDeviceId())) {
			// No deviceID was provided by the user, so we need to register it.
			mustRegister = true;
		} 
		
		String previousDeviceId = getPreviousDeviceId(input.getUser().getRegisteredUserId(), ctx);
		
		if (StringUtils.equals(previousDeviceId, EMPTY_STRING)) {
			// There is no previous device registered for this user
		    mustRegister = true;
		} else if (StringUtils.equals(previousDeviceId, input.getDevice().getDeviceId())) {
			return input;
		} else if (isRegisteredDevice(input.getUser().getRegisteredUserId(), 
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

	private String getPreviousDeviceId(long registeredUserId, Context ctx) {
		ByteBuffer buf = ctx.getState("UserDeviceTable-" + registeredUserId);
		return (buf != null) ? new String(buf.asReadOnlyBuffer().array()) : EMPTY_STRING;
	}
	
	private boolean isRegisteredDevice(long userId, String deviceId) {
		try ( Connection con = getDbConnection();
			  PreparedStatement ps = con.prepareStatement( "select count(*) "
			  		+ "from RegisteredDevice where user_id = ? AND device_id = ?")) {

			ps.setLong(1, userId); 
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
	
	protected boolean isInitalized() {
		return super.isInitalized() && StringUtils.isNotBlank(deviceRegistrationTopic);
	}

}
