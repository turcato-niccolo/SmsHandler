package com.gruppo1.distributednetworkmanager;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.Peer;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.BitSet;

/**
 * @author Niccolo' Turcato
 * Class that represents the Peer Node of DistributedNetwork
 */
class PeerNode extends Peer<BitSet> {
    private Node networkNode;
    private SMSPeer physicalPeer; //might be null depending from the used constructor

    /**
     * Constructor: creates an instance of Node, based on the SMSPeer address
     * Considers all the digits (except the '+') generates the key with the public Hash method
     * @param numBits number of bits which constitute the Key, must be multiple of 64 and >0
     * @param buildingPeer the physical Peer on which this node is based
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     */
    public PeerNode(int numBits, SMSPeer buildingPeer){
        if(buildingPeer.isValid()) {
                networkNode = new Node(numBits, Node.hash(buildingPeer.getAddress(), numBits));
                physicalPeer = buildingPeer;
        }
        else throw new IllegalArgumentException("buildingPeer isn't a valid peer, see SMSPeer.isValid()");
    }

    /**
     * Version of the constructor that initializes the key with the given value
     * @param buildingKey given value for the node's Key
     * @param numBits number of bits that compose the key (must be > length of buildingKey)
     *
     * @throws IllegalArgumentException if the numBit isn't multiple of 64 or numBits <= buildingKey.length()
     */
    public PeerNode(int numBits, @NonNull BitSet buildingKey){
        networkNode = new Node(numBits, buildingKey);
    }

    /**
     * @param peer the peer of which generate hashcode
     * @param numBits number of bits that the hash code will be constituted of, must be multiple of 64 and >0
     *
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     *
     * @return the bitSet containing the hash of the peer's address, bitSet's length is a multiple of 64
     */
    public static BitSet hash(SMSPeer peer, int numBits){
        if(peer.isValid()) {
            return Node.hash(peer.getAddress(), numBits);
        }
        else throw new IllegalArgumentException("buildingPeer isn't a valid peer, see SMSPeer.isValid()");
    }


    /**
     * @return key of the node (hashCode of the building peer calculated with static Hash)
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
    public PeerNode clone() {
        if(physicalPeer != null && physicalPeer.isValid())
            return new PeerNode(keyLength(), physicalPeer);
        else
            return new PeerNode(keyLength(), networkNode.getKey());
    }

    /**
     *
     * @param other peer to confront
     * @return true if this peer and the other are equals, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof PeerNode)
            return this.getAddress().equals(((PeerNode)other).getAddress());
        else return false;
    }

}
