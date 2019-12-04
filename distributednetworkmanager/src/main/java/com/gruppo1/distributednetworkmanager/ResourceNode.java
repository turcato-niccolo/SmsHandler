package com.gruppo1.distributednetworkmanager;

import androidx.annotation.NonNull;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.networkmanager.StringResource;
import java.util.BitSet;

/**
 * @author Niccolo' Turcato
 * Class that represents a Resource of DistributedNetwork
 */
public class ResourceNode implements Resource<String, String> {
    private StringResource physicalResource; //might be null based on the used Constructor
    private Node networkNode;

    /**
     * Constructor: creates an instance of Node, based on the StringResource name
     * Stores the entire object StringResource
     * @param numBits number of bits which constitute the Key, must be multiple of 64 and >0
     * @param buildingResource the physical Peer on which this node is based
     * @throws IllegalArgumentException if resource isn't valid or the numBit isn't multiple of 64
     */
    public ResourceNode(int numBits, StringResource buildingResource){
        if(buildingResource.isValid()){
            networkNode = new Node(numBits, Node.hash(buildingResource.getName(), numBits));
            physicalResource = buildingResource;
        }
        else throw new IllegalArgumentException("buildingResource isn't a valid peer, see StringResource.isValid()");
    }


    /**
     * Version of the constructor that initializes the key with the given value
     * @param buildingKey given value for the node's Key
     * @param numBits number of bits that compose the key (must be > length of buildingKey)
     *
     * @throws IllegalArgumentException if the numBit isn't multiple of 64 or numBits <= buildingKey.length()
     */
    public ResourceNode(int numBits, @NonNull BitSet buildingKey){
        networkNode = new Node(numBits, buildingKey);
    }

    /**
     * @param resource the peer of which generate hashcode
     * @param numBits number of bits that the hash code will be constituted of, must be multiple of 64 and >0
     *
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     *
     * @return the bitSet containing the hash of the resource's name, bitSet's length is a multiple of 64
     */
    public static BitSet hash(StringResource resource, int numBits){
        if(resource.isValid()) {
            return Node.hash(resource.getName(), numBits);
        }
        else throw new IllegalArgumentException("Resource isn't a valid peer, see SMSPeer.isValid()");
    }

    /**
     * @return the resource's Name if available, null if not
     */
    public String getName(){
        if(physicalResource != null && physicalResource.isValid())
            return physicalResource.getName();
        return null;
    }

    /**
     * @return the resource's value if available, null if not
     */
    public String getValue(){
        if(physicalResource != null && physicalResource.isValid())
            return physicalResource.getValue();
        return null;
    }

    /**
     * @return key of the node (hashCode of the building Resource calculated with static Hash)
     */
    public BitSet getAddress() {
        return networkNode.getKey();
    }

    /**
     * @return number of bits that compose the BitSet key
     */
    public int keyLength(){
        return networkNode.keyLength();
    }

    /**
     * @param other the Node for which calculate distance from this Node
     * @return The distance of the Keys, calculated as XOR of BitSets
     */
    public BitSet distanceFrom(PeerNode other){
        return networkNode.distanceFrom(new Node(other.keyLength(), other.getAddress()));
    }

    /**
     * @param other the Node for which calculate distance from this Node
     * @return The distance of the Keys, calculated as XOR of BitSets
     */
    public BitSet distanceFrom(ResourceNode other){
        return networkNode.distanceFrom(new Node(other.keyLength(), other.getAddress()));
    }

    /**
     * @return a new PeerNode equal to this
     */
    @Override
    public ResourceNode clone() {
        if(physicalResource != null && physicalResource.isValid())
            return new ResourceNode(keyLength(), physicalResource);
        else
            return new ResourceNode(keyLength(), networkNode.getKey());
    }

    /**
     *
     * @param other peer to confront
     * @return true if this peer and the other are equals, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other == null)
            return false;
        if(this == other)
            return true;
        if(other instanceof ResourceNode)
            return this.getAddress().equals(((ResourceNode)other).getAddress());
        return false;
    }

}
