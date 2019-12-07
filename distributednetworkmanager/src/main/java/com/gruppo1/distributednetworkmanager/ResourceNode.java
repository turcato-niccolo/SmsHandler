package com.gruppo1.distributednetworkmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.networkmanager.StringResource;

import java.util.BitSet;

/**
 * @author Niccolo' Turcato
 * Class that represents a Resource of DistributedNetwork
 */
public class ResourceNode extends Resource<String, String> {
    private StringResource physicalResource; //might be null based on the used Constructor
    private DistributedNetworkNode networkNode;

    /**
     * Constructor: creates an instance of DistributedNetworkNode, based on the StringResource name
     * Stores the entire object StringResource
     *
     * @param numBits          number of bits which constitute the Key, must be multiple of 64 and >0
     * @param buildingResource the physical Peer on which this node is based
     * @throws IllegalArgumentException if resource isn't valid or the numBit isn't multiple of 64
     */
    public ResourceNode(int numBits, StringResource buildingResource) {
        if (buildingResource.isValid()) {
            networkNode = new DistributedNetworkNode(numBits, DistributedNetworkNode.hash(buildingResource.getName(), numBits));
            physicalResource = buildingResource;
        } else
            throw new IllegalArgumentException("buildingResource isn't a valid peer, see StringResource.isValid()");
    }


    /**
     * Version of the constructor that initializes the key with the given value
     *
     * @param buildingKey given value for the node's Key
     * @param numBits     number of bits that compose the key (must be > length of buildingKey)
     * @throws IllegalArgumentException if the numBit isn't multiple of 64 or numBits <= buildingKey.length()
     */
    public ResourceNode(int numBits, @NonNull BitSet buildingKey) {
        networkNode = new DistributedNetworkNode(numBits, buildingKey);
    }

    /**
     * @param resource the peer of which generate hashcode
     * @param numBits  number of bits that the hash code will be constituted of, must be multiple of 64 and >0
     * @return the bitSet containing the hash of the resource's name, bitSet's length is a multiple of 64
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     */
    public static BitSet hash(StringResource resource, int numBits) {
        if (resource.isValid()) {
            return DistributedNetworkNode.hash(resource.getName(), numBits);
        } else
            throw new IllegalArgumentException("Resource isn't a valid peer, see SMSPeer.isValid()");
    }

    /**
     * @return the resource's Name if available, null if not
     */
    public String getName() {
        if (physicalResource != null && physicalResource.isValid())
            return physicalResource.getName();
        return null;
    }

    /**
     * @return the resource's value if available, null if not
     */
    public String getValue() {
        if (physicalResource != null && physicalResource.isValid())
            return physicalResource.getValue();
        return null;
    }

    /**
     * @return Generic DistributedNetworkNode of the network that has same key as this PeerNode
     */
    public DistributedNetworkNode getAddress() {
        return networkNode.clone();
    }

    /**
     * @return number of bits that compose the BitSet key
     */
    public int keyLength() {
        return networkNode.keyLength();
    }

    /**
     * @param other the DistributedNetworkNode for which calculate distance from this DistributedNetworkNode
     * @return The distance of the Keys, calculated as XOR of BitSets
     * @throws IllegalArgumentException if the given peerNode isn't part of this node's address space
     */
    public BitSet distanceFrom(@NonNull PeerNode other) {
        return networkNode.distanceFrom(new DistributedNetworkNode(other.keyLength(), other.getAddress().getKey()));
    }

    /**
     * @param other the DistributedNetworkNode for which calculate distance from this DistributedNetworkNode
     * @return The distance of the Keys, calculated as XOR of BitSets
     * @throws IllegalArgumentException if the given peerNode isn't part of this node's address space
     */
    public BitSet distanceFrom(@NonNull ResourceNode other) {
        return networkNode.distanceFrom(new DistributedNetworkNode(other.keyLength(), other.getAddress().getKey()));
    }

    /**
     * @return a new PeerNode equal to this
     */
    @Override
    public ResourceNode clone() {
        if (physicalResource != null && physicalResource.isValid())
            return new ResourceNode(keyLength(), physicalResource);
        else
            return new ResourceNode(keyLength(), networkNode.getKey());
    }

    /**
     * @param other peer to confront
     * @return true if this peer and the other are equals, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (this == other)
            return true;
        if (other instanceof ResourceNode)
            return this.getAddress().equals(((ResourceNode) other).getAddress());
        return false;
    }

    /**
     * @return true if this resource is to consider Valid, false otherwise
     */
    public boolean isValid() {
        if (physicalResource != null)
            return physicalResource.isValid();
        else
            return (networkNode != null);
    }

    /**
     * Not implemented
     *
     * @return an empty Bundle, not yet implemented
     */
    public Bundle getExtras() {
        return new Bundle();
    }

}
