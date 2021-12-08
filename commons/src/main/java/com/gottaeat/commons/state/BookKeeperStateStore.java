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
package com.gottaeat.commons.state;

import java.nio.ByteBuffer;

import org.apache.pulsar.functions.api.Context;

public class BookKeeperStateStore implements StateStore {
	
	private Context ctx;
	
	public BookKeeperStateStore(Context c) {
		this.ctx = c;
	}

	@Override
	public void deleteState(String key) {
		this.ctx.deleteState(key);
	}

	@Override
	public ByteBuffer getState(String key) {
		return this.ctx.getState(key);
	}

	@Override
	public void putState(String key, ByteBuffer value) {
		this.ctx.putState(key, value);
	}

}
