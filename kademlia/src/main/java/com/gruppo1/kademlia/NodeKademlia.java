package com.gruppo1.kademlia;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represent a node in Kademlia protocol where a Node has a id, a value, a list of others nodes
 * and it can do these actions: ping, store, findKey, findValue
 * TODO? non viene gestito il caso in cui non sia presente un valore o un id e genera ricorsione infinita
 * @author Giorgia Bortoletti
 */

public class NodeKademlia extends Node<String, String> implements ActionsNode {

    private ArrayList<NodeKademlia> listNodes; //nothing distinction from PeerNode and ResourceNode

    /**
     * Constructor
     *
     * @param key that is encrypted and saved as it's ID
     */
    public NodeKademlia(String key, String value) {
        super(key, value);

        listNodes = new ArrayList<>();
    }

    /**
     * Add node to list nodes
     * @param node
     * @return true if node is been added
     */
    public boolean addNode(NodeKademlia node){
        if(listNodes.size() == 20){
            //delete one node with some policies
            return false;
        }

        //add new node
        return listNodes.add(node);
    }

    /**
     * Remove node to list nodes
     * @param node
     * @return true if node is been removed
     */
    public boolean removeNode(NodeKademlia node){
        if(listNodes.contains(node))
            return listNodes.remove(node);
        return false;
    }

    /**
     * With this version of ping the research routes ping to the nearest node
     * @param idNode
     * @return
     */
    @Override
    public boolean ping(String idNode) {
        NodeKademlia closerNode = closerTo(idNode);

        if(this.equals(closerNode)){
            return true;
        }else{
            return closerNode.ping(idNode);
        }
    }

    @Override
    public NodeKademlia store(String key, String value) {
        NodeKademlia newNode = new NodeKademlia(key, value);
        NodeKademlia closerNode = closerTo(newNode.getID());

        if(this.equals(closerNode)){
            if(addNode(newNode) && newNode.addNode(this)) //add my self to the listNodes of the newNode
                return this;
            else
                return null;
        }else{
            return closerNode.store(key, value);
        }
    }

    @Override
    public NodeKademlia findNode(String idNode) {
        NodeKademlia closerNode = closerTo(idNode);

        if(this.equals(closerNode)){
            return this;
        }else{
            return closerNode.findNode(idNode);
        }

    }

    @Override
    public NodeKademlia findValue(String value) {
        for(NodeKademlia myNode : listNodes)
        {
            if(myNode.getValue().equals(value))
                return this;
        }

        for(NodeKademlia myNode : listNodes)
        {
            return myNode.findValue(value);
        }

        return null;
    }

    /**
     * Return node closer to idNode with XOR
     * @param idNode
     * @return node closest to idNode
     */
    private NodeKademlia closerTo(String idNode){
        int minDistance = xor(idNode, getID().toString());
        NodeKademlia closestNode = this;

        for(NodeKademlia myNode : listNodes)
        {
            int newDistance = xor(idNode, myNode.getID());
            if(newDistance < minDistance){
                minDistance = newDistance;
                closestNode = new NodeKademlia(myNode.getID(), myNode.getValue());
            }

        }

        return closestNode;
    }

    /**
     * Return xor between two Strings
     * @param s
     * @param s1
     * @return
     */
    private int xor(String s, String s1){
        byte[] sBytes = s.getBytes();
        byte[] s1Bytes = s1.getBytes();

        int xor = 0;

        for(int i=0; i<s.length(); i++){
            xor += sBytes[i] ^ s1Bytes[i];
        }

        return xor;
    }

    /**
     * Indicates whether some other object is "equal to" this one
     * @param o The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        if(this == o)
            return true;
        if (!(o instanceof NodeKademlia))
            return false;
        NodeKademlia node = (NodeKademlia) o;
        return getID().equals(node.getID());
    }

}
