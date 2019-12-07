package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class PeerNodeTest {

    PeerNode testNode;
    SMSPeer testPeer;

    @Before
    public void initialize(){
        testPeer = new SMSPeer("+390425678123"); //Valid number
        testNode = new PeerNode(64, testPeer);
    }

    @Test
    public void PeerNode_Constructor_NoExceptionTest(){
        SMSPeer peer = new SMSPeer("+390425666001");
        PeerNode node = new PeerNode(128, peer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void PeerNode_Constructor_PeerIllegalArgumentExceptionTest(){
        SMSPeer peer = new SMSPeer("This is invalid");
        PeerNode node = new PeerNode(128, peer);
    }

    @Test
    public void PeerNode_hashTest(){
        BitSet hash = PeerNode.hash(testPeer, testNode.keyLength());
        assertEquals(hash, testNode.getAddress().getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void PeerNode_hashExceptionTest(){
        BitSet hash = PeerNode.hash(testPeer, 120);
    }

    @Test
    public void PeerNode_ConstructorBitSetTest(){
        BitSet a = new BitSet(128);
        a.set(0, 4);
        PeerNode node = new PeerNode(128, a);
        assertEquals(a, node.getAddress().getKey());
    }

    @Test
    public void PeerNode_getAddressNegativeTest() {
        SMSPeer newPeer = new SMSPeer("+390425667007");
        assertNotEquals(PeerNode.hash(newPeer, testNode.keyLength()), testNode.getAddress().getKey());
    }

    @Test
    public void PeerNode_distanceFromTest(){
        BitSet A = PeerNode.hash(testPeer, testNode.keyLength());

        SMSPeer newPeer = new SMSPeer("+390425667007");
        PeerNode newPeerNode = new PeerNode(testNode.keyLength(), newPeer);
        BitSet B = PeerNode.hash(newPeer, testNode.keyLength());

        A.xor(B);
        assertEquals(A, testNode.distanceFrom(newPeerNode));
        assertEquals(A, newPeerNode.distanceFrom(testNode));
    }

    @Test
    public void PeerNode_distanceFromResourceTest(){
        BitSet A = PeerNode.hash(testPeer, testNode.keyLength());

        StringResource resource = new StringResource("Casual Name", "Some info");
        ResourceNode newResourceNode = new ResourceNode(testNode.keyLength(), resource);
        BitSet B = ResourceNode.hash(resource, testNode.keyLength());

        A.xor(B);
        assertEquals(A, testNode.distanceFrom(newResourceNode));
        assertEquals(A, newResourceNode.distanceFrom(testNode));
    }

    @Test
    public void PeerNode_compareDistancePositiveTest(){
        BitSet A = PeerNode.hash(testPeer, testNode.keyLength());

        SMSPeer newPeer = new SMSPeer("+390425667007");
        PeerNode newPeerNode = new PeerNode(testNode.keyLength(), newPeer);
        BitSet B = PeerNode.hash(newPeer, testNode.keyLength());

        //A = testNode.getAddress(), B = newPeerNode.getAddress()

        BitSet C = new BitSet(testNode.keyLength()); //000...000
        C.set(0,4); //...00011111

        PeerNode confrontNode = new PeerNode(testNode.keyLength(), C);

        A.xor(C);
        B.xor(C);

        assertEquals(DistributedNetworkNode.compare(A,B), DistributedNetworkNode.compare(confrontNode.distanceFrom(testNode), confrontNode.distanceFrom(newPeerNode)));
        assertEquals(DistributedNetworkNode.compare(B,A), DistributedNetworkNode.compare(confrontNode.distanceFrom(newPeerNode), confrontNode.distanceFrom(testNode)));
        assertEquals(DistributedNetworkNode.compare(A,A), DistributedNetworkNode.compare(confrontNode.distanceFrom(testNode), confrontNode.distanceFrom(testNode)));
    }

    @Test
    public void PeerNode_isValidPositiveTest() {
        assertTrue(testNode.isValid());
        BitSet A = PeerNode.hash(testPeer, testNode.keyLength());
        PeerNode newPeerNode = new PeerNode(testNode.keyLength(), A);
        assertTrue(newPeerNode.isValid());
    }


        @Test
    public void PeerNode_getPhysicalPeerPositiveTest(){
        assertEquals(testPeer, testNode.getPhysicalPeer());
    }

    @Test
    public void PeerNode_getPhysicalPeerNegativeTest(){
        SMSPeer newPeer = new SMSPeer("+390425667007");
        PeerNode newPeerNode = new PeerNode(testNode.keyLength(), newPeer);
        assertNotEquals(testPeer, newPeerNode.getPhysicalPeer());
    }
}