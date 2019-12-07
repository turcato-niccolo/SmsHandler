package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.smshandler.Peer;
import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KBucketTest {
    int dim = 20;
    KBucket bucket;
    PeerNode node;
    SMSPeer peer = new SMSPeer("+390425678102");

    @Before
    public void Initialize()
    {
        bucket = new KBucket(dim);
        node = new PeerNode(128, peer);
        bucket.Add(node.getAddress());
    }

    @Test
    public void KBucket_ContainsPositiveTest()
    {
        assertTrue(bucket.Contains(node.getAddress()));
        PeerNode otherNode = new PeerNode(128, new SMSPeer("+39348456789"));

        assertFalse(bucket.Contains(otherNode.getAddress()));
    }

    @Test
    public void KBucket_AddsPositiveTest(){
        PeerNode a = new PeerNode(128, new SMSPeer("+39348456789"));

        assertTrue(bucket.Add(a.getAddress()));
        assertTrue(bucket.Contains(a.getAddress()));
    }

    @Test
    public void KBucket_RemovesPositiveTest() {
        PeerNode a = new PeerNode(128, new SMSPeer("+39348456789"));

        assertTrue(bucket.Add(a.getAddress()));
        assertTrue(bucket.Contains(a.getAddress()));
        assertTrue(bucket.Remove(a.getAddress()));
        assertFalse(bucket.Contains(a.getAddress()));
    }

    @Test
    public void KBucket_GetsElementsPositiveTest(){
        PeerNode a = new PeerNode(128, new SMSPeer("+39348456789"));
        PeerNode b = new PeerNode(128, new SMSPeer("+39348676789"));
        PeerNode c = new PeerNode(128, new SMSPeer("+39568456789"));

        KBucket newBucket = new KBucket(3);

        newBucket.Add(a.getAddress());
        newBucket.Add(b.getAddress());
        newBucket.Add(c.getAddress());

        Node[] result = newBucket.getElements();

        assertEquals(result[0], a.getAddress());
        assertEquals(result[1],b.getAddress());
        assertEquals(result[2], c.getAddress());
    }

    @Test
    public void KBucket_RemovesKeepsSortedPositiveTest() {
        PeerNode a = new PeerNode(128, new SMSPeer("+39348456789"));
        PeerNode b = new PeerNode(128, new SMSPeer("+39348676789"));
        PeerNode c = new PeerNode(128, new SMSPeer("+39568456789"));

        KBucket newBucket = new KBucket(3);

        newBucket.Add(a.getAddress());
        newBucket.Add(b.getAddress());
        newBucket.Add(c.getAddress());

        newBucket.Remove(b.getAddress());

        Node[] elements = newBucket.getElements();

        assertEquals(elements[0], a.getAddress());
        assertEquals(elements[1], c.getAddress());
    }

    @Test
    public void KBucket_getOldestPositiveTest(){
        PeerNode a = new PeerNode(128, new SMSPeer("+39348456789"));
        PeerNode b = new PeerNode(128, new SMSPeer("+39348676789"));
        PeerNode c = new PeerNode(128, new SMSPeer("+39568456789"));

        KBucket newBucket = new KBucket(3);

        newBucket.Add(a.getAddress());
        newBucket.Add(b.getAddress());
        newBucket.Add(c.getAddress());

        assertEquals(a.getAddress(), newBucket.getOldest());
    }
}