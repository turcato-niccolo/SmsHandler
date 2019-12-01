package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class ResourceNodeTest {

    ResourceNode testNode;
    StringResource testResource;

    @Before
    public void Initialize(){
        testResource = new StringResource("This is a valid Resource Name", "This is a valid Resource Value");
        testNode = new ResourceNode(128, testResource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void PeerNode_Constructor_ResourceIllegalArgumentExceptionTest(){
        StringResource resource = new StringResource("", "I'm invalid");
        ResourceNode node = new ResourceNode(128, resource);
    }

    @Test
    public void ResourceNode_getNameTest() {
        assertEquals(testNode.getName(), testResource.getName());
    }

    @Test
    public void ResourceNode_getValueTest() {
        assertEquals(testNode.getValue(), testResource.getValue());
    }

    @Test
    public void PeerNode_distanceFromPeerTest(){
        BitSet A = ResourceNode.hash(testResource, testNode.keyLength());

        SMSPeer newPeer = new SMSPeer("+390425667007");
        PeerNode newPeerNode = new PeerNode(testNode.keyLength(), newPeer);
        BitSet B = PeerNode.hash(newPeer, testNode.keyLength());

        A.xor(B);
        assertEquals(A, testNode.distanceFrom(newPeerNode));
        assertEquals(A, newPeerNode.distanceFrom(testNode));
    }

    @Test
    public void PeerNode_distanceFromTest(){
        BitSet A = ResourceNode.hash(testResource, testNode.keyLength());

        StringResource resource = new StringResource("Casual Name", "Some info");
        ResourceNode newResourceNode = new ResourceNode(testNode.keyLength(), resource);
        BitSet B = ResourceNode.hash(resource, testNode.keyLength());

        A.xor(B);
        assertEquals(A, testNode.distanceFrom(newResourceNode));
        assertEquals(A, newResourceNode.distanceFrom(testNode));
    }

}