package com.gruppo1.distributednetworkmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends Rounting Table with KBucket that contains Node
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

    /**
     * @param node Node to add
     * @return true if the node has been added, false otherwise
     */
    @Override
    public boolean add(Node node) {
        if(node == null)
            return false;
        return bucketsTable.get(getLocation(node)).add(node);
    }

    /**
     * @param node Node to remove
     * @return true if the node has been removed, false otherwise
     */
    @Override
    public boolean remove(Node node) {
        if(node == null)
            return false;
        int positionNode = getLocation(node);
        KBucket bucketFound = bucketsTable.get(positionNode);
        if(!bucketFound.contains(node))
            return false;
        return bucketFound.remove(node);
    }

    /**
     * @param node Node of which check presence
     * @return true if present, false otherwise
     */
    @Override
    public boolean contains(Node node) {
        if(node == null || bucketsTable.size() == 0)
            return false;
        int positionNode = getLocation(node);
        if(positionNode == -1)
            return false;
        return bucketsTable.get(positionNode).contains(node);
    }

    /**
     * @param i index of the bucket in buckets container
     * @return the bucket at index i
     */
    @Override
    public KBucket getBucket(int i) {
        try {
            return bucketsTable.get(i);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * @param node Node of the distributed Network
     * @return the index (between 0 and N -1) of the bucket that maybe containing the given Node with BinarySet, -1 otherwise
     */
    @Override
    public int getLocation(Node node) {
        if(node == null)
            return -1;
        Object distanceObject = nodeOwner.getDistance(node);
        if(distanceObject instanceof BinarySet) {
            BinarySet distance = (BinarySet) distanceObject;
            return sizeTable - 1 - distance.getFirstPositionOfOne();
        }
        return -1;
    }

    /**
     * @param node
     * @return the closest Node at the node in the routing table if it is present, null otherwise
     */
    @Override
    public Node getClosest(Node node) {
        if(!node.equals(nodeOwner)) {
            Node[] nodesClosest = getKClosest(node);
            if (nodesClosest != null){
                int minDistance = sizeTable * 2;
                Node nodeClosest = null;
                for (int i = 0; i < nodesClosest.length; i++) {
                    BinarySet distanceBinarySet = (BinarySet) nodeOwner.getDistance(node);
                    int distance = sizeTable - 1 - distanceBinarySet.getFirstPositionOfOne();
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

    /**
     * @param node
     * @return the closest K Nodes at the node in the routing table if it is present, null otherwise
     */
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

}
