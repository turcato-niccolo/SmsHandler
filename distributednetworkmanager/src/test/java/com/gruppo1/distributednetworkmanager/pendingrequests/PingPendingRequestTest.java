package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

import org.junit.Test;

public class PingPendingRequestTest {
    @Test(expected = InvalidActionException.class)
    public void PingPendingRequest(){
        new PingPendingRequest(null, null);
    }
}
