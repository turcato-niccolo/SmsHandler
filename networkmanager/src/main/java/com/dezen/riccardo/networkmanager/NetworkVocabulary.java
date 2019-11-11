package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.ArrayList;
import java.util.List;

public class NetworkVocabulary implements Vocabulary<SMSPeer, StringResource>{
    private List<SMSPeer> peers;
    private List<StringResource> resources;

    public NetworkVocabulary(){
        peers = new ArrayList<>();
        resources = new ArrayList<>();
    }

    /**
     * Adds a new Peer. Null Peer will not be inserted.
     * @param newPeer the new Peer, whose key does not already exist
     */
    @Override
    public boolean addPeer(SMSPeer newPeer){
        if(newPeer == null || peers.contains(newPeer)) return false;
        peers.add(newPeer);
        return true;
    }

    /**
     * Removes the Peer with the matching key, if it exists. Null Peer will not be removed.
     * @param peerToRemove Peer to remove
     * @return true if it was removed, false otherwise
     */
    @Override
    public boolean removePeer(SMSPeer peerToRemove) {
        if(peerToRemove == null || !peers.contains(peerToRemove)) return false;
        peers.remove(peerToRemove);
        return true;
    }

    /**
     * Updates the Peer with the matching key, if it exists. Null Peer will not be updated.
     * @param updatedPeer the new value for the Peer if one with a matching key exists
     * @return the old value for the Peer, null if it didn't exist
     */
    @Override
    public SMSPeer updatePeer(SMSPeer updatedPeer) {
        if(updatedPeer == null || !peers.contains(updatedPeer)) return null;
        int peerIndex = peers.indexOf(updatedPeer);
        SMSPeer oldPeer = peers.get(peerIndex);
        peers.set(peerIndex, updatedPeer);
        return oldPeer;
    }

    /**
     * Returns a copy of the list of all Peers. Peers are not copied singularly.
     * @return a list containing all Peers
     */
    @Override
    public List<SMSPeer> getPeers() {
        return new ArrayList<>(peers);
    }

    //TODO Resources management
    /**
     * Adds a new resource. Null Resource won't be added.
     * @param newResource the new Resource, whose key does not already exist
     */
    @Override
    public boolean addResource(StringResource newResource) {
        return false;
    }

    /**
     * Removes the Resource with the matching key, if it exists. Null Resource won't be searched for.
     * @param resourceToRemove the Resource to remove
     * @return true if it was removed, false otherwise
     */
    @Override
    public boolean removeResource(StringResource resourceToRemove) {
        return false;
    }

    /**
     * Updates the value of a Resource. Null Resource won't be updated.
     * @param updatedResource the new value for the Resource
     * @return the old value for the resource, null if it doesn't exist
     */
    @Override
    public StringResource updateResource(StringResource updatedResource) {
        return null;
    }

    /**
     * Returns a copy of the list of all Resources. Resources are not copied singularly.
     * @return a list containing all Resources
     */
    @Override
    public List<StringResource> getResources() {
        return null;
    }
}
