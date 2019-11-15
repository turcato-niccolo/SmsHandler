package com.dezen.riccardo.networkmanager.deprecated;

import com.dezen.riccardo.networkmanager.NetworkDictionary;
import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Assert;
import org.junit.Test;

class NetworkVocabularyOwnershipTest {
    private NetworkDictionary networkVocabulary = new NetworkDictionary();
    private SMSPeer peer1 = new SMSPeer("peer1");
    private SMSPeer peer2 = new SMSPeer("peer2");
    private SMSPeer peer3 = new SMSPeer("peer3");
    private StringResource res1 = new StringResource("res1","1");
    private StringResource res2 = new StringResource("res2","2");
    private StringResource res3 = new StringResource("res3", "3");

    private void initialization(){
        networkVocabulary.addPeer(peer1);
        networkVocabulary.addPeer(peer2);
        networkVocabulary.addPeer(peer3);
        networkVocabulary.addResource(res1);
        networkVocabulary.addResource(res2);
        networkVocabulary.addResource(res3);
    }

    @Test
    public void addOwnerForResourceIgnoresNonContainedPeer() {
        initialization();
        SMSPeer nonContainedPeer = new SMSPeer("I'm not contained");
        Assert.assertFalse(networkVocabulary.addOwnerForResource(nonContainedPeer,res1));
    }

    @Test
    public void addOwnerForResourceIgnoresNonContainedResource() {
        initialization();
        StringResource nonContainedResource = new StringResource("I'm not","contained");
        Assert.assertFalse(networkVocabulary.addOwnerForResource(peer1,nonContainedResource));
    }

    @Test
    public void addOwnerForResourceValidPair(){
        initialization();
        Assert.assertTrue(networkVocabulary.addOwnerForResource(peer1,res1));
        //False if already owned
        Assert.assertFalse(networkVocabulary.addOwnerForResource(peer1,res1));
    }

    @Test
    public void removeOwnerForResourceIgnoresNonContainedPeer() {
        initialization();
        SMSPeer nonContainedPeer = new SMSPeer("I'm not contained");
        Assert.assertFalse(networkVocabulary.removeOwnerForResource(nonContainedPeer,res1));
    }

    @Test
    public void removeOwnerForResourceIgnoresNonContainedResource() {
        initialization();
        StringResource nonContainedResource = new StringResource("I'm not","contained");
        Assert.assertFalse(networkVocabulary.removeOwnerForResource(peer1,nonContainedResource));
    }

    @Test
    public void removeOwnerForResourceValidPair(){
        initialization();
        networkVocabulary.addOwnerForResource(peer1,res1);
        Assert.assertTrue(networkVocabulary.removeOwnerForResource(peer1,res1));
        //False if not owned
        Assert.assertFalse(networkVocabulary.removeOwnerForResource(peer1,res1));
    }

    @Test
    public void getResourcesForPeerNoResources() {
        initialization();
        Assert.assertEquals(0,networkVocabulary.getResourcesForPeer(peer3).length);
    }

    @Test
    public void getResourcesForPeerSomeResources() {
        initialization();
        networkVocabulary.addOwnerForResource(peer1,res1);
        networkVocabulary.addOwnerForResource(peer1,res2);
        Assert.assertEquals(2,networkVocabulary.getResourcesForPeer(peer1).length);
    }

    @Test
    public void getResourceOwnersSomeOwners(){
        initialization();
        networkVocabulary.addOwnerForResource(peer1,res1);
        networkVocabulary.addOwnerForResource(peer2,res1);
        Assert.assertEquals(2,networkVocabulary.getResourceOwners(res1).length);
    }

    @Test
    public void getResourceOwnersNoOwners() {
        initialization();
        networkVocabulary.removeOwnerForResource(peer1,res1);
        networkVocabulary.removeOwnerForResource(peer2,res1);
        Assert.assertEquals(0,networkVocabulary.getResourceOwners(res1).length);
    }
}