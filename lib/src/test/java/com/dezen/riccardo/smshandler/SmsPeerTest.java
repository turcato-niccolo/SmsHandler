package com.dezen.riccardo.smshandler;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SmsPeerTest {

    private SMSPeer peer;

    @Test
    public void getAddress(){
        String address = "335";
        peer = new SMSPeer(address);
        assertEquals(address, peer.getAddress());
    }

    @Test
    public void isEmpty(){
        String address = "";
        peer = new SMSPeer(address);
        assertEquals(true, peer.isEmpty());
    }

    @Test
    public void isBlank(){
        String address = " ";
        peer = new SMSPeer(address);
        assertEquals(true, peer.isBlank());
    }

    @Test
    public void isValid(){
        String address = "3336";
        peer = new SMSPeer(address);
        assertEquals(true, peer.isValid());
    }

}
