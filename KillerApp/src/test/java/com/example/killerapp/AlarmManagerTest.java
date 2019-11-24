package com.example.killerapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class AlarmManagerTest {

    @Test
    public void testGetRequestStringMessage() {
        AlarmManager alarmManager = new AlarmManager();
        String requestStringMessage = alarmManager.audioAlarmMessages[alarmManager.request];
        assertEquals(requestStringMessage, alarmManager.getRequestStringMessage());
    }

    @Test
    public void testContainsAlarmRequest() {
        AlarmManager alarmManager = new AlarmManager();
        String requestStringMessage = alarmManager.getRequestStringMessage();
        assertTrue(alarmManager.containsAlarmRequest(requestStringMessage));
    }
}