package com.gottaeat.services.fraud.scoring.fraudlabs;

import java.util.Hashtable;

import com.fraudlabspro.FraudLabsPro;
import com.fraudlabspro.Order;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.gottaeat.domain.fraud.scoring.fraudlabs.Transaction;

/**
 * 
 * @see https://www.fraudlabspro.com/developer/api/screen-order
 *
 */
public class FraudScoringService implements Function<Transaction, Integer> {
	
	private JSONParser parser = new JSONParser();
	private String apiKey;

	@Override
	public Integer process(Transaction input, Context ctx) throws Exception {
		
		if (!isInitalized()) {
			apiKey = (String) ctx.getUserConfigValue("apiKey").orElse(null);
		}
		
		// Configures FraudLabs Pro API key
        FraudLabsPro.APIKEY = apiKey;

        // Screen Order API
        Order order = new Order();

        // Sets order details
        Hashtable<String, String> data = new Hashtable<>();
        
        data.put("ip", null); // Get this from IDMG ?
        data.put("format", "json");
        
        data.put("first_name", input.getFirstName().toString());
        data.put("last_name", input.getLastName().toString());
        data.put("email", input.getEmail().toString());
        data.put("user_phone", input.getPhoneNumber().toString());

        // Billing information
        data.put("bill_addr", input.getBillingAddress().getAddress().toString());
        data.put("bill_city", input.getBillingAddress().getCity().toString());
        data.put("bill_state", input.getBillingAddress().getState().toString());
        data.put("bill_country", input.getBillingAddress().getCountry().toString());
        data.put("bill_zip_code", input.getBillingAddress().getPostalCode().toString());
        // data.put("number", ""); // Credit card number ?

        // Order information
        data.put("amount", Float.toString(input.getAmount()));
        data.put("currency", "USD");
        data.put("payment_mode", order.CREDIT_CARD);  // Please refer reference section for full list of payment methods

        // Shipping information
        data.put("ship_addr", input.getShippingAddress().getAddress().toString());
        data.put("ship_city", input.getShippingAddress().getCity().toString());
        data.put("ship_state", input.getShippingAddress().getState().toString());
        data.put("ship_zip_code", input.getShippingAddress().getPostalCode().toString());
        data.put("ship_country", input.getShippingAddress().getCountry().toString());
        
         // Sends order details to FraudLabs Pro
        JSONObject response = (JSONObject)parser.parse(order.validate(data));
        
        /* Overall score between 1 and 100. 100 is the highest risk and 1 is the lowest risk. */
        return (int) response.get("fraudlabspro_score");

	}

	private boolean isInitalized() {
		return StringUtils.isNotBlank(apiKey);
	}

}
