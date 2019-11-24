package com.example.killerapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocationManagerTest {

    private LocationManager locationManager;

    @Test
    public void testGetRequestStringMessage() {
        locationManager = new LocationManager();

        String locationRequest = locationManager.getRequestStringMessage();
        assertTrue(locationRequest, locationRequest.contains(locationManager.locationMessages[locationManager.request]));
    }
    @Test
    public void testContainsLocationRequest()
    {
        locationManager = new LocationManager();

        String locationRequest = locationManager.getRequestStringMessage();
        assertTrue(locationManager.containsLocationRequest(locationRequest));
    }


}