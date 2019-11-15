package com.dezen.riccardo.networkmanager;

import android.content.Context;

import androidx.room.Room;

import com.dezen.riccardo.networkmanager.database_dictionary.PeerDatabase;
import com.dezen.riccardo.networkmanager.database_dictionary.PeerEntity;
import com.dezen.riccardo.networkmanager.database_dictionary.ResourceDatabase;
import com.dezen.riccardo.networkmanager.database_dictionary.ResourceEntity;
import com.dezen.riccardo.smshandler.SMSPeer;

import static com.dezen.riccardo.smshandler.SmsHandler.SMS_HANDLER_LOCAL_DATABASE;

/**
 * @author Giorgia Bortoletti
 */
public class NetworkDictionaryDatabase{

    private ResourceDatabase resourceDatabase;
    private PeerDatabase peerDatabase;

    public NetworkDictionaryDatabase(Context context) {
        this.resourceDatabase = Room.databaseBuilder(context, ResourceDatabase.class, SMS_HANDLER_LOCAL_DATABASE)
                .enableMultiInstanceInvalidation()
                .build();
        this.peerDatabase = Room.databaseBuilder(context, PeerDatabase.class, SMS_HANDLER_LOCAL_DATABASE)
                .enableMultiInstanceInvalidation()
                .build();
    }

    /**
     * Adds a new Peer. Null Peer will not be inserted.
     * @param newPeer the new Peer, whose key does not already exist
     */
    public void addPeer(SMSPeer newPeer){
        peerDatabase.access().add(new PeerEntity(newPeer.getAddress()));
    }

    /**
     * Removes the Peer with the matching key, if it exists. Null Peer will not be removed.
     * @param peerToRemove Peer to remove
     */
    public void removePeer(SMSPeer peerToRemove) {
        peerDatabase.access().remove(new PeerEntity(peerToRemove.getAddress()));
    }

    /**
     * Updates the value of a Peer. Null or invalid Resource won't be updated.
     * @param updatedPeer the new value for the Peer
     */
    public void updatePeer(SMSPeer updatedPeer) {
        peerDatabase.access().update(new PeerEntity(updatedPeer.getAddress()));
    }

    /**
     * Verify if a Peer is in the database
     * @param searchPeer the new value for the Peer
     * @return true if the peer was found
     */
    public boolean containsPeer(SMSPeer searchPeer) {
        return peerDatabase.access().contains(searchPeer.getAddress());
    }

    /**
     * Returns an array containing all Peers. Peers are not copied singularly.
     * @return a list containing all Peers
     */
    public SMSPeer[] getPeers() {
        int numbersPeer = peerDatabase.access().getAll().length;
        SMSPeer[] peerArray = new SMSPeer[numbersPeer];
        PeerEntity[] peerEntities = peerDatabase.access().getAll();
        for(int i=0; i<numbersPeer; i++)
            peerArray[i] = new SMSPeer(peerEntities[i].address);
        return peerArray;
    }

    /**
     * Adds a new resource. Null or invalid Resource won't be added.
     * @param newResource the new Resource, whose key does not already exist
     */
    public void addResource(StringResource newResource) {
        resourceDatabase.access().add(new ResourceEntity(newResource.getName(), newResource.getValue()));
    }

    /**
     * Removes the Resource with the matching key, if it exists.
     * Null or invalid Resource won't be searched for.
     * @param resourceToRemove the Resource to remove
     */
    public void removeResource(StringResource resourceToRemove) {
        resourceDatabase.access().remove(new ResourceEntity(resourceToRemove.getName(), resourceToRemove.getValue()));
    }

    /**
     * Updates the value of a Resource. Null or invalid Resource won't be updated.
     * Since a matching key is required to update, and being "name" the key for Resources, this
     * method only updates any extra fields the Resource might have besides "peerAddress" itself.
     * @param updatedResource the new value for the Resource
     * @return true if the resource was found and updated, false otherwise.
     */
    public void updateResource(StringResource updatedResource) {
        resourceDatabase.access().update(new ResourceEntity(updatedResource.getName(), updatedResource.getName()));
    }

    /**
     * Returns an array containing all Resources. Resources are not copied singularly.
     * @return a list containing all Resources
     */
    public StringResource[] getResources() {
        int numbersResource = resourceDatabase.access().getAll().length;
        StringResource[] resourceArray = new StringResource[numbersResource];
        ResourceEntity[] resourceEntities = resourceDatabase.access().getAll();
        for(int i=0; i<numbersResource; i++)
            resourceArray[i] = new StringResource(resourceEntities[i].keyName, resourceEntities[i].value);
        return resourceArray;
    }

    /**
     * Verify if a Resource is in the database
     * @param searchResource the new value for the Peer
     * @return true if the Resource was found
     */
    public boolean containsResource(StringResource searchResource) {
        return peerDatabase.access().contains(searchResource.getName());
    }


}

