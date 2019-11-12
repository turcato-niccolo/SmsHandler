package com.dezen.riccardo.networkmanager;

import androidx.annotation.Nullable;

import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.ArrayList;
import java.util.List;

public class NetworkVocabulary implements Vocabulary<SMSPeer, StringResource>{
    private List<PeerItem> peers;
    private List<StringResource> resources;

    public NetworkVocabulary(){
        peers = new ArrayList<>();
        resources = new ArrayList<>();
    }

    /**
     * Class used to contain references to Peers and their associated resources
     */
    private class PeerItem{
        private SMSPeer peer;
        private List<StringResource> ownedResources;
        PeerItem(SMSPeer peer){
            this.peer = peer;
            ownedResources = new ArrayList<>();
        }

        /**
         * @return the Peer for this item
         */
        public SMSPeer getPeer(){
            return peer;
        }

        /**
         * Method to update the value of this PeerItems's Peer.
         * Peer key is considered unique, so it must be equal for field "peer" and parameter "newPeer".
         * @param newPeer the new valid value for the Peer
         */
        public void setPeer(SMSPeer newPeer){
            this.peer = newPeer;
        }

        /**
         * Method to add a Resource to the list of Resources owned by the Peer if not owned already
         * @param resource the Resource to add
         */
        public void addResource(StringResource resource){
            ownedResources.add(resource);
        }

        /**
         * Method to remove a Resource from the list of owned Resources
         * @param resource the Resource to remove
         */
        public void removeResource(StringResource resource){
            ownedResources.remove(resource);
        }

        /**
         * Getter for ownedResources
         * @return the list of resources peer owns
         */
        public List<StringResource> getOwnedResources() {
            return ownedResources;
        }

        /**
         * Two PeerItems are equal if their "peer" field is.
         */
        @Override
        public boolean equals(@Nullable Object obj) {
            if(obj instanceof PeerItem){
                return peer.equals(((PeerItem) obj).peer);
            }
            else return false;
        }
    }

    /**
     * Adds a new Peer. Null Peer will not be inserted.
     * @param newPeer the new Peer, whose key does not already exist
     */
    @Override
    public boolean addPeer(SMSPeer newPeer){
        if(newPeer == null || contains(newPeer)) return false;
        peers.add(new PeerItem(newPeer));
        return true;
    }

    /**
     * Removes the Peer with the matching key, if it exists. Null Peer will not be removed.
     * @param peerToRemove Peer to remove
     * @return true if it was removed, false otherwise
     */
    @Override
    public boolean removePeer(SMSPeer peerToRemove) {
        if(peerToRemove == null || !contains(peerToRemove)) return false;
        peers.remove(new PeerItem(peerToRemove));
        return true;
    }

    /**
     * Updates extra info of the Peer with the matching key, if it exists. Null Peer will not be updated.
     * @param updatedPeer the new value for the Peer if one with a matching key exists
     * @return true if the Peer was updated, false if it didn't exist
     */
    @Override
    public boolean updatePeer(SMSPeer updatedPeer) {
        if(updatedPeer == null || !contains(updatedPeer)) return false;
        //We find the PeerItem whose key matches "updatedPeer" to change it's "peer" field but not
        //its resources.
        peers.get(peers.indexOf(new PeerItem(updatedPeer))).setPeer(updatedPeer);
        return true;
    }

    /**
     * Returns an array containing all Peers. Peers are not copied singularly.
     * @return a list containing all Peers
     */
    @Override
    public SMSPeer[] getPeers() {
        SMSPeer[] peerArray = new SMSPeer[peers.size()];
        int i = 0;
        for(PeerItem peerItem : peers) peerArray[i++] = peerItem.getPeer();
        return peerArray;
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
    public boolean updateResource(StringResource updatedResource) {
        return false;
    }

    /**
     * Returns a copy of the list of all Resources. Resources are not copied singularly.
     * @return a list containing all Resources
     */
    @Override
    public StringResource[] getResources() {
        return null;
    }

    //TODO evaluate advantages of directly overriding equals inside PeerItem

    /**
     * Returns whether the given user exists in this Vocabulary
     * @param peer the Peer to find
     * @return true if it exists (an instance of PeerItem exists whose "peer" value equals the "peer"
     * passed as parameter), false otherwise.
     */
    public boolean contains(SMSPeer peer){
        PeerItem tempItem = new PeerItem(peer);
        return peers.contains(tempItem);
    }

    /**
     * Method to tell whether the given Resource exists in this Vocabulary
     * @param resource the Resource to find
     * @return true if resource exists, false otherwise.
     */
    public boolean contains(StringResource resource){
        return resources.contains(resource);
    }
}
