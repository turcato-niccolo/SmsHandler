package com.gruppo1.distributednetworkmanager;

import java.util.List;

/**
 * Interface to define routing table
 * @param <T> type of Bucket
 */
public interface RoutingTableInterface<B extends Bucket<Node>> {

    /**
     * Add element to routing table
     * @param newBucket
     * @return true if it has been added, false otherwise
     */
    boolean addNode(B newBucket);

    /**
     * Remove element from routing table
     * @param bucketToDelete
     * @return true if it has been removed, false otherwise
     */
    boolean removeNode(B bucketToDelete);

    /**
     * Find if routing table contains this element
     * @param bucketToFind
     * @return true if it has been found, false otherwise
     */
    boolean contains(B bucketToFind);

    /**
     * Return K elements closer
     * @param bucketCloser
     * @return a List of K elements closer
     */
    List<B> getKCloser(B bucketCloser);

}
