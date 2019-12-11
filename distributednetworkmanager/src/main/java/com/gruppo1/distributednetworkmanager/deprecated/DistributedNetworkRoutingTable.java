package com.gruppo1.distributednetworkmanager.deprecated;

import com.gruppo1.distributednetworkmanager.Node;


public class DistributedNetworkRoutingTable {

    /**
     * @param i index of the bucket in buckets container
     * @return the bucket at index i
     */
   // public Bucket<Node> getBucket(int i) {
        //return new KBucket(128);
   // }

    /**
     * @param node DistributedNetworkNode of the distributed Network
     * @return the index (between 0 and N -1) of the bucket containing (or that should contain) the given DistributedNetworkNode (Resource or Peer)
     */
    public int getLocation(Node node) {
        return 0;
    }

    /**
     * @return true if the node has been added, false otherwise
     */
    public boolean Add(Node node) {
        return true;
    }

    /**
     * @param resourceNode the generic DistributedNetworkNode with the same key as the resource
     * @return the owner of the resource (equal keys) if present, otherwise the closest one
     */
    //public Node getOwner(Node resourceNode) {
      //  return new PeerNode(new BinarySet(BitSetUtils.hash("+39045678", 128)));
    //}

}
