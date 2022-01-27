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

import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.gottaeat.domain.geography.LatLon;
import com.topografix.gpx._1._1.GpxType;
import com.topografix.gpx._1._1.WptType;

public class TrekSimulator {

	private String gpxFile;
	private GpxType gpx;
	private WptType previousWayPoint;
	
	// This is the property we listen to for changes
	private LatLon currentLocation;
	
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	public TrekSimulator(String srcFile) {
		this.gpxFile = srcFile;
	}

	public void start() throws FileNotFoundException, JAXBException {
		initGpx();
		
		gpx.getTrk().forEach(trk -> {
			trk.getTrkseg().forEach(seg -> {
				seg.getTrkpt().forEach(point -> {
					
					// For the first way point, don't sleep
					if (previousWayPoint != null) {
					  try {
						 /* Calculate the time between the way point that the currentLocation  
						  * is currently set to and when the way point we are processing now
						  * was reached. 
						  * 
						  * Then we "simulate" the travel time by sleeping that 
						  * amount of time.
						  */
						 Thread.sleep(timeBetweenWaypoints(previousWayPoint, point));
					  } catch (InterruptedException e) {
						  e.printStackTrace();
					  }
					}
					
					// Update the current location property
					setCurrentLocation(
						LatLon.newBuilder()
							.setLatitude(point.getLat().doubleValue())
							.setLongitude(point.getLon().doubleValue())
						    .build());
					
					// Update the previous way point value for the next loop iteration
					previousWayPoint = point;
				});
			});
		});
	}
	
	private long timeBetweenWaypoints(WptType current, WptType next) {
		return next.getTime().toGregorianCalendar().getTimeInMillis() -
				current.getTime().toGregorianCalendar().getTimeInMillis();
	}
	
	@SuppressWarnings("unchecked")
	private void initGpx() throws JAXBException, FileNotFoundException {
		JAXBContext jaxbContext = JAXBContext.newInstance(GpxType.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		
		InputStream inStream = new FileInputStream(this.gpxFile);
		JAXBElement<GpxType> element = (JAXBElement<GpxType>) jaxbUnmarshaller.unmarshal( inStream );
		this.gpx = element.getValue();
	}

	public LatLon getCurrentLocation() {
		return currentLocation;
	}
	
	public void setCurrentLocation(LatLon newValue) {
		LatLon oldValue = currentLocation;
		currentLocation = newValue;
	    changes.firePropertyChange("currentLocation", oldValue, newValue);
	}
	
	public PropertyChangeSupport getPropertyChangeSupport() {
	    return changes;
	}
}
