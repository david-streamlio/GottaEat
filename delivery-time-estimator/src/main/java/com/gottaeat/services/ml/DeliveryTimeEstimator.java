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
package com.gottaeat.services.ml;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.commons.lang3.StringUtils;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.regression.RegressionModel;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.regression.RegressionModelEvaluator;
import org.jpmml.model.PMMLUtil;

import com.gottaeat.domain.geography.LatLon;
import com.gottaeat.domain.order.FoodOrderML;

public class DeliveryTimeEstimator implements Function<FoodOrderML, FoodOrderML> {

	private static final String MODEL_KEY = "DTE-Model";
	
	private IgniteClient client;
	private ClientCache<Long, LatLon> cache;
	private String datagridUrl;
	private String locationCacheName;
	
	private byte[] mlModel = null;
	private ModelEvaluator<RegressionModel> evaluator; //
	
	@Override
	public FoodOrderML process(FoodOrderML order, Context ctx) throws Exception {
		
		if (initalized()) {
		  mlModel = ctx.getState(MODEL_KEY).array(); //
		  evaluator = new RegressionModelEvaluator(
			PMMLUtil.unmarshal(new ByteArrayInputStream(mlModel)));  //
		}
		
		HashMap<FieldName, Double> featureVector = new HashMap<>();
		
		featureVector.put(FieldName.create("avg_prep_last_hour"), 
		  order.getRestaurantFeatures().getAvgMealPrepLastHour());
		
		featureVector.put(FieldName.create("avg_prep_last_7days"), 
		  order.getRestaurantFeatures().getAvgMealPrepLast7Days()); //
		
		featureVector.put(FieldName.create("driver_lat"),
		  getCache().get(order.getAssignedDriverId()).getLatitude());
		
		featureVector.put(FieldName.create("driver_long"),
		  getCache().get(order.getAssignedDriverId()).getLongitude());  //
		
		Long travel = (Long)evaluator.evaluate(featureVector)
				.get(FieldName.create("travel_time"));  //
		
		order.setEstimatedArrival(System.currentTimeMillis() + travel);  //
		return order;
	}
	
	private final boolean initalized() {
		return (mlModel != null) && StringUtils.isNotBlank(datagridUrl) && 
		  StringUtils.isNotBlank(locationCacheName);
	}
	
	private ClientCache<Long, LatLon> getCache() {
		if (cache == null) {
			cache = getClient().getOrCreateCache(locationCacheName);
		}
		return cache;
	}
	
	private IgniteClient getClient() {
		if (client == null) {
			ClientConfiguration cfg = new ClientConfiguration().setAddresses(datagridUrl);
			client = Ignition.startClient(cfg);
		}
		return client;
	}

}
