package com.dezen.riccardo.smshandler;


import com.dezen.riccardo.smshandler.exceptions.InvalidAddressException;
import com.dezen.riccardo.smshandler.exceptions.InvalidMessageException;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

/**
 * @author Giorgia Bortoletti
 * @author Turcato Niccolo' (FIX)
 */
public class SmsMessageTest {

    private SMSMessage message;
    private String data;
    private SMSPeer peer;

    @Before
    public void createMessage(){
        assertNotSame(SMSPeer.PhoneNumberValidity.ADDRESS_GENERIC_INVALID, SMSPeer.isAddressValid("+390425668001"));
        assertTrue(SMSPeer.isGlobalPhoneNumber("+390425668001"));
        //assertTrue(SMSPeer.PhoneNumberValidity.ADDRESS_NO_COUNTRY_CODE == SMSPeer.isAddressValid("+390425668001"));
        //assertTrue(SMSPeer.PhoneNumberValidity.ADDRESS_NOT_PHONE_NUMBER == SMSPeer.isAddressValid("+390425668001"));
        //assertTrue(SMSPeer.PhoneNumberValidity.ADDRESS_TOO_LONG == SMSPeer.isAddressValid("+390425668001"));
        //assertTrue(SMSPeer.PhoneNumberValidity.ADDRESS_TOO_SHORT == SMSPeer.isAddressValid("+390425668001"));
       //assertTrue(SMSPeer.PhoneNumberValidity.ADDRESS_VALID == SMSPeer.isAddressValid("+390425668001"));
        peer = new SMSPeer("+390425668001");

        data = "body";
        message = new SMSMessage(peer, data);
    }

    @Test
    public void getData(){
        assertTrue(peer.isValid());
        assertEquals(data, message.getData());
    }

    @Test
    public void getPeer(){
        assertEquals(peer, message.getPeer());
    }

    @Test(expected = InvalidAddressException.class)
    public void isEmptyPeer(){
        SMSPeer peerEmpty = new SMSPeer("");
        //Can't create an invalid Peer anymore
        SMSMessage newMessage = new SMSMessage(peerEmpty, data);
        assertFalse(newMessage.isValid());
    }

    @Test(expected = InvalidAddressException.class)
    public void isNullPeer(){
        SMSPeer peerNull = new SMSPeer(null);
        //Can't create an invalid Peer anymore
        SMSMessage newMessage = new SMSMessage(peerNull, data);
        assertFalse(newMessage.isValid());
    }

    @Test(expected = InvalidAddressException.class)
    public void isBlankPeer(){
        SMSPeer peerBlank = new SMSPeer("  ");
        //Can't create an invalid Peer anymore
        SMSMessage newMessage = new SMSMessage(peerBlank, data);
        assertFalse(newMessage.isValid());
    }

    @Test(expected = InvalidMessageException.class)
    public void isEmptyData(){
        String dataEmpty = "";
        SMSMessage newMessage = new SMSMessage(peer, dataEmpty); //should launch exception
    }

    @Test
    public void isValid(){
        assertTrue(message.isValid());
    }

}
