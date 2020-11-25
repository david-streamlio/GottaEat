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
package com.gottaeat.services.location.tracking;

import com.gottaeat.domain.user.UserLocation;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

/**
 * 
 * By default, a location update is sent every 30 seconds from the mobile application,
 * this function listens for these updates from all of users that are currently logged 
 * in to the mobile application and updates the in memory data grid with this information.
 * 
 * It also passes the information on to another topic so that it can retained in a separate
 * database for compliance and data model training (e.g. travel time estimation)
 *
 */
public class LocationTrackingService implements Function<UserLocation, UserLocation> {

	@Override
	public UserLocation process(UserLocation input, Context ctx) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
