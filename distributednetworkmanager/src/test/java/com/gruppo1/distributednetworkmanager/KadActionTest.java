package com.gruppo1.distributednetworkmanager;
import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class KadActionTest {

    @Test
    public void testConstructorWithAllParameters(){
        String expectedaddress="+393888145659";
        SMSPeer smsPeer=new SMSPeer(expectedaddress);
        int expectedId=11;
        int expectedpart=12;
        int expectedmaxpart=23;
        int inviteCode=2;
        int peerAddressCode=2;
        String expectedpayload="+393456298741";
        KadAction kadAction=new KadAction(smsPeer, KadAction.ActionType.INVITE,expectedId,expectedpart,expectedmaxpart, KadAction.PayloadType.PEER_ADDRESS,expectedpayload);
        assertEquals(expectedId,kadAction.getOperationId());
        assertEquals(expectedpart,kadAction.getCurrentPart());
        assertEquals(expectedmaxpart,kadAction.getTotalParts());
        assertEquals(expectedpayload,kadAction.getPayload());
        assertEquals(KadAction.ActionType.getTypeFromVal(inviteCode),kadAction.getActionType());
        assertEquals(KadAction.PayloadType.getTypeFromCode(peerAddressCode),kadAction.getPayloadType());
        assertEquals(smsPeer,kadAction.getPeer());

    }
    @Test
    public void testPayloadMach1(){
        String payload1="";
        assertTrue(KadAction.payloadMatchesType(KadAction.PayloadType.IGNORED,payload1));

    }
    @Test
    public void testPayloadMach2(){
        String payload2="true";
        assertTrue(KadAction.payloadMatchesType(KadAction.PayloadType.BOOLEAN,payload2));

    }
    @Test
    public void testPayloadMach3(){
        String payload3="+393888542978";
        assertTrue(KadAction.payloadMatchesType(KadAction.PayloadType.PEER_ADDRESS,payload3));

    }
    @Test
    public void testPayloadMach4(){
        String payload4="99999999999999998765432123456789";
        assertTrue(KadAction.payloadMatchesType(KadAction.PayloadType.NODE_ID,payload4));
    }
    @Test
    public void testConstructorWithSMSMessage(){
        String expectedaddress="+393888145659";
        SMSPeer smsPeer=new SMSPeer(expectedaddress);
        int length=4;
        int expectedId=2;
        int expectedpart=1;
        int expectedmaxpart=22;
        int inviteCode=2;
        int peerAddressCode=2;
        String expectedpayload="+393456298741";
        KadAction kadAction2=new KadAction(smsPeer, KadAction.ActionType.INVITE,expectedId,expectedpart,expectedmaxpart, KadAction.PayloadType.PEER_ADDRESS,expectedpayload);
        String data=inviteCode+KadAction.addPadding(expectedId,length)+KadAction.addPadding(expectedpart,length)
                +KadAction.addPadding(expectedmaxpart,length)+peerAddressCode+expectedpayload;
        assertEquals(data,kadAction2.toMessage().getData());
        SMSMessage smsMessage=new SMSMessage(smsPeer,data);
        KadAction kadAction=new KadAction(smsMessage);
        assertEquals(data,kadAction.toMessage().getData());
        assertEquals(expectedId,kadAction.getOperationId());
        assertEquals(expectedpart,kadAction.getCurrentPart());
        assertEquals(expectedmaxpart,kadAction.getTotalParts());
        assertEquals(expectedpayload,kadAction.getPayload());
        assertEquals(KadAction.ActionType.getTypeFromVal(inviteCode),kadAction.getActionType());
        assertEquals(KadAction.PayloadType.getTypeFromCode(peerAddressCode),kadAction.getPayloadType());
        assertEquals(smsPeer,kadAction.getPeer());
    }

    @Test
    public void testIsRequestResponse(){
        int invalidCode=241;
        assertFalse(KadAction.ActionType.INVALID.isRequest());
        assertTrue(KadAction.ActionType.PING.isRequest());
        assertTrue(KadAction.ActionType.PING_ANSWER.isResponse());
        assertTrue(KadAction.ActionType.INVITE.isRequest());
        assertTrue(KadAction.ActionType.INVITE_ANSWER.isResponse());
        assertTrue(KadAction.ActionType.STORE.isRequest());
        assertTrue(KadAction.ActionType.STORE_ANSWER.isResponse());
        assertTrue(KadAction.ActionType.FIND_NODE.isRequest());
        assertTrue(KadAction.ActionType.FIND_NODE_ANSWER.isResponse());
        assertTrue(KadAction.ActionType.FIND_VALUE.isRequest());
        assertTrue(KadAction.ActionType.FIND_VALUE_ANSWER.isResponse());

        assertFalse(KadAction.ActionType.INVALID.isResponse());
        assertFalse(KadAction.ActionType.PING.isResponse());
        assertFalse(KadAction.ActionType.PING_ANSWER.isRequest());
        assertFalse(KadAction.ActionType.INVITE.isResponse());
        assertFalse(KadAction.ActionType.INVITE_ANSWER.isRequest());
        assertFalse(KadAction.ActionType.STORE.isResponse());
        assertFalse(KadAction.ActionType.STORE_ANSWER.isRequest());
        assertFalse(KadAction.ActionType.FIND_NODE.isResponse());
        assertFalse(KadAction.ActionType.FIND_NODE_ANSWER.isRequest());
        assertFalse(KadAction.ActionType.FIND_VALUE.isResponse());
        assertFalse(KadAction.ActionType.FIND_VALUE_ANSWER.isRequest());
        assertEquals(KadAction.PayloadType.INVALID,KadAction.PayloadType.getTypeFromCode(invalidCode));
        assertEquals(KadAction.ActionType.INVALID, KadAction.ActionType.getTypeFromVal(invalidCode));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValid()
    {
        String expectedaddress="+393888145659";
        SMSPeer smsPeer=new SMSPeer(expectedaddress);
        int expectedId=1;
        int expectedpart=1;
        int expectedmaxpart=2;
        String expectedpayload="+393456298741";
        KadAction kadAction=new KadAction(smsPeer, KadAction.ActionType.INVALID,expectedId,expectedpart,expectedmaxpart, KadAction.PayloadType.PEER_ADDRESS,expectedpayload);
        kadAction.isValid();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValid2()
    {
        String expectedaddress="+393888145659";
        SMSPeer smsPeer=new SMSPeer(expectedaddress);
        int expectedId=1;
        int expectedpart=1;
        int expectedmaxpart=2;
        String expectedpayload="+393456298741";
        KadAction kadAction=new KadAction(smsPeer, KadAction.ActionType.INVITE,expectedId,expectedpart,expectedmaxpart, KadAction.PayloadType.INVALID,expectedpayload);
        kadAction.isValid();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testIsValid3()
    {
        String expectedaddress="+393888145659";
        SMSPeer smsPeer=new SMSPeer(expectedaddress);
        int expectedId=1;
        int expectedpart=5;
        int expectedmaxpart=1234;
        String expectedpayload="+393456298741";
        KadAction kadAction=new KadAction(smsPeer, KadAction.ActionType.INVITE,expectedId,expectedpart,expectedmaxpart, KadAction.PayloadType.PEER_ADDRESS,expectedpayload);
        assertFalse(kadAction.isValid());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testIsValid4()
    {
        String expectedaddress="+393888145659";
        SMSPeer smsPeer=new SMSPeer(expectedaddress);
        int expectedId=-5;
        int expectedpart=5;
        int expectedmaxpart=2;
        String expectedpayload="+393456298741";
        KadAction kadAction=new KadAction(smsPeer, KadAction.ActionType.INVITE,expectedId,expectedpart,expectedmaxpart, KadAction.PayloadType.PEER_ADDRESS,expectedpayload);
        assertFalse(kadAction.isValid());
    }
    @Test
    public void  testAddPadding()
    {
        int wantedLength=4;
        int id=5;
        String expectedId="0005";
        assertEquals(expectedId,KadAction.addPadding(id,wantedLength));
    }
    @Test
    public void  testToMessage()
    {
        String expectedaddress="+393888145659";
        SMSPeer smsPeer=new SMSPeer(expectedaddress);
        int wantedLength=4;
        int expectedId=1;
        int expectedpart=1;
        int expectedmaxpart=2;
        String expectedpayload="+393456298741";
        String expectedMassage= KadAction.ActionType.INVITE.getCode()+KadAction.addPadding(expectedId,wantedLength)+KadAction.addPadding(expectedpart,wantedLength)+
                KadAction.addPadding(expectedmaxpart,wantedLength)+ KadAction.PayloadType.PEER_ADDRESS.getCode() +expectedpayload;
        KadAction kadAction=new KadAction(smsPeer, KadAction.ActionType.INVITE,expectedId,expectedpart,expectedmaxpart, KadAction.PayloadType.PEER_ADDRESS,expectedpayload);
        SMSMessage smsMessage=kadAction.toMessage();
        assertEquals(expectedMassage,smsMessage.getData());
    }
    @Test
    public void  testToMessage2()
    {
        String expectedaddress="+393888145659";
        SMSPeer smsPeer=new SMSPeer(expectedaddress);
        int wantedLength=4;
        int expectedId=2;
        int expectedpart=4;
        int expectedmaxpart=5;
        String expectedpayload="+393456298741";
        String expectedMassage= KadAction.ActionType.INVITE.getCode()+KadAction.addPadding(expectedId,wantedLength)+KadAction.addPadding(expectedpart,wantedLength)+
                KadAction.addPadding(expectedmaxpart,wantedLength)+ KadAction.PayloadType.PEER_ADDRESS.getCode() +expectedpayload;
        KadAction kadAction=new KadAction(smsPeer, KadAction.ActionType.INVITE,expectedId,expectedpart,expectedmaxpart, KadAction.PayloadType.PEER_ADDRESS,expectedpayload);
        SMSMessage smsMessage=kadAction.toMessage();
        assertEquals(expectedMassage,smsMessage.getData());
    }
    @Test
    public void  testRemovePadding()
    {
        String strinToRemovePadding="0007";
        String string1="00004250";
        int expected1=4250;
        int expectedString=7;
        assertEquals(expectedString,KadAction.removePadding(strinToRemovePadding));
        assertEquals(expected1,KadAction.removePadding(string1));
    }

}
