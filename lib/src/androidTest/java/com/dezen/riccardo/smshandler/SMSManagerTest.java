package com.dezen.riccardo.smshandler;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SMSManagerTest {

    private SMSManager smsManager;

    @Before
    public void createSmsManager(){
        Context context = ApplicationProvider.getApplicationContext();
        this.smsManager = SMSManager.getInstance(context);
    }

    @Test
    public void sendMessage_bodyEmpty() {
        SMSPeer peer = new SMSPeer("3334455666");
        String data = "";
        SMSMessage smsMessage = new SMSMessage(peer, data);
        assertEquals(false, this.smsManager.sendMessage(smsMessage));
    }

    @Test
    public void sendMessage_peerEmpty() {
        SMSPeer peer = new SMSPeer("");
        String data = "body";
        SMSMessage smsMessage = new SMSMessage(peer, data);
        assertEquals(false, this.smsManager.sendMessage(smsMessage));
    }

    @Test
    public void sendMessage_peerBlank() {
        SMSPeer peer = new SMSPeer("  ");
        String data = "body";
        SMSMessage smsMessage = new SMSMessage(peer, data);
        assertEquals(false, this.smsManager.sendMessage(smsMessage));
    }

    /**
     * Peer not valid as global phone number
     */
    @Test
    public void sendMessage_peerNotValid() {
        SMSPeer peer = new SMSPeer("{{@@");
        String data = "body";
        SMSMessage smsMessage = new SMSMessage(peer, data);
        assertEquals(false, this.smsManager.sendMessage(smsMessage));
    }

    @Test
    public void sendMessage_valid() {
        SMSPeer peer = new SMSPeer("3334455666");
        String data = "body";
        SMSMessage smsMessage = new SMSMessage(peer, data);
        //TODO? not sendMessage because it does not have SEND_SMS permission
        assertEquals(true, this.smsManager.sendMessage(smsMessage));
    }
}
