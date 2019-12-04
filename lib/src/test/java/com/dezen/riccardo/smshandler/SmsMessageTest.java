package com.dezen.riccardo.smshandler;

import android.telephony.SmsMessage;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Giorgia Bortoletti
 */
public class SmsMessageTest {

    private SMSMessage message;
    private String data;
    private SMSPeer peer;

    @Before
    public void createMessage(){
        peer = new SMSPeer("+12025550100");
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
    public void isEmptyPeer(){
        SMSPeer peerEmpty = new SMSPeer("");
        SMSMessage newMessage = new SMSMessage(peerEmpty, data);
        assertFalse(newMessage.isValid());
    }

    @Test
    public void isNullPeer(){
        SMSPeer peerNull = new SMSPeer(null);
        SMSMessage newMessage = new SMSMessage(peerNull, data);
        assertFalse(newMessage.isValid());
    }

    @Test
    public void isBlankPeer(){
        SMSPeer peerBlank = new SMSPeer("  ");
        SMSMessage newMessage = new SMSMessage(peerBlank, data);
        assertFalse(newMessage.isValid());
    }

    @Test
    public void isEmptyData(){
        String dataEmpty = "";
        SMSMessage newMessage = new SMSMessage(peer, dataEmpty);
        assertFalse(newMessage.isValid());
    }

    @Test
    public void isValid(){
        assertTrue(message.isValid());
    }

}
