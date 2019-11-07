package com.dezen.riccardo.smshandler;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SmsMessageTest {

    private SMSMessage message;
    private String data;
    private SMSPeer peer;

    @Before
    public void createMessage(){
        peer = new SMSPeer("335");
        data = "body";
        message = new SMSMessage(peer, data);
    }

    @Test
    public void getData(){
        assertEquals(data, message.getData());
    }

    @Test
    public void getPeer(){
        assertEquals(peer, message.getPeer());
    }

    @Test
    public void isNotValidPeer(){
        peer = new SMSPeer("");
        message = new SMSMessage(peer, data);
        assertEquals(false, message.isValid());
    }

    @Test
    public void isNotValidData(){
        data = "";
        message = new SMSMessage(peer, data);
        assertEquals(false, message.isValid());
    }

    @Test
    public void isValid(){
        assertEquals(true, message.isValid());
    }

}
