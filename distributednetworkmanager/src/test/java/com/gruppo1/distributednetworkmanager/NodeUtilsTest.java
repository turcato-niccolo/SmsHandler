package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.Peer;
import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;

public class NodeUtilsTest {
    BitSet peerKey;
    BitSet resourceKey;
    Peer<String> peer;
    Resource<String, String> resource;
    PeerNode node;
    int numBit = 128;

    @Before
    public void init(){
        peer = new SMSPeer("+390425777777");
        peerKey = BitSetUtils.hash(peer.getAddress().getBytes(), numBit);
        resource = new StringResource("This is my name", "this is my value");
        resourceKey = BitSetUtils.hash(resource.getName().getBytes(), numBit);
        node = new PeerNode(new BinarySet(peerKey));
    }

    @Test
    public void NodeUtils_getIdForPeerPositiveTest(){
        assertEquals(peerKey, NodeUtils.getIdForPeer(peer, numBit));
    }

    @Test(expected = IllegalArgumentException.class)
    public void NodeUtils_getIdForPeerIllegalKeyLengthNegativeTest(){
        NodeUtils.getIdForPeer(peer, 170);
    }

    @Test(expected = IllegalArgumentException.class)
    public void NodeUtils_getIdForPeerIllegalPeerNegativeTest(){
        NodeUtils.getIdForPeer(null, numBit);
    }

    @Test
    public void NodeUtils_getIdForResourcePositiveTest(){
        assertEquals(resourceKey, NodeUtils.getIdForResource(resource, numBit));
    }

    @Test(expected = IllegalArgumentException.class)
    public void NodeUtils_getIdForResourceIllegalKeyLengthNegativeTest(){
        NodeUtils.getIdForResource(resource, 170);
    }

    @Test(expected = IllegalArgumentException.class)
    public void NodeUtils_getIdForResourceIllegalPeerNegativeTest(){
        NodeUtils.getIdForResource(null, numBit);
    }

    @Test
    public void NodeUtils_getNodeForPeerPositiveTest(){
        node = new PeerNode(new BinarySet(peerKey));
        assertEquals(node, NodeUtils.getNodeForPeer(peer, numBit));
    }

    @Test(expected = IllegalArgumentException.class)
    public void NodeUtils_getNodeForPeerIllegalKeyLengthNegativeTest(){
        NodeUtils.getNodeForPeer(peer, 170);
    }

    @Test(expected = IllegalArgumentException.class)
    public void NodeUtils_getNodeForPeerIllegalPeerNegativeTest(){
        NodeUtils.getNodeForPeer(null, numBit);
    }

}