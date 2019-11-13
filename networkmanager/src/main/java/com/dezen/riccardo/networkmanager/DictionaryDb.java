package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.networkmanager.database_dictionary.PeerEntity;
import com.dezen.riccardo.networkmanager.database_dictionary.ResourceEntity;
import com.dezen.riccardo.smshandler.Peer;

public interface DictionaryDb<P extends PeerEntity, R extends ResourceEntity> {
    /**
     * Method to find the Peer that owns a specific Resource
     * @param resource the resource to find
     * @return the Peer owning the parameter resource, null if doesn't exist
     */
    P findPeerWithResource(R resource);

    /**
     * Method to get the Resources associated with a specific Peer
     * @param peer the Peer whose resources the method should return
     * @return array of resources for said peer, null if the peer doesn't exist
     */
    R[] findResourcesForPeer(P peer);

    /**
     * Method returning the peers whose resources can be currently requested
     * @return an array of peers whose resources can be accessed
     */
    P[] getAvailablePeers();

    /**
     * Method returning the available resources in the dictionary
     * @return an array of the resources that can be requested
     */
    R[] getAvailableResource();

    /**
     * Method to add a valid couple Peer-Resource[] to the dictionary
     * @param peer the new valid Peer
     * @param resources the valid Resources the peer owns
     */
    void add(P peer, R[] resources);

    /**
     * Method to remove a Peer along with its Resources
     * @param peer the Peer to remove
     */
    void remove(P peer);
}
