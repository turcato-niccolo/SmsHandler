package com.gruppo1.distributednetworkmanager;

/**
 * @author
 */
public class KBucket extends Bucket<PeerNode> {
    private int dimension;
    private int last;
    private PeerNode[] elements;

    /**
     * @param dim capacity of the bucket
     */
    public KBucket(int dim) {
        dimension = dim;
        elements = new PeerNode[dimension];
    }

    /**
     * @param toFind object of which verify presence in bucket
     * @return true if the element is contained in the bucket, false otherwise
     */
    public boolean contains(PeerNode toFind){
        for (int i = 0; i<last; i++)
            if(elements[i].equals(toFind))
                return true;
        return false;
    }


    /**
     * Added with stack logic, eventually replaces the one "on top"
     * @param peerNode object to add
     * @return true if it has been added, false otherwise
     */
    public boolean add(PeerNode peerNode){
        if(!contains(peerNode)){
            if(last == dimension)
                elements[dimension-1] = peerNode.clone();
            else
                elements[last++] = peerNode.clone();
            return contains(peerNode);
        }
        else return false;
    }

    /**
     *
     * @param peerNode object to remove from bucket
     * @return true if obj has been removed, false otherwise
     */
    public boolean remove(PeerNode peerNode){
        if(contains(peerNode)) {
            for (int i = 0; i < last; i++)
                if (elements[i].equals(peerNode)) {
                    for (int j = i; j < dimension - 1; j++)
                        elements[j] = elements[j + 1];
                    elements[--last] = null;
                }
            return !contains(peerNode);
        }
        return false;
    }

    /**
     * @return an array containing (a copy) the elements in the bucket, sorted by insertion time
     */
    public PeerNode[] getElements(){
        PeerNode[] copy = new PeerNode[last];
        for (int i = 0; i<last; i++) copy[i] = elements[i].clone();
        return copy;
    }

    /**
     * @return (a copy of) the oldest peer in the network, the "bottom" one, if the bucket is empty returns null
     */
    public PeerNode getOldest(){
        if(dimension > 0)
            return elements[0];
        return null;
    }
}
