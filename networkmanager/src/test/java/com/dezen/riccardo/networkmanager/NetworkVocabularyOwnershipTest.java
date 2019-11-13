package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NetworkVocabularyOwnershipTest {
    private NetworkVocabulary networkVocabulary = new NetworkVocabulary();
    private SMSPeer peer1 = new SMSPeer("peer1");
    private SMSPeer peer2 = new SMSPeer("peer2");
    private SMSPeer peer3 = new SMSPeer("peer3");
    private StringResource res1 = new StringResource("res1","1");
    private StringResource res2 = new StringResource("res2","2");
    private StringResource res3 = new StringResource("res3", "3");

    @Test
    public void initialization(){
        networkVocabulary.addPeer(peer1);
        networkVocabulary.addPeer(peer2);
        networkVocabulary.addPeer(peer3);
        networkVocabulary.addResource(res1);
        networkVocabulary.addResource(res2);
        networkVocabulary.addResource(res3);
        assertEquals(3,networkVocabulary.getPeers().length);
        assertEquals(3,networkVocabulary.getResources().length);
    }

    @Test
    public void addOwnerForResource() {
        //TODO Ownership these tests after updating contains() specs tests and impl.
    }

    @Test
    public void removeOwnerForResource() {
    }

    @Test
    public void getResourcesForPeer() {
    }

    @Test
    public void getResourceOwners() {
    }
}