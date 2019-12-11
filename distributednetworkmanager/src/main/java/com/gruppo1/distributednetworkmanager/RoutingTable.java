package com.gruppo1.distributednetworkmanager;

/**
 * Structure for a container that realizes the routing table described by Kademlia P2P algorithm
 * It is built using as base DistributedNetworkNode, the Peer that builds the object. So each peer of the network has its own
 *
 * Given an integer N: number of bit that compose the address space for the network's Nodes
 *
 * The RoutingTable contains N Bucket(of a defined dimension)
 * i: is the index of a Bucket (between 0 and N-1)
 *
 * The bucket of index i contains the Nodes that have distance (ORX metric) >= 2^(N-i+1) <= 2^(N-i)-1
 *
 * i.e.
 *
 * if(i = 0)
 *      bit[N-1] = NOT(myself[N-1]
 *      bit[N-2, 0] = any
 *
 * if(i > 0 && i < N-1)
 *      bit[N-1, N-i] = myself[N-1, N-i]
 *      bit[N-(i+1)] = NOT(myself[N-(i+1)]
 *      bit[N-(i+2), 0] = myself[N-(i+2), 0]
 *
 * if(i = N-1)
 *      the only DistributedNetworkNode that has distance = 1
 *
 * @param <B> type of Bucket used for this structure
 */
public abstract class RoutingTable<B extends Bucket<Node>> {
    /**
     * @param i index of the bucket in buckets container
     * @return the bucket at index i
     */
    public abstract B getBucket(int i);

    /**
     * @param node DistributedNetworkNode of the distributed Network
     * @return the index (between 0 and N -1) of the bucket containing (or that should contain) the given DistributedNetworkNode (Resource or Peer)
     */
    public abstract int getLocation(Node node);

    /**
     * @return true if the node has been added, false otherwise
     */
    public abstract boolean Add(Node node);

    /**
     * @param resourceNode the generic DistributedNetworkNode with the same key as the resource
     * @return the owner of the resource (equal keys) if present, otherwise the closest one
     */
    public abstract Node getOwner(Node resourceNode);

}
