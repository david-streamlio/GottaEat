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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Record;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gottaeat.domain.geography.*;
import com.uber.h3core.H3Core;

import io.jsonwebtoken.io.IOException;


public class HexagonEncodingServiceTest {
	
	private static final GeoEncodedAddress GOOD_ADDRESS, BAD_ADDRESS;
	
	static {
		GOOD_ADDRESS = GeoEncodedAddress
				.newBuilder()
				.setAddress(Address.newBuilder()
						.setCity("Chicago")
						.setCountry("USA")
						.setState("IL")
						.setStreet("1600 Jefferson Ave")
						.setZip("66101").build())
				.setLatLong(LatLon.newBuilder()
						.setLatitude(37.775938728915946)
						.setLongitude(-122.41795063018799)
						.build())
				.build();
		
		BAD_ADDRESS = GeoEncodedAddress
				.newBuilder()
				.setAddress(Address.newBuilder()
						.setCity("Chicago")
						.setCountry("USA")
						.setState("IL")
						.setStreet("1600 Jefferson Ave")
						.setZip("66101").build())
				.setLatLong(LatLon.newBuilder()
						.setLatitude(437.775938728915946)
						.setLongitude(-222.41795063018799)
						.build())
				.build();
	}
	
	@Mock
	private Context mockContext;
	
	@SuppressWarnings("rawtypes")
	@Mock 
	private Record mockRecord;
	
	@Mock
	private H3Core faultyH3;

	private HexagonEncodingService service;

	
	@SuppressWarnings("unchecked")
	@Before
	public final void init() {
		MockitoAnnotations.initMocks(this);
		
		when(mockContext.getCurrentRecord()).thenReturn(mockRecord);
		when(faultyH3.geoToH3Address(anyDouble(), anyDouble(), anyInt()))
		  .thenThrow(IOException.class);
		
		service = new HexagonEncodingService();
	}
	
	@Test
	public final void nullAddressTest() throws Exception {
        H3EncodedAddress result = service.process(null, mockContext);
		assertNull(result);
	}
	
	@Test
	public final void noGeoTest() throws Exception {
		GeoEncodedAddress addr = GeoEncodedAddress
				.newBuilder()
				.setAddress(Address.newBuilder()
						.setCity("Chicago")
						.setCountry("USA")
						.setState("IL")
						.setStreet("1600 Jefferson Ave")
						.setZip("66101").build())
				.build();
		
		H3EncodedAddress result = service.process(addr, mockContext);
		assertNull(result);
	}
	
	@Test
	public final void h3FailureTest() throws Exception {
	  service.setH3(faultyH3);
	  
	  H3EncodedAddress result = service.process(GOOD_ADDRESS, mockContext);
	  assertNull(result);
	  verify(mockRecord, times(1)).fail();
	}
	
	@Test
	public final void validAddressTest() throws Exception {
		
		H3EncodedAddress result = service.process(GOOD_ADDRESS, mockContext);
		
		assertNotNull(result);
		assertEquals("8928308280fffff", result.getH3Address());
	}
	
	@Test
	public final void invalidAddressTest() throws Exception {
		
		H3EncodedAddress result = service.process(BAD_ADDRESS, mockContext);
		
		assertNotNull(result);
		assertEquals("8904208c34bffff", result.getH3Address());
	}
}
