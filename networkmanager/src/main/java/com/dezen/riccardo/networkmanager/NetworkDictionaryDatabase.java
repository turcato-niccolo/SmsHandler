package com.dezen.riccardo.networkmanager;

import android.content.Context;

import androidx.room.Room;

import com.dezen.riccardo.networkmanager.database_dictionary.PeerDatabase;
import com.dezen.riccardo.networkmanager.database_dictionary.PeerEntity;
import com.dezen.riccardo.networkmanager.database_dictionary.ResourceDatabase;
import com.dezen.riccardo.networkmanager.database_dictionary.ResourceEntity;
import com.dezen.riccardo.smshandler.SMSPeer;

import static com.dezen.riccardo.smshandler.SMSHandler.UNREAD_SMS_DATABASE_NAME;

public class NetworkDictionaryDatabase implements Dictionary<SMSPeer, StringResource>{

    private ResourceDatabase resourceDatabase;
    private PeerDatabase peerDatabase;

    public NetworkDictionaryDatabase(Context context) {
        this.resourceDatabase = Room.databaseBuilder(context, ResourceDatabase.class, UNREAD_SMS_DATABASE_NAME)
                .enableMultiInstanceInvalidation()
                .build();
        this.peerDatabase = Room.databaseBuilder(context, PeerDatabase.class, UNREAD_SMS_DATABASE_NAME)
                .enableMultiInstanceInvalidation()
                .build();
    }

    /**
     * Adds a new Peer. Null Peer will not be inserted.
     * @param newPeer the new Peer, whose key does not already exist
     */
    @Override
    public boolean addPeer(SMSPeer newPeer){
        if(newPeer == null || !contains(newPeer)) return false;
        peerDatabase.access().add(new PeerEntity(newPeer.getAddress()));
        return true;
    }

    /**
     * Removes the Peer with the matching key, if it exists. Null Peer will not be removed.
     * @param peerToRemove Peer to remove
     * @return true if it was removed, false otherwise
     */
    @Override
    public boolean removePeer(SMSPeer peerToRemove) {
        if(peerToRemove == null || contains(peerToRemove)) return false;
        peerDatabase.access().remove(new PeerEntity(peerToRemove.getAddress()));
        return true;
    }

    //TODO? update Peer
    @Override
    public boolean updatePeer(SMSPeer updatedPeer) {
        return false;
    }

    //TODO? Query contains in PeerDatabase
    public boolean contains(SMSPeer peer){
        //return peerDatabase.access().contains(peer.getAddress());
        return false;
    }

    /**
     * Returns an array containing all Peers. Peers are not copied singularly.
     * @return a list containing all Peers
     */
    @Override
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
    @Override
    public boolean addResource(StringResource newResource) {
        if(newResource == null || !newResource.isValid() || !contains(newResource)) return false;
        resourceDatabase.access().add(new ResourceEntity(newResource.getName(), newResource.getValue()));
        return true;
    }

    /**
     * Removes the Resource with the matching key, if it exists.
     * Null or invalid Resource won't be searched for.
     * @param resourceToRemove the Resource to remove
     * @return true if it was removed, false otherwise
     */
    @Override
    public boolean removeResource(StringResource resourceToRemove) {
        if(resourceToRemove == null || !resourceToRemove.isValid() || !contains(resourceToRemove))
            return false;
        resourceDatabase.access().remove(new ResourceEntity(resourceToRemove.getName(), resourceToRemove.getValue()));
        return true;
    }

    /**
     * Updates the value of a Resource. Null or invalid Resource won't be updated.
     * Since a matching key is required to update, and being "name" the key for Resources, this
     * method only updates any extra fields the Resource might have besides "peerAddress" itself.
     * @param updatedResource the new value for the Resource
     * @return true if the resource was found and updated, false otherwise.
     */
    @Override
    public boolean updateResource(StringResource updatedResource) {
        if(updatedResource == null || !updatedResource.isValid())
            return false;
        resourceDatabase.access().update(new ResourceEntity(updatedResource.getName(), updatedResource.getName()));
        return true;
    }

    //TODO? implementation of contains in the ResourceDatabase
    public boolean contains(StringResource resource){
        return false;
    }
    /**
     * Returns a copy of the list of all Resources. Resources are not copied singularly.
     * @return a list containing all Resources
     */
    @Override
    public StringResource[] getResources() {
        int numbersResource = resourceDatabase.access().getAll().length;
        StringResource[] resourceArray = new StringResource[numbersResource];
        ResourceEntity[] resourceEntities = resourceDatabase.access().getAll();
        for(int i=0; i<numbersResource; i++)
            resourceArray[i] = new StringResource(resourceEntities[i].keyName, resourceEntities[i].value);
        return resourceArray;
    }


}

