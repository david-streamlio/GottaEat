package com.gottaeat.services.order.fraud;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

/**
 * Takes in Fraud Score object which represents the probability that an order is fraudulent,
 * and determines whether it crosses the threshold we set for "fraud"
 *
 */
public class FraudDetectionService implements Function<String, String> {

	@Override
	public String process(String input, Context context) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
