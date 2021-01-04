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
package com.gottaeat.services.device.registration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.user.ActiveUser;
import com.gottaeat.domain.user.DeviceInfo;
import com.gottaeat.domain.user.User;
import com.gottaeat.security.authentication.sms.verification.SMSVerificationResponse;
import com.gottaeat.services.device.DeviceServiceBase;

/**
 * 
 * Once a mobile device has been verified using SMS 2FA, then the response is sent here
 * so we can add the new device to the database. Once we have updated the database, we
 * then construct an ActiveUser object and forward it to the DeviceValidationService in order
 * for the StateStore to be updated.
 * 
 * ASSUMPTION: The output topic for this service will be the input topic of the DeviceValidationService
 */
public class DeviceRegistrationService extends DeviceServiceBase implements Function<SMSVerificationResponse, ActiveUser> {
	
	@Override
	public ActiveUser process(SMSVerificationResponse input, Context ctx) throws Exception {
		
		if (!isInitalized()) {
			initalize(ctx);
		}
		
		registerDevice(input);
		return ActiveUser.newBuilder()
				.setDevice(DeviceInfo.newBuilder()
						.setDeviceId(input.getDeviceId())
						.build())
				.setUser(User.newBuilder().build())
				.build();
	}
	
	private void registerDevice(SMSVerificationResponse res) throws Exception {
		PreparedStatement ps = getInsertStmt();
		ps.setLong(1, res.getRegisteredUserId());
		ps.setNString(2, res.getDeviceId().toString());
		ps.executeUpdate();
	}
	
	private PreparedStatement getInsertStmt() throws ClassNotFoundException, SQLException {
		if (stmt == null) {
		  stmt = getDbConnection().prepareStatement("INSERT INTO RegisteredDevice VALUES (?, ?);");
		}
		return stmt;
	}

}
