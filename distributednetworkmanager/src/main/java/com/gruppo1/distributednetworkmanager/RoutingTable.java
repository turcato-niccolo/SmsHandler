package com.gruppo1.distributednetworkmanager;

/**
 * Structure for a container that realizes the routing table described by Kademlia P2P algorithm
 * It is built using as base Node, the Peer that builds the object. So each peer of the network has its own
 *
 * Given an integer N: number of bit that compose the address space for the network's Nodes
 *
 * The RoutingTable contains N Bucket(of a defined dimension)
 * i: is the index of a Bucket (between 0 and N-1)
 *
 * The bucket of index i contains the Nodes that have distance (ORX metric) >= 2^(N-i-1) <= 2^(N-i)-1
 *
 * i.e.
 *
 * if(i = 0)
 *      bit[0] = NOT(myself[0])
 *      bit[0, N-1] = any
 *
 * if(i > 0 && i < N-1)
 *      bit[N-1, N-i] = myself[N-1, N-i]
 *      bit[N-(i+1)] = NOT(myself[N-(i+1)]
 *      bit[N-(i+2), 0] = myself[N-(i+2), 0]
 *
 * if(i = N-1)
 *      bit[N-1] = NOT(myself[0])
 *      bit[0, N-1] = myself[0, N-1]
 *      the only Node that has distance = 1
 *
 * @param <B> type of Bucket used for this structure
 */
public abstract class RoutingTable<B extends Bucket<Node<BinarySet>>> {
    /**
     * @param i index of the bucket in buckets container
     * @return the bucket at index i
     */
    public abstract B getBucket(int i);

    /**
     * @param node Node of the distributed Network
     * @return the index (between 0 and N -1) of the bucket that maybe containing the given Node (Resource or Peer), -1 otherwise
     */
    public abstract int getLocation(PeerNode node);

    /**
     * @return true if the node has been added, false otherwise
     */
    public abstract boolean add(PeerNode node);

    /**
     * @return the closest Node at the ownerNode in the rt if present, otherwise null
     */
    public abstract Node getClosest();

    /**
     * @return the closest K Nodes at the ownerNode in the rt if present, otherwise null
     */
    public abstract Node[] getKClosest();

    /**
     * @param node node of which check presence in the RT
     * @return true if present, false otherwise
     */
    public abstract boolean contains(PeerNode node);

    /**
     * @param node Node to remove form the RT
     */
    public abstract boolean remove(PeerNode node);

}
