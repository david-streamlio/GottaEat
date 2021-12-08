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
package com.gottaeat.services.ordervalidation;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.pulsar.common.functions.ConsumerConfig;
import org.apache.pulsar.common.functions.FunctionConfig;
import org.apache.pulsar.functions.LocalRunner;

public class OrderValidationServiceLocalRunner {

	public static void main(String[] args) throws Exception {
		
		Map<String, ConsumerConfig> inputSpecs = new HashMap<String, ConsumerConfig> ();
	    inputSpecs.put("persistent://orders/inbound/food-orders", 
	    		ConsumerConfig.builder().schemaType("avro").build());
		
	    FunctionConfig functionConfig = 
	    	FunctionConfig.builder()
	    	.className(OrderValidationService.class.getName())
	    	.inputs(Collections.singleton("persistent://orders/inbound/food-orders"))
	    	.inputSpecs(inputSpecs)
	    	.name("order-validation")
	    	.output("persistent://orders/inbound/valid-food-orders")
	    	.outputSchemaType("avro")
	    	.runtime(FunctionConfig.Runtime.JAVA)
	    	.build();
	    
	    // Assumes you started docker container with --volume=${HOME}/exchange:/pulsar/manning/dropbox 
	    String credentials_path = System.getProperty("user.home") + File.separator 
	    		+ "exchange" + File.separator;

	    LocalRunner localRunner = 
	    	LocalRunner.builder()
	    		.brokerServiceUrl("pulsar+ssl://localhost:6651")
	    		.clientAuthPlugin("org.apache.pulsar.client.impl.auth.AuthenticationTls")
	    		.clientAuthParams("tlsCertFile:" + credentials_path + "admin.cert.pem,tlsKeyFile:"
	    				+ credentials_path + "admin-pk8.pem")
	    		.tlsTrustCertFilePath(credentials_path + "ca.cert.pem")
	    		.functionConfig(functionConfig)
	    		.build();
	    
	    localRunner.start(false);
	    Thread.sleep(30 * 1000);
	    localRunner.stop();
	    System.exit(0);
	}
}
