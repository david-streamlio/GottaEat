package com.gottaeat.simulators.driver;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.junit.Test;


public class TrekSimulatorTest {
	
	@Test
	public final void simpleTest() throws FileNotFoundException, JAXBException {
		TrekSimulator sim = new TrekSimulator("/Users/david/clone-zone/personal/GottaEat/driver-mobile-app-simulator/src/test/resources/4075989.gpx");
		
//		sim.getPropertyChangeSupport().addPropertyChangeListener(
//			"currentLocation", (PropertyChangeEvent event) -> {
//			    System.out.println(event.getPropertyName()  + " to " + event.getNewValue());
//		});
//		
//		sim.start();
		
	}
}
