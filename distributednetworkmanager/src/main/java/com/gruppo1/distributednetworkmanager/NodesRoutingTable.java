package com.gruppo1.distributednetworkmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends Rounting Table with KBucket that contains PeerNode
 * Size of RoutingTable is fixed and equals to nodeOwner's key length
 * @author Giorgia Bortoletti
 */
public class NodesRoutingTable extends RoutingTable<KBucket> {

    private List<KBucket> bucketsTable;
    private int sizeTable;
    private Node nodeOwner;

    /**
     * Constructor where the routing table lenght is equal to peer owner length
     * @param nodeOwner
     */
    public NodesRoutingTable(Node nodeOwner){
        new NodesRoutingTable(nodeOwner, nodeOwner.keyLength());
    }

    /**
     * Constructor where the routing table lenght is sizeTable
     * @param nodeOwner
     * @param sizeTable dimension of routing table
     */
    public NodesRoutingTable(Node nodeOwner, int sizeTable){
        this.nodeOwner = nodeOwner;
        this.sizeTable = sizeTable;
        bucketsTable = new ArrayList<>(sizeTable);
        for(int i=0; i<sizeTable; i++)
            bucketsTable.add(new KBucket(sizeTable));
    }

    /**
     * @return nodeOwner of this routing table
     */
    public Node getNodeOwner(){ return nodeOwner; }


    @Override
    public boolean add(Node node) {
        if(node == null)
            return false;
        return bucketsTable.get(getLocation(node)).add(node);
    }

    @Override
    public boolean remove(Node node) {
        if(node == null)
            return false;
        int distance = nodeOwner.getDistanceInteger(node);
        KBucket bucketFound = findBucket(distance); //bucket where should be the node
        if(bucketFound == null)
            return false;
        return bucketFound.remove(node);
    }

    @Override
    public boolean contains(Node node) {
        if(node == null || bucketsTable.size() == 0)
            return false;
        int distance = nodeOwner.getDistanceInteger(node);
        KBucket bucketFound = findBucket(distance); //bucket where should be the node
        if(bucketFound == null)
            return false;
        return bucketFound.contains(node);
    }

    /**
     * @param i index of the bucket in buckets container to return
     * @return bucker at position i if it exists, null otherwise
     * @throws IndexOutOfBoundsException if the position i is not valid
     */
    @Override
    public KBucket getBucket(int i) {
        try {
            return bucketsTable.get(i);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    @Override
    public int getLocation(Node node) {
        if(node == null)
            return -1;
        int distance = nodeOwner.getDistanceInteger(node);
        for(int i=0; i<sizeTable; i++){
            int min = (int)Math.pow(2,sizeTable-i-1);
            int max = (int)Math.pow(2,sizeTable-i)-1;
            if(distance >= min && distance <= max)
                return i;
        }
        return -1;
    }

    @Override
    public Node getClosest(Node node) {
        if(!node.equals(nodeOwner)) {
            Node[] nodesClosest = getKClosest(node);
            if (nodesClosest != null){
                int minDistance = sizeTable * 2;
                Node nodeClosest = null;
                for (int i = 0; i < nodesClosest.length; i++) {
                    int distance = node.getDistanceInteger(nodesClosest[i]);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nodeClosest = nodesClosest[i];
                    }
                }
                return nodeClosest;
            }
        }
        return null;
    }

    @Override
    public Node[] getKClosest(Node node) {
        if(!node.equals(nodeOwner)) {
            int position = getLocation(node); //the higher the position, the closer it is
            for (int i = position; i >= 0; i--) { //moves away but it looks for a bucket with some nodes
                Node[] nodesClosest = bucketsTable.get(i).getElements();
                if (nodesClosest.length >= 1)
                    return nodesClosest;
            }
        }
        return null;
    }

    /**
     * This method is used to find which is the bucket of a Node with the distance from peer owner to Node
     * @param distanceFromnodeOwner
     * @return KBucket which maybe contains the Node, null otherwise
     */
    private KBucket findBucket(int distanceFromnodeOwner){
        for(int i=0; i<sizeTable; i++){
            int min = (int)Math.pow(2,sizeTable-i-1);
            int max = (int)Math.pow(2,sizeTable-i)-1;
            if(distanceFromnodeOwner >= min && distanceFromnodeOwner <= max)
            {
                return bucketsTable.get(i);
            }
        }
        return null;
    }


}
