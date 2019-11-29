package com.gruppo1.distributednetworkmanager;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.dezen.riccardo.networkmanager.NetworkDictionary;
import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkDictionaryWithOwnershipTest {
    //Reusable fields for Peer management tests

    private String address = "+390425111000";
    private SMSPeer peer = new SMSPeer(address);

    //Reusable fields for Resource management tests

    private String name = "example";
    private String value = "example";
    private StringResource resource = new StringResource(name,value);

    private NetworkDictionaryWithOwnership networkVocabulary;

    @Before
    public void createContext(){
        Context context = ApplicationProvider.getApplicationContext();
        networkVocabulary = new NetworkDictionaryWithOwnership(context);
        networkVocabulary.addPeer(peer);

    }

    @Test
    public void addResourcePositiveTest() {
        StringResource newResource = new StringResource("addResourcePositiveTestResourceKey", "addResourcePositiveTestResourceValue");
        assertTrue(networkVocabulary.contains(peer));
        assertTrue(networkVocabulary.addResource(newResource));
        assertFalse(networkVocabulary.verifyOwnership(peer, newResource));
    }

    @Test
    public void addResourceWithOwnerPositiveTest() {
        StringResource newResource = new StringResource("addResourceWithOwnerPositiveTestResourceKey", "addResourceWithOwnerPositiveTestResourceValue");
        assertTrue(networkVocabulary.contains(peer));
        assertTrue(networkVocabulary.addResource(newResource, peer));
        assertTrue(networkVocabulary.verifyOwnership(peer, newResource));
    }

    @Test
    public void addResourceWithOwnersPositiveTest() {
        StringResource newResource = new StringResource("addResourceWithOwnerPositiveTestResourceKey", "addResourceWithOwnerPositiveTestResourceValue");

        assertTrue(networkVocabulary.contains(peer));
        SMSPeer ownerPeer = new SMSPeer("+390425666111");
        assertTrue(networkVocabulary.addPeer(ownerPeer));
        assertTrue(networkVocabulary.contains(ownerPeer));

        assertTrue(networkVocabulary.addResource(newResource, new SMSPeer[]{peer, ownerPeer}));

        assertTrue(networkVocabulary.getResourcesOwnedByPeer(peer).contains(newResource));
        assertTrue(networkVocabulary.getResourcesOwnedByPeer(ownerPeer).contains(newResource));

        assertTrue(networkVocabulary.verifyOwnership(peer, newResource));
        assertTrue(networkVocabulary.verifyOwnership(ownerPeer, newResource));
    }

        @Test
    public void verifyOwnershipPositiveTest() {
        StringResource newResource = new StringResource("verifyOwnershipPositiveTestResourceKey", "verifyOwnershipPositiveTestResourceValue");
        assertTrue(networkVocabulary.addResource(newResource, peer));
        assertTrue(networkVocabulary.verifyOwnership(peer, newResource));
        assertFalse(networkVocabulary.verifyOwnership(NetworkDictionaryWithOwnership.NON_EXISTENT_OWNER, newResource));
    }

    @Test
    public void getResourcesOwnedByPeerPositiveTest() {
        StringResource newResource = new StringResource("getResourcesOwnedByPeerPositiveResourceKey", "getResourcesOwnedByPeerPositiveResourceValue");
        assertTrue(networkVocabulary.addResource(newResource, peer));
        assertTrue(networkVocabulary.getResourcesOwnedByPeer(peer).contains(newResource));


        //
    }

    @Test
    public void verifyUnexistentOwnershipTestPositive(){
        StringResource newResource = new StringResource("verifyUnexistentOwnershipTestPositiveResourceKey", "verifyUnexistentOwnershipTestPositiveResourceValue");
        assertTrue(networkVocabulary.addResource(newResource));
        assertTrue(networkVocabulary.verifyUnexistentOwnership(newResource));
    }

    @Test
    public void addOwnerToResourcePositiveTest(){
        StringResource newResource = new StringResource("addOwnerToResourcePositiveTestResourceKey", "addOwnerToResourcePositiveTestResourceValue");
        assertTrue(networkVocabulary.addResource(newResource));
        assertTrue(networkVocabulary.addOwnerToResource(peer, newResource));

        SMSPeer ownerPeer = new SMSPeer("+390425666111");
        assertTrue(networkVocabulary.addPeer(ownerPeer));
        assertTrue(networkVocabulary.contains(ownerPeer));

        assertTrue(networkVocabulary.addOwnerToResource(ownerPeer, newResource));

        SMSPeer anotherPeer = new SMSPeer("+390425666222");
        assertTrue(networkVocabulary.addPeer(anotherPeer));
        assertTrue(networkVocabulary.contains(anotherPeer));

        assertFalse(networkVocabulary.verifyOwnership(anotherPeer, newResource));
    }

}