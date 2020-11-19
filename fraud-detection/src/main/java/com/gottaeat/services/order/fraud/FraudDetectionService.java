package com.gottaeat.services.order.fraud;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.fraud.fraudlabs.FraudScoringResult;
import com.gottaeat.domain.order.FoodOrder;

/**
 * Takes in Fraud Score object which represents the probability that an order is fraudulent,
 * and determines whether it crosses the threshold we set for "fraud"
 *
 */
public class FraudDetectionService implements Function<FraudScoringResult, FoodOrder> {

	@Override
	public FoodOrder process(FraudScoringResult input, Context context) throws Exception {
		// TODO route "fraud" orders to a different topic...
		return null;
	}

}
