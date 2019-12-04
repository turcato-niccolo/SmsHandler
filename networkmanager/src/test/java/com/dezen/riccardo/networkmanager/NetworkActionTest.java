package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/*
 * Syntax:
         *
         * <SMSLibrary-TAG>[ACTION]<SEPARATOR>[ARGUMENT]<SEPARATOR>[EXTRA]
        *
        * INVITE [IGNORED] [IGNORED]
        * ACCEPT [IGNORED] [IGNORED]
        * ADD_USER [PEER] [IGNORED]
        * REMOVE_USER [PEER] [IGNORED]
        * GREET_USER [PEER] [IGNORED]
        * MSG [MESSAGE] [IGNORED]
        * REMOVE_RESOURCE [KEY] [IGNORED]
        * ADD_RESOURCE [KEY] [VALUE]
        */

public class NetworkActionTest {
    NetworkAction testAction;
    SMSPeer testPeer;


    @Before
    public void Initialize(){
        testPeer = new SMSPeer("+390425669011");
    }

    @Test
    public void NetworkAction_ConstructorINVITETest(){
        testAction = new NetworkAction(NetworkAction.Type.INVITE, NetworkAction.DEFAULT_IGNORED, NetworkAction.DEFAULT_IGNORED);
        testAction.setDestinationPeer(testPeer);
        assertTrue(testAction.isValid());
    }

    @Test
    public void NetworkAction_ConstructorACCEPT_INVITETest(){
        testAction = new NetworkAction(NetworkAction.Type.ANSWER_INVITE, NetworkAction.DEFAULT_IGNORED, NetworkAction.DEFAULT_IGNORED);
        testAction.setDestinationPeer(testPeer);
        assertTrue(testAction.isValid());
    }

    @Test
    public void NetworkAction_ConstructorADD_USERTest(){
        testAction = new NetworkAction(NetworkAction.Type.ADD_USER, testPeer.getAddress(), NetworkAction.DEFAULT_IGNORED);
        testAction.setDestinationPeer(testPeer);
        assertTrue(testAction.isValid());
    }

    @Test
    public void NetworkAction_ConstructorREMOVE_USERTest(){
        testAction = new NetworkAction(NetworkAction.Type.REMOVE_USER, testPeer.getAddress(), NetworkAction.DEFAULT_IGNORED);
        testAction.setDestinationPeer(testPeer);
        assertTrue(testAction.isValid());
    }

    @Test
    public void NetworkAction_ConstructorGREET_USERTest(){
        testAction = new NetworkAction(NetworkAction.Type.GREET_USER, testPeer.getAddress(), NetworkAction.DEFAULT_IGNORED);
        testAction.setDestinationPeer(testPeer);
        assertTrue(testAction.isValid());
    }

    @Test
    public void NetworkAction_ConstructorMSGTest(){
        testAction = new NetworkAction(NetworkAction.Type.MSG, "This is a message", NetworkAction.DEFAULT_IGNORED);
        testAction.setDestinationPeer(testPeer);
        assertTrue(testAction.isValid());
    }

    @Test
    public void NetworkAction_ConstructorADD_RESOURCETest(){
        testAction = new NetworkAction(NetworkAction.Type.ADD_RESOURCE, "This is a Res key", "This is the resource's body");
        testAction.setDestinationPeer(testPeer);
        assertTrue(testAction.isValid());
    }
    @Test
    public void NetworkAction_ConstructorREMOVE_RESOURCETest(){
        testAction = new NetworkAction(NetworkAction.Type.REMOVE_RESOURCE, "This is a Key", NetworkAction.DEFAULT_IGNORED);
        testAction.setDestinationPeer(testPeer);
        assertTrue(testAction.isValid());
    }

    @Test
    public void NetworkAction_ConstructorFromSMSTest(){
        testAction = new NetworkAction(NetworkAction.Type.INVITE, NetworkAction.DEFAULT_IGNORED, NetworkAction.DEFAULT_IGNORED);
        testAction.setDestinationPeer(testPeer);
        SMSMessage generated = testAction.generateMessage();
        String[] params = generated.getData().split("\r");
        NetworkAction receivedAction = new NetworkAction(generated);
        assertEquals(testAction.getAction(), receivedAction.getAction());
        assertEquals(testAction.getParam(), receivedAction.getParam());
        assertEquals(testAction.getExtra(), receivedAction.getExtra());
        assertEquals(testAction.getPeer(), receivedAction.getPeer());
    }

    @Test
    public void NetworkAction_SetDestinationPeerPositiveTest(){
        testAction = new NetworkAction(NetworkAction.Type.ADD_RESOURCE, "This is a Res key", "This is the resource's body");
        testAction.setDestinationPeer(testPeer);
        assertTrue(testAction.isValid());
        SMSMessage generated = testAction.generateMessage();
        assertTrue(generated.isValid());
        assertEquals(testPeer, generated.getPeer());
    }

    @Test
    public void NetworkAction_SetDestinationPeerNegativeTest(){
        testAction = new NetworkAction(NetworkAction.Type.ADD_RESOURCE, "This is a Res key", "This is the resource's body");
        testAction.setDestinationPeer(new SMSPeer("+390425667567"));
        assertTrue(testAction.isValid());
        SMSMessage generated = testAction.generateMessage();
        assertTrue(generated.isValid());
        assertNotEquals(testPeer, generated.getPeer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void NetworkAction_InvalidADD_RESOURCEActionTest(){
        testAction = new NetworkAction(NetworkAction.Type.ADD_RESOURCE, NetworkAction.DEFAULT_IGNORED, "This is the resource's body");
    }

    @Test
    public void NetworkAction_InvalidGetParamsActionTest(){
        StringResource testResource = new StringResource("This is a Res key", "This is the resource's body");
        testAction = new NetworkAction(NetworkAction.Type.ADD_RESOURCE, testResource.getName(), testResource.getValue());
        assertEquals(NetworkAction.Type.ADD_RESOURCE, testAction.getAction());
        assertEquals(testResource.getName(), testAction.getParam());
        assertEquals(testResource.getValue(), testAction.getExtra());
    }
}