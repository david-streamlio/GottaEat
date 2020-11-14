package com.gottaeat.dao.order;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class OrderHistoryLookup implements Function<String, String> {

	@Override
	public String process(String input, Context context) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
