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
package com.gottaeat.simulators.driver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.AvroSchema;

import com.gottaeat.domain.geography.LatLon;

public class TopicPublisher implements PropertyChangeListener {

	private Producer<LatLon> prod;
	
	public TopicPublisher(String topicName) {
		try {
			
			prod = PulsarClient.builder()
				.serviceUrl("pulsar://localhost:6650")
				.build()
				.newProducer(AvroSchema.of(LatLon.class))
				  .topic(topicName)
				  .create();
			
		} catch (PulsarClientException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("Changed " + evt.getPropertyName()  + " to " + evt.getNewValue());
		
		try {
			
			prod.newMessage()
			  .value((LatLon) evt.getNewValue())
			  .send();
			
		} catch (PulsarClientException e) {
			e.printStackTrace();
		}
	}

}
