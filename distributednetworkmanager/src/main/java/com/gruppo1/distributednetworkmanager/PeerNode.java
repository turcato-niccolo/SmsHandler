package com.gruppo1.distributednetworkmanager;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.Peer;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.BitSet;

/**
 * @author Niccolo' Turcato
 * Class that represents the Peer DistributedNetworkNode of DistributedNetwork
 * Address is a Bitset of fixed length
 */
class PeerNode extends Peer<DistributedNetworkNode> {
    private DistributedNetworkNode networkNode;
    private SMSPeer physicalPeer; //might be null depending from the used constructor

    private static final String NOT_VALID_PEER_EXCEPTION_MSG = "the given building Peer isn't a valid peer, see SMSPeer.isValid()";


    /**
     * Constructor: creates an instance of DistributedNetworkNode, based on the SMSPeer address
     * Considers all the digits (except the '+') generates the key with the public Hash method
     *
     * @param numBits      number of bits which constitute the Key, must be multiple of 64 and >0
     * @param buildingPeer the physical Peer on which this node is based
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     */
    public PeerNode(int numBits, @NonNull SMSPeer buildingPeer) {
        if (buildingPeer.isValid()) {
            networkNode = new DistributedNetworkNode(numBits, DistributedNetworkNode.hash(buildingPeer.getAddress(), numBits));
            physicalPeer = buildingPeer;
        } else throw new IllegalArgumentException();
    }

    /**
     * Version of the constructor that initializes the key with the given value
     *
     * @param buildingKey given value for the node's Key
     * @param numBits     number of bits that compose the key (must be > length of buildingKey)
     * @throws IllegalArgumentException if the numBit isn't multiple of 64 or numBits <= buildingKey.length()
     */
    public PeerNode(int numBits, @NonNull BitSet buildingKey) {
        networkNode = new DistributedNetworkNode(numBits, buildingKey);
    }

    /**
     * @param peer    the peer of which generate hashcode
     * @param numBits number of bits that the hash code will be constituted of, must be multiple of 64 and >0
     * @return the bitSet containing the hash of the peer's address, bitSet's length is a multiple of 64
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     */
    public static BitSet hash(@NonNull SMSPeer peer, int numBits) {
        if (peer.isValid()) {
            return DistributedNetworkNode.hash(peer.getAddress(), numBits);
        } else throw new IllegalArgumentException(NOT_VALID_PEER_EXCEPTION_MSG);
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
    public PeerNode clone() {
        if (physicalPeer != null && physicalPeer.isValid())
            return new PeerNode(keyLength(), physicalPeer);
        else
            return new PeerNode(keyLength(), networkNode.getKey());
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
        if (other instanceof PeerNode)
            return this.getAddress().equals(((PeerNode) other).getAddress());
        else return false;
    }

    /**
     * @return true if this Peer node is to consider Valid, false otherwise
     */
    public boolean isValid() {
        if (physicalPeer != null)
            return physicalPeer.isValid();
        else
            return (networkNode != null);
    }


    /**
     * @return the physical SMSPeer that is contained in the DistributedNetworkNode if available, null if not available
     */
    public SMSPeer getPhysicalPeer() {
        if (physicalPeer != null && physicalPeer.isValid())
            return new SMSPeer(physicalPeer.getAddress());
        return null;
    }
}
