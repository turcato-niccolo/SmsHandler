package com.gruppo1.kademlia;

/**
 * This interface defines actions that a Node can do
 * @author Giorgia Bortoletti
 */
public interface ActionsNode {

    /**
     * Return true if a node is online
     * @param IDNode
     * @return true if it is found, false otherwise
     */
    boolean ping(String IDNode);

    /**
     * Store a <key; value> pair
     * @param key
     * @param value
     * @return true if it is stored, false otherwise
     */
    NodeKademlia store(String key, String value);

    /**
     * Find a node
     * @param IDNode 160-bit ID node
     * @return node
     */
    NodeKademlia findNode(String IDNode);

    /**
     * Find a node value
     * @param value
     * @return node
     */
    NodeKademlia findValue(String value);

}
