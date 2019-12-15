package com.gruppo1.distributednetworkmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class NodesRoutingTableTest {

    public static final int NUMBER_BITS = 3;
    private NodesRoutingTable rt;
    private Node<BinarySet>  nodeOwner, node;
    private BitSet bitSet;
    private BinarySet distance;

    @Before
    public void createRoutingTable(){
        bitSet = BitSet.valueOf(new byte[]{(new Integer(7)).byteValue()});

        nodeOwner = mock(Node.class);
        when(nodeOwner.getKey()).thenReturn(new BinarySet(bitSet));
        when(nodeOwner.keyLength()).thenReturn(NUMBER_BITS);

        node = mock(Node.class);
        when(node.keyLength()).thenReturn(NUMBER_BITS);

        rt = new NodesRoutingTable(nodeOwner, NUMBER_BITS);
    }

    @Test
    public void add() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()}); //KEY = 001
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(rt.add(node));
    }

    private BinarySet getDistancePeerNode(Node<BinarySet> nodeOwner, Node<BinarySet> node){
        return new PeerNode(nodeOwner.getKey()).getDistance(node);
    }

    @Test
    public void contains() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()}); //KEY = 001
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(rt.add(node));
        assertTrue(rt.contains(node));
    }

    @Test
    public void notContains() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()}); //KEY = 010
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertFalse(rt.contains(node));
    }

    @Test
    public void removeTrue() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(4)).byteValue()}); //KEY = 100
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(rt.add(node));
        assertTrue(rt.remove(node));
    }

    @Test
    public void removeFalse() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(3)).byteValue()}); //KEY = 011
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertFalse(rt.remove(node));
    }

    @Test
    public void getBucket() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer(5)).byteValue()}); //KEY = 101
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertTrue(rt.add(node));
        KBucket bucket = rt.getBucket(1);
        assertEquals(node, bucket.getOldest());
    }

    @Test
    public void getBucketInvalidPosition() {
        assertEquals(null, rt.getBucket(NUMBER_BITS));
    }

    @Test
    public void getNodeOwner() {
        assertEquals(nodeOwner, rt.getNodeOwner());
    }

    @Test
    public void getLocation() {
        bitSet = BitSet.valueOf(new byte[]{(new Integer((int)Math.pow(2,NUMBER_BITS)-2)).byteValue()}); //110
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);

        assertEquals(NUMBER_BITS-1, rt.getLocation(node));
    }

    @Test
    public void getClosest() {
        Node<BinarySet> referenceNode = mock(Node.class);
        Node<BinarySet> closestNode = mock(Node.class);

        bitSet = BitSet.valueOf(new byte[]{(new Integer(6)).byteValue()}); //KEY = 110
        when(node.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, node);
        when(nodeOwner.getDistance(node)).thenReturn(distance);
        assertTrue(rt.add(node));

        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()}); //KEY = 001
        when(referenceNode.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, referenceNode);
        when(nodeOwner.getDistance(referenceNode)).thenReturn(distance);
        assertTrue(rt.add(referenceNode));

        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()}); //KEY = 010
        when(closestNode.getKey()).thenReturn(new BinarySet(bitSet));
        distance =  getDistancePeerNode(nodeOwner, closestNode);
        when(nodeOwner.getDistance(closestNode)).thenReturn(distance);
        assertTrue(rt.add(closestNode));

        distance =  getDistancePeerNode(referenceNode, closestNode);
        when(referenceNode.getDistance(closestNode)).thenReturn(distance);

        assertEquals(closestNode.getKey(), rt.getClosest(referenceNode).getKey());
    }

    @Test
    public void getKClosest() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(6)).byteValue()});
        Node closestNode = new PeerNode(new BinarySet(bitSet)); //KEY = 110
        rt.remove(closestNode);

        Node[] nodes = new PeerNode[2];
        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()});
        nodes[0] = new PeerNode(new BinarySet(bitSet)); //KEY = 001
        rt.add(nodes[0]);

        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()});
        nodes[1] = new PeerNode(new BinarySet(bitSet)); //KEY = 010
        rt.add(nodes[1]);

        bitSet = BitSet.valueOf(new byte[]{(new Integer(4)).byteValue()});
        Node newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 100
        rt.add(newNode);

        bitSet = BitSet.valueOf(new byte[]{(new Integer(3)).byteValue()});
        Node nodeTest = new PeerNode(new BinarySet(bitSet)); //KEY = 011

        assertEquals(nodes, rt.getKClosest(nodeTest));


    }

}