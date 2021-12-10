package com.gottaeat.simulators.restaurant;

import org.apache.pulsar.client.impl.schema.AvroSchema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.restaurant.Restaurant;
import com.gottaeat.domain.restaurant.SolicitationResponse;

public class RestaurantSolicititationSimulator implements Function<FoodOrder, Void> {

	private static final Restaurant rest = 
		Restaurant.newBuilder()
			.setLocation(Address.newBuilder()
				.setCity("Chicago")
				.setState("IL")
				.setCountry("USA")
				.setStreet("1455 Main St")
				.setZip("66011")
				.build())
			.build();
	
	@Override
	public Void process(FoodOrder input, Context ctx) throws Exception {
		
		ctx.getLogger().info("Accepting order # " + ctx.getCurrentRecord()
			.getProperties().get("order-id"));
		
	    SolicitationResponse value = SolicitationResponse.newBuilder()
			.setFood(input.getFood())
			.setRestaurant(rest)
			.setNotificationTopic("persistent://restaurants/notifications/restaurant-123")
			.setEta(null)
			.build();
	    
	    ctx.newOutputMessage(ctx.getOutputTopic(), AvroSchema.of(SolicitationResponse.class))
	    	.value(value)
	    	.properties(ctx.getCurrentRecord().getProperties())
	    	.send();
	    
	    return null;
	}

}
