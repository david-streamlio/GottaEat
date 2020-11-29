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
package com.gottaeat.services.fraud.scoring.ipqualityscore;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FraudScore {

	private boolean success;
	
	private String message;
	
	private int fraud_score;
	
	private String country_code;
	
	private String region;
	
	private String city;
	
	private String isp;
	
	private int asn;
	
	private String organization;
	
	private float latitude;
	
	private float longitude;
	
	private boolean is_crawler;
	
	private String timezone;
	
	private boolean mobile;
	
	private String host;
	
	private boolean proxy;
	
	private boolean vpn;
	
	private boolean tor;
	
	private boolean active_vpn;
	
	private boolean active_tor;
	
	private boolean recent_abuse;
	
	private boolean bot_status;
	
	private TransactionDetails transaction_details;
	
	private String request_id;
}