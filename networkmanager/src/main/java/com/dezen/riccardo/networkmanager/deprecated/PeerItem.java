package com.dezen.riccardo.networkmanager.deprecated;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to contain references to Peers and their associated resources
 */
public abstract class PeerItem{
    private SMSPeer peer;
    private List<StringResource> ownedResources;
    PeerItem(@NonNull SMSPeer peer){
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
     * @return true if resource was added, false otherwise
     */
    public boolean addResource(StringResource resource){
        if(!ownsResource(resource)){
            ownedResources.add(resource);
            return true;
        }
        return false;
    }

    /**
     * Method to remove a Resource from the list of owned Resources
     * @param resource the Resource to remove
     * @return true if resource was removed, false otherwise
     */
    public boolean removeResource(StringResource resource){
        if(!ownsResource(resource)) return false;
        ownedResources.remove(resource);
        return true;
    }

    /**
     * Method to check ownership of valid Resource
     * @param resource the Resource to check
     * @return true if "peer" owns the resource, false if not
     */
    public boolean ownsResource(StringResource resource){
        return ownedResources.contains(resource);
    }

    /**
     * Getter for ownedResources
     * @return the array of resources "peer" owns
     */
    public StringResource[] getOwnedResources() {
        StringResource[] ownedResourcesArray = new StringResource[ownedResources.size()];
        ownedResources.toArray(ownedResourcesArray);
        return ownedResourcesArray;
    }

    /**
     * Two PeerItems are equal if their "peer" field is.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof PeerItem){
            return this.peer.equals(((PeerItem) obj).peer);
        }
        else return false;
    }
}