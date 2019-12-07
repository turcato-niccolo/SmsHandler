package com.gruppo1.distributednetworkmanager;


/**
 * Class that describes an abstract Node
 *
 * @param <T> type of key
 */
public abstract class Node<T> implements Cloneable {

    abstract public int keyLength();

    abstract public T getKey();

    abstract public Node<T> clone();
}
