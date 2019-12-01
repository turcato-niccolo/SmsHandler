package com.gruppo1.distributednetworkmanager;

/**
 * @author Niccolo' Turcato
 * Structure of K-Bucket object based on the peer-to-peer network protocol Kademlia
 *
 * @param <T> Type of object contained in the bucket
 */
public abstract class Bucket<T> {
    private int dimension;

    /**
     * @param dim capacity of the bucket
     */
    public Bucket(int dim) {
        dimension = dim;
    }

    /**
     * @param obj object of which verify presence in bucket
     * @return true if the element is contained in the bucket, false otherwise
     */
    public abstract boolean Contains(T obj);


    /**
     * Added with stack logic, eventually replaces the one "on top"
     * @param obj object to add
     * @return true if it has been added, false otherwise
     */
    public abstract boolean Add(T obj);

    /**
     *
     * @param obj object to remove from bucket
     * @return true if obj has been removed, false otherwise
     */
    public abstract boolean Remove(T obj);

    /**
     * @return an array containing (a copy) the elements in the bucket, sorted by insertion time
     */
    public abstract T[] getElements();
}
