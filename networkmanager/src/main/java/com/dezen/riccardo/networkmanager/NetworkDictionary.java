package com.dezen.riccardo.networkmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implementing Dictionary. Conceived as a double dictionary on SMSPeer and StringResource.
 * Due to trouble with testing android class. Lists have been used.
 * @author Riccardo De Zen, Giorgia Bortoletti
 */
public class NetworkDictionary implements Dictionary<SMSPeer, StringResource> {
    private Map<String, String>  peers;
    private Map<String, String> resources;
    private NetworkDictionaryDatabase database;

    public NetworkDictionary(Context context){
        database = new NetworkDictionaryDatabase(context);
        peers = new HashMap<>();
        resources = new HashMap<>();
    }


    //TODO? make this method asynchronous
    /**
     * Import resources and peers from database to variables Map
     */
    private void importFromDatabase(){
        SMSPeer[] smsPeers = database.getPeers();
        StringResource[] stringResources = database.getResources();

        for(SMSPeer peer : smsPeers)
            peers.put(peer.getAddress(), "");

        for(StringResource resource : stringResources)
            resources.put(resource.getName(), resource.getName());

    }

    //TODO? make this method asynchronous
    /**
     * Export resources and peer from maps to database
     */
    private void exportToDatabase(){

        for(Map.Entry<String, String> peerEntry : peers.entrySet())
        {
            SMSPeer smsPeer = new SMSPeer(peerEntry.getKey());
            if(!database.containsPeer(smsPeer))
            {
                database.addPeer(smsPeer);
            }
        }

        for(Map.Entry<String, String> resourceEntry : resources.entrySet())
        {
            StringResource stringResource = new StringResource(resourceEntry.getKey(), resourceEntry.getValue());
            if(!database.containsResource(stringResource)){
                database.addResource(stringResource);
            }else{
                database.updateResource(stringResource);
            }
        }
    }

    /**
     * Adds a new Peer. Null Peer will not be inserted.
     * @param newPeer the new Peer, whose key does not already exist
     */
    @Override
    public boolean addPeer(SMSPeer newPeer){
        if(newPeer == null || contains(newPeer)) return false;
        peers.put(newPeer.getAddress(), "");
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
        peers.remove(peerToRemove.getAddress());
        return true;
    }

    /**
     * Updates the Peer with the matching key, if it exists.
     * @param updatedPeer the new value for the Peer if one with a matching key exists
     * @return true if the Peer was found and updated, false otherwise
     */
    @Override
    public boolean updatePeer(SMSPeer updatedPeer) {
        if(updatedPeer == null || !contains(updatedPeer)) return false;
        peers.remove(updatedPeer.getAddress());
        peers.put(updatedPeer.getAddress(),"");
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
        for(Map.Entry<String, String> peerEntry : peers.entrySet())
        {
            peerArray[i++] = new SMSPeer(peerEntry.getKey());
        }

        return peerArray;
    }

    /**
     * Returns whether the given user exists in this Vocabulary
     * @param peer the Peer to find
     * @return true if it exists, false otherwise.
     */
    public boolean contains(SMSPeer peer){
        if(peer == null) return false;
        return peers.containsKey(peer.getAddress());
    }

    /**
     * Adds a new resource. Null or invalid Resource won't be added.
     * @param newResource the new Resource, whose key does not already exist
     */
    @Override
    public boolean addResource(StringResource newResource) {
        if(newResource == null || !newResource.isValid() || contains(newResource)) return false;
        resources.put(newResource.getName(), newResource.getValue());
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
        resources.remove(resourceToRemove.getName());
        return true;
    }

    /**
     * Updates the value of a Resource. Null or invalid Resource won't be updated.
     * @param updatedResource the new value for the Resource
     * @return true if the resource was found and updated, false otherwise.
     */
    @Override
    public boolean updateResource(StringResource updatedResource) {
        if(updatedResource == null || !updatedResource.isValid() || !contains(updatedResource))
            return false;
        resources.remove(updatedResource.getName());
        resources.put(updatedResource.getName(), updatedResource.getName());
        return true;
    }

    /**
     * Returns a copy of the list of all Resources. Resources are not copied singularly.
     * @return a list containing all Resources
     */
    @Override
    public StringResource[] getResources() {
        StringResource[] resourcesArray = new StringResource[resources.size()];
        int i = 0;
        for(Map.Entry<String, String> resourceEntry : resources.entrySet())
        {
            resourcesArray[i++] = new StringResource(resourceEntry.getKey(), resourceEntry.getValue());
        }

        return resourcesArray;
    }

    /**
     * Method to tell whether the given Resource exists in this Vocabulary
     * @param resource the Resource to find
     * @return true if resource exists, false otherwise.
     */
    public boolean contains(StringResource resource){
        if(resource == null) return false;
        return resources.containsKey(resource.getName());
    }




}
