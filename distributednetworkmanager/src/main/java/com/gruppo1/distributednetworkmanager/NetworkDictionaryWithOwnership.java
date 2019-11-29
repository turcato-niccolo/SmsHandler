package com.gruppo1.distributednetworkmanager;

import android.content.Context;

import com.dezen.riccardo.networkmanager.NetworkDictionary;
import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkDictionaryWithOwnership extends NetworkDictionary {

    private Map<StringResource, SMSPeer[]> ownership;
    static final SMSPeer NON_EXISTENT_OWNER = new SMSPeer("NON_EXISTENT_OWNER_ADDRESS");
    //Watch out this is not a valid Peer for (SMSPeer.isValid())

    /**
     * Constructor, initializes the object and defines the backup database
     * @param context app's context, used to define backup database
     */
    public NetworkDictionaryWithOwnership(Context context){
        super(context);
        ownership = new HashMap<>();
    }

    /**
     *
     * @param peerToRemove Peer to remove
     * @return true if the peer and its ownership have been removed
     */
    @Override
    public boolean removePeer(SMSPeer peerToRemove) {
        //TODO
        return true;
    }

    /**
     *
     * @param peerToRemove the peer that has to be removed
     * @param newOwnerOfOrphanResources the peer that will inherit the removed one's resources
     * @return true if the peer has been removed and the ownership of his resources has been moved
     */
    public boolean removePeer(SMSPeer peerToRemove, SMSPeer newOwnerOfOrphanResources) {
        //TODO
        return true;
    }


    /**
     * Adds a new resource without an owner. Null or invalid Resource won't be added.
     * @param newResource the new Resource, whose key does not already exist
     * @return true if it is added, false if already present o not added.
     */
    @Override
    public boolean addResource(StringResource newResource)
    {
        if(!super.contains(newResource)) {
            super.addResource(newResource);
            ownership.put(newResource, new SMSPeer[]{NON_EXISTENT_OWNER});
            return super.contains(newResource) && verifyUnexistentOwnership(newResource);
        }
        else return false;
    }

    /**
     *Adds a new resource with an owner. Null or invalid Resource won't be added.
     * @param newResource resource to be added
     * @param ownerPeer the specified owner (must already be present in Dictionary)
     * @return true if the resource has been added, false if it was already present, or the indicated user wasn't valid or not present
     */
    public boolean addResource(StringResource newResource, SMSPeer ownerPeer)
    {
        if(newResource != null && !super.contains(newResource) && ownerPeer.isValid() && super.contains(ownerPeer)){
            super.addResource(newResource);
            ownership.put(newResource, new SMSPeer[]{ownerPeer});
            return super.contains(newResource);
        }
        else return false;
    }

    /**
     *Adds a new resource with some owners. Null or invalid Resource won't be added.
     * @param newResource resource to be added
     * @param ownerPeers the specified owners (must already be present in Dictionary)
     * @return true if the resource has been added, false if it was already present, or the indicated peer weren't valid or not present
     */
    public boolean addResource(StringResource newResource, SMSPeer[] ownerPeers)
    {
        if(newResource != null && !super.contains(newResource)){
            for (SMSPeer newOwnerPeer: ownerPeers) {
                if(!newOwnerPeer.isValid() || !super.contains(newOwnerPeer))
                    return false;
            }
            super.addResource(newResource);
            ownership.put(newResource, ownerPeers);
            return super.contains(newResource);
        }
        else return false;
    }

    /**
     * @param ownerPeer the peer whose asked ownedResource's ownership, must be a valid SMSPeer
     * @param ownedResource the resource whose asked if it's owned by ownerPeer
     * @return true if ownerPeer really owns ownedResource, false if the peer is not present in Dictionary or doesn't own the resource or resource is not present in Dictionary
     */
    public boolean verifyOwnership(SMSPeer ownerPeer, StringResource ownedResource)
    {
        if(ownerPeer.isValid() && super.contains(ownerPeer) && super.contains(ownedResource)){
            return getResourcesOwnedByPeer(ownerPeer).contains(ownedResource);
        }
        return false;
    }

    /**
     *
     * @param newOwnerPeer the peer that we want to own the given resource, must be present in the dictionary
     * @param ownedResource the resource that becomes owned by given peer, must be present in the dictionary
     * @return true if the peer has been granted with the resource's ownership, false otherwise
     */
    public boolean addOwnerToResource(SMSPeer newOwnerPeer, StringResource ownedResource)
    {
        if(newOwnerPeer != null && newOwnerPeer.isValid() && super.contains(ownedResource)){
            for (Map.Entry<StringResource, SMSPeer[]> ownershipEntry: ownership.entrySet()) {
                if(ownershipEntry.getKey().equals(ownedResource)){
                    SMSPeer[] peers = ownershipEntry.getValue();
                    if(peers.length == 1 && peers[0].equals(NON_EXISTENT_OWNER))
                        //this is the case for resources that don't have an owner
                        peers[0] = newOwnerPeer;
                    else
                    {   //the resource has at least one owner
                        SMSPeer[] newPeers = new SMSPeer[peers.length+1];
                        System.arraycopy(peers, 0, newPeers, 0, peers.length);
                        newPeers[newPeers.length-1] = newOwnerPeer;
                        ownershipEntry.setValue(newPeers);
                    }
                    return verifyOwnership(newOwnerPeer, ownedResource);
                }
            }
        }
        return false;
    }

    /**
     *
     * @param orphanResource the resource for which we want to check if it does not have and owner
     * @return true if the resource doesn't have an owner, false if the Dictionary doesn't contain the resource
     */
    public boolean verifyUnexistentOwnership(StringResource orphanResource)
    {
        if(super.contains(orphanResource)) {
            return getResourcesOwnedByPeer(NON_EXISTENT_OWNER).contains(orphanResource);
        }
        else return false;
    }


    /**
     *
     * @param wantedOwnerPeer the peer whose asked which resources it owns, must be a valid SMSPeer
     * @return
     */
    public ArrayList<StringResource> getResourcesOwnedByPeer(SMSPeer wantedOwnerPeer)
    {
        ArrayList<StringResource> ownedResourcesList = new ArrayList<>();
        for (Map.Entry<StringResource, SMSPeer[]> ownershipEntry: ownership.entrySet()) {
            SMSPeer[] research = ownershipEntry.getValue();
            for (SMSPeer owner: research) {
                if(owner.equals(wantedOwnerPeer))
                    ownedResourcesList.add(ownershipEntry.getKey());
            }
        }
        return ownedResourcesList;
    }

}
