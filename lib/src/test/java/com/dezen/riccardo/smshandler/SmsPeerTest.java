package com.dezen.riccardo.smshandler;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Giorgia Bortoletti
 * Small tweaks by Riccardo De Zen.
 */
public class SmsPeerTest {

    @Test
    public void getAddress(){
        String address = "+12025550100";
        SMSPeer peer = new SMSPeer(address);
        assertEquals(address, peer.getAddress());
    }

    @Test
    public void isNull(){
        SMSPeer peer = new SMSPeer(null);
        assertFalse(peer.isValid());
    }

    @Test
    public void isEmpty(){
        String address = "";
        SMSPeer peer = new SMSPeer(address);
        assertFalse(peer.isValid());
    }

    @Test
    public void isBlank(){
        String address = " ";
        SMSPeer peer = new SMSPeer(address);
        assertFalse(peer.isValid());
    }

    @Test
    public void isValid(){
        String address = "+12025550100";
        SMSPeer peer = new SMSPeer(address);
        assertTrue(peer.isValid());
    }

}
