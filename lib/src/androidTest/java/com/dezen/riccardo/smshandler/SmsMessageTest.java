package com.dezen.riccardo.smshandler;

import com.dezen.riccardo.smshandler.exceptions.InvalidMessageException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Giorgia Bortoletti
 * @author Riccardo De Zen
 */
public class SmsMessageTest {

    //a default valid peer
    private SMSPeer peer;
    private SMSMessage message;
    private static final String EX_VALID_ADDRESS = "+39892424";
    private static final String VALID_ADDRESS = "+390425668000";
    private static final String EX_VALID_MSG = "Wassup Mankind";

    @Before
    public void createValidPeer(){
        peer = new SMSPeer(VALID_ADDRESS);
    }

    @Test
    public void isMessageValidNull(){
        assertEquals(
                SMSMessage.MessageValidity.MESSAGE_EMPTY,
                SMSMessage.isMessageValid(null)
        );
    }

    @Test
    public void isMessageValidEmpty(){
        assertEquals(
                SMSMessage.MessageValidity.MESSAGE_EMPTY,
                SMSMessage.isMessageValid("")
        );
    }

    @Test
    public void isMessageValidTooLong(){
        StringBuilder sb = new StringBuilder();
        while(sb.length() <= 160) sb.append(sb.length());
        assertEquals(
                SMSMessage.MessageValidity.MESSAGE_TOO_LONG,
                SMSMessage.isMessageValid(sb.toString())
        );
    }

    @Test
    public void isMessageValid(){
        assertEquals(
                SMSMessage.MessageValidity.MESSAGE_VALID,
                SMSMessage.isMessageValid(EX_VALID_MSG)
        );
    }

    @Test(expected = InvalidMessageException.class)
    public void constructorFails(){
        String invalidMSG = "";
        message = new SMSMessage(peer,invalidMSG);
    }

    @Test
    public void constructorSucceeds(){
        message = new SMSMessage(peer,EX_VALID_MSG);
        assertNotNull(message);
    }

    @Test
    public void getData(){
        message = new SMSMessage(peer,EX_VALID_MSG);
        assertEquals(EX_VALID_MSG, message.getData());
    }

    @Test
    public void getPeer(){
        message = new SMSMessage(peer,EX_VALID_MSG);
        assertEquals(peer, message.getPeer());
    }

    @Test
    public void isValid(){
        message = new SMSMessage(peer,EX_VALID_MSG);
        assertTrue(message.isValid());
    }

}