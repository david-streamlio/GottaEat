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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDetails {
	
	private boolean valid_billing_address;
	
	private boolean valid_shipping_address;
	
	private boolean valid_billing_email;
	
	private boolean valid_shipping_email;
	
	private boolean risky_billing_phone;
	
	private boolean risky_shipping_phone;
	
	private String billing_phone_country;
	
	private String billing_phone_country_code;
	
	private String shipping_phone_country;
	
	private String shipping_phone_country_code;
	
	private String billing_phone_carrier;
	
	private String shipping_phone_carrier;
	
	private String billing_phone_line_type;
	
	private String shipping_phone_line_type;
	
	private boolean fraudulent_behavior;
	
	private String bin_country;
	
	private boolean risky_username;
	
	private boolean valid_billing_phone;
	
	private boolean valid_shipping_phone;
	
	private boolean leaked_billing_email;
	
	private boolean leaked_shipping_email;
	
	private boolean leaked_user_data;
	
	private boolean is_prepaid_card;
	
	private int risk_score;
}