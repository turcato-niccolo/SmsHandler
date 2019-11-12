package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NetworkVocabularyTest {
    @Test
    public void addPeer_validPeer() {
        String address = "example";
        SMSPeer peer = new SMSPeer(address);
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        assertTrue(networkVocabulary.addPeer(peer));
        assertEquals(1,networkVocabulary.getPeers().length);
    }

    @Test
    public void addPeer_nullPeer(){
        SMSPeer peer = null;
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        assertFalse(networkVocabulary.addPeer(peer));
        assertEquals(0,networkVocabulary.getPeers().length);
    }

    @Test
    public void addPeer_existingPeer(){
        String address = "example";
        SMSPeer peer = new SMSPeer(address);
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        networkVocabulary.addPeer(peer);
        assertFalse(networkVocabulary.addPeer(peer));
        assertEquals(1,networkVocabulary.getPeers().length);
    }

    @Test
    public void removePeer_nullPeer() {
        SMSPeer peer = null;
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        assertFalse(networkVocabulary.removePeer(peer));
    }

    @Test
    public void removePeer_existingPeer(){
        String address = "example";
        SMSPeer peer = new SMSPeer(address);
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        assertTrue(networkVocabulary.addPeer(peer));
        assertTrue(networkVocabulary.removePeer(peer));
        assertEquals(0,networkVocabulary.getPeers().length);
    }

    @Test
    public void removePeer_nonExistingPeer(){
        String existing_address = "I exist";
        SMSPeer existing_peer = new SMSPeer(existing_address);
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        networkVocabulary.addPeer(existing_peer);
        String non_existing_address = "I don't";
        SMSPeer non_existing_peer = new SMSPeer(non_existing_address);
        assertFalse(networkVocabulary.removePeer(non_existing_peer));
        assertEquals(1,networkVocabulary.getPeers().length);
    }

    @Test
    public void updatePeer_nullPeer() {
        SMSPeer peer = null;
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        assertFalse(networkVocabulary.updatePeer(peer));
    }

    @Test
    public void updatePeer_existingPeer() {
        String address = "example";
        SMSPeer peer = new SMSPeer(address);
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        networkVocabulary.addPeer(peer);
        assertTrue(networkVocabulary.updatePeer(peer));
        assertEquals(peer,networkVocabulary.getPeers()[0]);
    }

    @Test
    public void updatePeer_non_existing_Peer() {
        String existing_address = "I exist";
        SMSPeer existing_peer = new SMSPeer(existing_address);
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        networkVocabulary.addPeer(existing_peer);
        String non_existing_address = "I don't";
        SMSPeer non_existing_peer = new SMSPeer(non_existing_address);
        assertFalse(networkVocabulary.updatePeer(non_existing_peer));
        assertTrue(networkVocabulary.updatePeer(existing_peer));
    }

    @Test
    public void getPeers_isCopy() {
        String address = "I should remain";
        SMSPeer peer = new SMSPeer(address);
        NetworkVocabulary networkVocabulary = new NetworkVocabulary();
        networkVocabulary.addPeer(peer);
        SMSPeer[] peers = networkVocabulary.getPeers();
        peers = new SMSPeer[0];
        Assert.assertNotEquals(0,networkVocabulary.getPeers().length);
    }

    //TODO Resource tests
    @Test
    public void addResource() {
    }

    @Test
    public void removeResource() {
    }

    @Test
    public void updateResource() {
    }

    @Test
    public void getResource() {
    }
}