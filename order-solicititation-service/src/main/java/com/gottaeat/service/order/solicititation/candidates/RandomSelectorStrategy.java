package com.gottaeat.service.order.solicititation.candidates;

import java.util.ArrayList;
import java.util.List;

import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.order.FoodOrder;

/**
 * This implementation randomly generates a list of 3-5 "random" 
 * restaurant ids and returns them.  
 *
 */
public class RandomSelectorStrategy implements CandidateSelectorStrategy {

	@Override
	public List<String> getCandidates(FoodOrder order, Address deliveryAddr) {
		int numCandidates = 1;
		List<String> restaurants = new ArrayList<String> (numCandidates);
		
		for (int idx = 0; idx < numCandidates; idx++) {
			restaurants.add(generateRandomTopic());
		}
	
		return restaurants;
	}
	
	private String generateRandomTopic() {
		return "persistent://restaurants/solicitations/restaurant123";
	}

}
