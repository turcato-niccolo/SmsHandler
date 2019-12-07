package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class DistributedNetworkNodeTest {

    DistributedNetworkNode testNode;
    String testString = "This is a random Key String";

    @Before
    public void initialize() {
        testNode = new DistributedNetworkNode(64, DistributedNetworkNode.hash(testString, 64));
    }

    @Test(expected = IllegalArgumentException.class)
    public void Node_Constructor_NumBitsIllegalArgumentExceptionTest() {
        DistributedNetworkNode node = new DistributedNetworkNode(127, DistributedNetworkNode.hash(testString, 128));
    }

    @Test
    public void Node_hashTest() {
        BitSet hash = DistributedNetworkNode.hash(testString, testNode.keyLength());
        assertEquals(hash, testNode.getKey());
    }

    @Test
    public void Node_comparePositiveTest() {
        BitSet big = new BitSet(128);
        BitSet small = new BitSet(128);

        big.set(10, 100);
        small.set(10, 80);
        assertEquals(DistributedNetworkNode.compare(small, big), 1);
        assertEquals(DistributedNetworkNode.compare(big, small), -1);
        assertEquals(DistributedNetworkNode.compare(big, big), 0);
    }

    @Test
    public void Node_distanceFromTest() {
        BitSet A = DistributedNetworkNode.hash(testString, testNode.keyLength());

        String newString = "Another valid string key";
        DistributedNetworkNode newPeerNode = new DistributedNetworkNode(testNode.keyLength(), DistributedNetworkNode.hash(newString, testNode.keyLength()));
        BitSet B = DistributedNetworkNode.hash(newString, testNode.keyLength());

        A.xor(B);
        assertEquals(A, testNode.distanceFrom(newPeerNode));
        assertEquals(A, newPeerNode.distanceFrom(testNode));
    }

    @Test
    public void Node_compareDistancePositiveTest() {
        BitSet A = DistributedNetworkNode.hash(testString, testNode.keyLength());

        String newString = "Another valid string key";
        DistributedNetworkNode newPeerNode = new DistributedNetworkNode(testNode.keyLength(), DistributedNetworkNode.hash(newString, testNode.keyLength()));
        BitSet B = DistributedNetworkNode.hash(newString, testNode.keyLength());

        //A = testNode.getKey(), B = newPeerNode.getKey()

        BitSet C = new BitSet(testNode.keyLength()); //000...000
        C.set(0, 4); //...00011111

        DistributedNetworkNode confrontNode = new DistributedNetworkNode(testNode.keyLength(), C);

        A.xor(C);
        B.xor(C);

        assertEquals(DistributedNetworkNode.compare(A, B), DistributedNetworkNode.compare(confrontNode.distanceFrom(testNode), confrontNode.distanceFrom(newPeerNode)));
        assertEquals(DistributedNetworkNode.compare(B, A), DistributedNetworkNode.compare(confrontNode.distanceFrom(newPeerNode), confrontNode.distanceFrom(testNode)));
        assertEquals(DistributedNetworkNode.compare(A, A), DistributedNetworkNode.compare(confrontNode.distanceFrom(testNode), confrontNode.distanceFrom(testNode)));
    }

    @Test
    public void Node_compareDistanceWrongAddrSpaceTest(){

    }

    @Test
    public void Node_equalsTest() {
        SMSPeer calogero = new SMSPeer("+390425668606");
        PeerNode a = new PeerNode(128, calogero);
        PeerNode b = new PeerNode(128, calogero);
        PeerNode c = new PeerNode(64, calogero);

        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertTrue(a.equals(a));
        assertFalse(a.equals(c));
        assertFalse(c.equals(b));
    }
}