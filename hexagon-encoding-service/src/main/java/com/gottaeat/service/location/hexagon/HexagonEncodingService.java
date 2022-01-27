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
package com.gottaeat.service.location.hexagon;

import java.io.IOException;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.google.common.annotations.VisibleForTesting;
import com.gottaeat.domain.geography.GeoEncodedAddress;
import com.gottaeat.domain.geography.H3EncodedAddress;
import com.uber.h3core.H3Core;

public class HexagonEncodingService implements Function<GeoEncodedAddress, H3EncodedAddress> {

	private H3Core h3;
	
	@Override
	public H3EncodedAddress process(GeoEncodedAddress input, Context ctx) throws Exception {
		
		if (input == null || input.getLatLong() == null) {
		  return null; 
		}
		
		H3EncodedAddress hexAddr = null;
		
		try {
			
			hexAddr = H3EncodedAddress.newBuilder()
				.setGeo(input)
				.setH3Address(getH3().geoToH3Address(
					input.getLatLong().getLatitude(), 
					input.getLatLong().getLongitude(), 9))
				.build();
			
		} catch (final Exception ex) {
			ctx.getCurrentRecord().fail();  // retry the message
		}
		
		return hexAddr;	
	}
	
	@VisibleForTesting
	public void setH3(H3Core h3) {
		this.h3 = h3;
	}
	
	private H3Core getH3() throws IOException {
		if (h3 == null) {
		  h3 = H3Core.newInstance();	
		}
		
		return h3;
	}

}
