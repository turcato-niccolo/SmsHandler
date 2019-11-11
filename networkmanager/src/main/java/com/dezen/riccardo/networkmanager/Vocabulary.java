package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.Peer;

import java.util.List;

/**
 * The Address of a Peer is used as it's key
 * @param <P> Peer type
 * The Name of a Resource is used as it's key
 * @param <R> Resource type
 */
public interface Vocabulary<P extends Peer, R extends Resource>{
    /**
     * Adds a new Peer
     * @param newPeer the new Peer, whose key does not already exist
     * @return true if the Peer was added, false otherwise
     */
    boolean addPeer(P newPeer);

    /**
     * Removes the Peer with the matching key, if it exists
     * @param peerToRemove Peer to remove
     * @return true if it was removed, false otherwise
     */
    boolean removePeer(P peerToRemove);

    /**
     * Updates the Peer with the matching key, if it exists
     * @param updatedPeer the new value for the Peer if one with a matching key exists
     * @return the old value for the Peer, null if it didn't exist
     */
    P updatePeer(P updatedPeer);

    /**
     * Returns a copy of the list of all Peers. Peers are not copied singularly.
     * @return a list containing all Peers
     */
    List<P> getPeers();

    /**
     * Adds a new resource
     * @param newResource the new Resource, whose key does not already exist
     * @return true if the Resource was added, false otherwise
     */
    boolean addResource(R newResource);

    /**
     * Removes the Resource with the matching key, if it exists
     * @param resourceToRemove the Resource to remove
     * @return true if it was removed, false otherwise
     */
    boolean removeResource(R resourceToRemove);

    /**
     * Updates the value of a Resource
     * @param updatedResource the new value for the Resource
     * @return the old value for the resource, null if it doesn't exist
     */
    R updateResource(R updatedResource);

    /**
     * Returns a copy of the list of all Resources. Resources are not copied singularly.
     * @return a list containing all Resources
     */
    List<R> getResources();
}
