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

/**
 * 
 * This class simulates the movement of a driver by
 * taking a route file and periodically publishing the
 * location information to a Pulsar topic.
 * 
 * @see https://www.openstreetmap.org/traces for GPS trace examples
 *
 */
public class DriverRouteSimulator {
	
	private TopicPublisher publisher;
	private TrekSimulator sim;
	
	protected DriverRouteSimulator(String topicName, String routeFile) {
		this.publisher = new TopicPublisher(topicName);
		sim = new TrekSimulator(routeFile);
	}
	
	protected void start() throws Exception {
		
		// Register the publisher with the simulator.
		sim.getPropertyChangeSupport()
		   .addPropertyChangeListener("currentLocation", publisher);
		
		sim.start();
	}

	public static void main(String[] args) throws Exception {
		DriverRouteSimulator sim = new DriverRouteSimulator(args[0], args[1]);
		sim.start();
	}

}
