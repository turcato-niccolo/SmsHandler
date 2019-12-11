package com.gruppo1.distributednetworkmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends Rounting Table with KBucket that contains PeerNode
 * Size of RoutingTable is fixed and equals to peerOwner's key length
 * @author Giorgia Bortoletti
 */
public class PeersRoutingTable extends RoutingTable<KBucket> {

    private List<KBucket> bucketsTable;
    private int sizeTable;
    private PeerNode peerOwner;

    /**
     * Constructor where the routing table lenght is equal to peer owner length
     * @param peerOwner
     */
    public PeersRoutingTable(PeerNode peerOwner){
        new PeersRoutingTable(peerOwner, peerOwner.keyLength());
    }

    /**
     * Constructor where the routing table lenght is sizeTable
     * @param peerOwner
     * @param sizeTable dimension of routing table
     */
    public PeersRoutingTable(PeerNode peerOwner, int sizeTable){
        this.peerOwner = peerOwner;
        this.sizeTable = sizeTable;
        bucketsTable = new ArrayList<>(sizeTable);
        for(int i=0; i<sizeTable; i++)
            bucketsTable.add(new KBucket(sizeTable));
    }

    /**
     * @return peerOwner of this routing table
     */
    public PeerNode getPeerOwner()
    {
        return peerOwner;
    }
    @Override
    public boolean add(PeerNode node) {
        if(node == null)
            return false;
        return bucketsTable.get(getLocation(node)).add(node);
    }

    @Override
    public boolean remove(PeerNode node) {
        if(node == null)
            return false;
        int distance = peerOwner.getDistanceInteger(node);
        KBucket bucketFound = findBucket(distance); //bucket where should be the node
        if(bucketFound == null)
            return false;
        return bucketFound.remove(node);
    }

    @Override
    public boolean contains(PeerNode node) {
        if(node == null || bucketsTable.size() == 0)
            return false;
        int distance = peerOwner.getDistanceInteger(node);
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
    public int getLocation(PeerNode node) {
        if(node == null)
          return -1;
        int distance = peerOwner.getDistanceInteger(node);
        for(int i=0; i<sizeTable; i++){
            int min = (int)Math.pow(2,sizeTable-i-1);
            int max = (int)Math.pow(2,sizeTable-i)-1;
            if(distance >= min && distance <= max)
                return i;
        }
        return -1;
    }

    @Override
    public Node getClosest() {
        Node[] nodesClosest = getKClosest();

        if (nodesClosest != null){
            int minDistance = sizeTable * 2;
            Node nodeClosest = null;
            for (int i = 0; i < nodesClosest.length; i++) {
                int distance = peerOwner.getDistanceInteger(nodesClosest[i]);
                if (distance < minDistance) {
                    minDistance = distance;
                    nodeClosest = nodesClosest[i];
                }
            }
            return nodeClosest;
        }
        return null;
    }

    @Override
    public Node[] getKClosest() {
        for(int i = bucketsTable.size()-1; i>= 0; i--){
            Node[] nodesClosest = bucketsTable.get(i).getElements();
            if(nodesClosest.length >= 1)
                return nodesClosest;
        }
        return null;
    }

    /**
     * This method is used to find which is the bucket of a Node with the distance from peer owner to Node
     * @param distanceFromPeerOwner
     * @return KBucket which maybe contains the Node, null otherwise
     */
    private KBucket findBucket(int distanceFromPeerOwner){
        for(int i=0; i<sizeTable; i++){
            int min = (int)Math.pow(2,sizeTable-i-1);
            int max = (int)Math.pow(2,sizeTable-i)-1;
            if(distanceFromPeerOwner >= min && distanceFromPeerOwner <= max)
            {
                return bucketsTable.get(i);
            }
        }
        return null;
    }


}

