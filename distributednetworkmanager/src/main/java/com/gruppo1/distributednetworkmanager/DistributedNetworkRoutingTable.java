package com.gruppo1.distributednetworkmanager;

public class DistributedNetworkRoutingTable extends RoutingTable<Bucket<Node>> {

    /**
     * @param i index of the bucket in buckets container
     * @return the bucket at index i
     */
    public Bucket<Node> getBucket(int i) {
        return new KBucket(128);
    }

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

    @Override
    public boolean contains(Node node) {
        return false;
    }

    @Override
    public void remove(Node node) {

    }

    @Override
    public Node getClosest(Node node) {
        return null;
    }

    @Override
    public Node[] getKClosest(Node node) {
        return new Node[0];
    }
}
