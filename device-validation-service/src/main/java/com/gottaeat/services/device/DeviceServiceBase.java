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
package com.gottaeat.services.device;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.functions.api.Context;

public abstract class DeviceServiceBase {

	public static final String DB_DRIVER_KEY = "dbDriverClass";
	public static final String DB_PASS_KEY = "dbPass";
	public static final String DB_URL_KEY = "dbUrl";
	public static final String DB_USER_KEY = "dbUser";
	
	protected Connection con;
	protected PreparedStatement stmt;
	protected String dbUrl;
	protected String dbUser;
	protected String dbPass;
	protected String dbDriverClass;
	
	protected Connection getDbConnection() throws SQLException, ClassNotFoundException {
		if (con == null) {
			Class.forName(dbDriverClass);
			con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
		}
		return con;
	}

	protected boolean isInitalized() {
		return StringUtils.isNotBlank(dbUrl) && StringUtils.isNotBlank(dbUser) 
				&& StringUtils.isNotBlank(dbPass) && StringUtils.isNotBlank(dbDriverClass);
	}
	
	protected void initalize(Context ctx) {
		dbUrl = (String) ctx.getUserConfigValue(DB_URL_KEY).orElse(null);
		dbDriverClass = (String) ctx.getUserConfigValue(DB_DRIVER_KEY).orElse(null);
		dbUser = (String) ctx.getSecret(DB_USER_KEY);
		dbPass = (String) ctx.getSecret(DB_PASS_KEY);
	}

}
