package com.example.killerapp;


import org.junit.Test;

import static org.junit.Assert.*;

public class GetLongitudeTest {

    private final String longitudeTag = "<LG>";
    private final String longitudeTagEnd = "</LG>";
    @Test
    public void CorrectResult()
    {
        LocationManager locationManager = new LocationManager();
        String expected = "12.44";
        String received = longitudeTag + expected + longitudeTagEnd;
        assertEquals(expected, locationManager.getLongitude(received));
    }

    @Test
    public void CorrectResultNegative()
    {
        LocationManager locationManager = new LocationManager();
        String expected = "-45.6";
        String received = longitudeTag + expected + longitudeTagEnd;
        assertEquals(expected, locationManager.getLongitude(received));
    }
}