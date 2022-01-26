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
package com.gottaeat.services.h3.tracking;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.commons.lang3.StringUtils;

import com.gottaeat.domain.driver.DriverH3Location;
import com.gottaeat.domain.geography.LatLon;

public class GridTrackingService implements Function<DriverH3Location, Void> {

    static final String DATAGRID_KEY = "datagridUrl";
    
	private IgniteClient client;
	private String datagridUrl;
	
	@Override
	public Void process(DriverH3Location input, Context ctx) throws Exception {
		if (!initalized()) {
			datagridUrl = ctx.getUserConfigValue(DATAGRID_KEY).orElse("localhost:10800").toString();
		}
		getCache(String.valueOf(input.getLocation().getH3Address())).put(
			input.getDriverId(), 
			input.getLocation().getGeo().getLatLong());
		return null;
	}
	
	// Each cache is <driverId, LatLon>
	private ClientCache<Long, LatLon> getCache(String cellID) {
		return getClient().getOrCreateCache("drivers-cell-" + cellID);
	}
	
	private IgniteClient getClient() {
		if (client == null) {
			ClientConfiguration cfg = new ClientConfiguration().setAddresses(datagridUrl);
			client = Ignition.startClient(cfg);
		}
		return client;
	}
	
	private boolean initalized() {
		return StringUtils.isNotBlank(datagridUrl);
	}

}
