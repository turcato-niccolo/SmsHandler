package com.gruppo1.distributednetworkmanager;

import java.util.List;

/**
 * Interface defining standard behaviour for a class able to provide data regarding the Nodes in a Network
 * @author Riccardo De Zen
 * @param <K> Type of key for the Node the implementing class works on
 * @param <N> Type of Node the implementing class works on
 */
public interface NodeDataProvider<K, N extends Node<K>>{
    /**
     * Method to return the closest known Node to the target one
     * @param target the target Node key
     * @return the closest known Node to the target
     */
    N getClosest(K target);

    /**
     * Method to return the (up to) k known Nodes to a target one
     * @param k the max amount of Nodes returned
     * @param target the target Node key
     * @return a List containing up to k Nodes, the closest known Nodes to the target
     */
    List<N> getKClosest(int k, K target);

    /**
     * Method to return the (up to) k Nodes in the given list that are closest to the target
     * @param target the target Node key
     * @return the Node closest to the target amongst the known Nodes
     */
    List<N> filterKClosest(int k, K target, List<N> nodes);
}
