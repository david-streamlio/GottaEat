package com.gottaeat.services.fraud.scoring.ipqualityscore;

import java.nio.charset.StandardCharsets;
import java.net.Inet6Address;
import java.net.UnknownHostException;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.JSONParser;

import com.google.common.hash.Hashing;
import com.gottaeat.domain.fraud.fraudlabs.FraudScoringResult;
import com.gottaeat.domain.fraud.fraudlabs.OrderScoringData;
import com.gottaeat.domain.payment.CreditCard;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FraudScoringService implements Function<OrderScoringData, FraudScoringResult> {

	// https://ipqualityscore.com/api/json/ip/7T7l9zFs5ishiWsr7xJtFANB1iG2aXdv/2600:8801:1f00:e13:cd4a:31eb:ecaa:34d?strictness=1&billing_email=myemail@example.com&billing_phone=5555555555&billing_country=US
	
	
	private static final String BASE_URL = "https://ipqualityscore.com";
	private JSONParser parser = new JSONParser();
	
	private String url;
	
	@Override
	public FraudScoringResult process(OrderScoringData input, Context ctx) throws Exception {
		if (!isInitalized()) {
			apiKey = (String) ctx.getUserConfigValue("apiKey").orElse(null);
		}

		HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
		httpBuilder.addQueryParameter("billing_first_name", input.getCustomer().getFirstName().toString());
		httpBuilder.addQueryParameter("billing_last_name", input.getCustomer().getLastName().toString());
		httpBuilder.addQueryParameter("billing_country", input.getCustomer().getBillingAddress().getCountry().toString());
		httpBuilder.addQueryParameter("billing_address_1", input.getCustomer().getBillingAddress().getStreet().toString());
		httpBuilder.addQueryParameter("billing_city", input.getCustomer().getBillingAddress().getCity().toString());
		httpBuilder.addQueryParameter("billing_region", input.getCustomer().getBillingAddress().getState().toString());
		httpBuilder.addQueryParameter("billing_postcode", input.getCustomer().getBillingAddress().getZip().toString());
		httpBuilder.addQueryParameter("billing_email", input.getCustomer().getEmail().toString());
		httpBuilder.addQueryParameter("billing_phone", input.getCustomer().getPhoneNumber().toString());
		
		httpBuilder.addQueryParameter("shipping_first_name", input.getCustomer().getFirstName().toString());
		httpBuilder.addQueryParameter("shipping_last_name", input.getCustomer().getLastName().toString());
		httpBuilder.addQueryParameter("shipping_country", input.getOrder().getDeliveryLocation().getCountry().toString());
		httpBuilder.addQueryParameter("shipping_address_1", input.getOrder().getDeliveryLocation().getStreet().toString());
		httpBuilder.addQueryParameter("shipping_city", input.getOrder().getDeliveryLocation().getCity().toString());
		httpBuilder.addQueryParameter("shipping_region", input.getOrder().getDeliveryLocation().getState().toString());
		httpBuilder.addQueryParameter("shipping_postcode", input.getOrder().getDeliveryLocation().getZip().toString());
		httpBuilder.addQueryParameter("shipping_email", input.getCustomer().getEmail().toString());
		httpBuilder.addQueryParameter("shipping_phone", input.getCustomer().getPhoneNumber().toString());
		httpBuilder.addQueryParameter("order_amount", Float.toString(input.getOrder().getPayment().getAmount().getTotal()));
		httpBuilder.addQueryParameter("order_quantity", input.getOrder().getFood().size() + "");
		httpBuilder.addQueryParameter("recurring", "false");
		httpBuilder.addQueryParameter("strictness", "1");
		
		if (input.getOrder().getPayment().getMethodOfPayment().getType() instanceof CreditCard) {
			CreditCard cc = (CreditCard) input.getOrder().getPayment().getMethodOfPayment().getType();
			httpBuilder.addQueryParameter("credit_card_bin", cc.getAccountNumber().toString().substring(0, 5));
			httpBuilder.addQueryParameter("credit_card_hash", Hashing.sha256()
					  .hashString(cc.getAccountNumber().toString(), StandardCharsets.UTF_8)
					  .toString());
			httpBuilder.addQueryParameter("credit_card_expiration_month", cc.getExpMonth().toString());
			httpBuilder.addQueryParameter("credit_card_expiration_year", cc.getExpYear().toString());
		}
		
		Request request = new Request.Builder().url(httpBuilder.build()).build();
		
		OkHttpClient client = new OkHttpClient().newBuilder().build();
        Call call = client.newCall(request);
        
        Response response = call.execute();
		response.body().string();
        
		return null;
	}
	
	private boolean isInitalized() {
       return StringUtils.isNotBlank(apiKey);
	}
	
	private String getUrl() throws UnknownHostException {
       if (url == null) {
    	 url =  HttpUrl.parse(BASE_URL + "/api/json/ip/" + apiKey + "/" + 
            Inet6Address.getLocalHost().getHostAddress()).newBuilder().build().toString(); 
       }
       return url;
	}

}
