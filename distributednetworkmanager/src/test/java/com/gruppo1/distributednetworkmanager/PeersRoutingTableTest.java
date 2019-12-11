package com.gruppo1.distributednetworkmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class PeersRoutingTableTest {

    public static final int NUMBER_BITS = 3;
    private PeersRoutingTable rt;

    @Before
    public void createRoutingTable(){
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(7)).byteValue()});
        PeerNode testNode = new PeerNode(new BinarySet(bitSet)); //KEY = 111
        rt = new PeersRoutingTable(testNode, NUMBER_BITS);
    }

    @Test
    public void add() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 001
        assertTrue(rt.add(newNode));
    }

    @Test
    public void contains() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 001
        rt.add(newNode);
        assertTrue(rt.contains(newNode));
    }

    @Test
    public void notContains() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 010
        assertFalse(rt.contains(newNode));
    }


    @Test
    public void removeTrue() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(4)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 100
        rt.add(newNode);
        assertTrue(rt.remove(newNode));
    }

    @Test
    public void removeFalse() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(3)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 011
        assertFalse(rt.remove(newNode));
    }

    @Test
    public void getBucket() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(5)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 101
        rt.add(newNode);
        KBucket bucket = rt.getBucket(1);
        assertEquals(newNode, bucket.getOldest());
    }

    @Test
    public void getLocation() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer((int)Math.pow(2,NUMBER_BITS)-2)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 110
        rt.getLocation(newNode);
        assertEquals(NUMBER_BITS-1, rt.getLocation(newNode));
    }

    @Test
    public void getClosest() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(6)).byteValue()});
        PeerNode closestNode = new PeerNode(new BinarySet(bitSet)); //KEY = 110
        rt.add(closestNode);
        bitSet = BitSet.valueOf(new byte[]{(new Integer(2)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 010
        rt.add(newNode);
        assertEquals(closestNode, rt.getClosest());
    }

    @Test
    public void getKClosest() {
        BitSet bitSet = BitSet.valueOf(new byte[]{(new Integer(6)).byteValue()});
        PeerNode closestNode = new PeerNode(new BinarySet(bitSet)); //KEY = 110
        rt.remove(closestNode);

        PeerNode[] nodes = new PeerNode[2];
        bitSet = BitSet.valueOf(new byte[]{(new Integer(4)).byteValue()});
        nodes[0] = new PeerNode(new BinarySet(bitSet)); //KEY = 100
        rt.add(nodes[0]);

        bitSet = BitSet.valueOf(new byte[]{(new Integer(5)).byteValue()});
        nodes[1] = new PeerNode(new BinarySet(bitSet)); //KEY = 101
        rt.add(nodes[1]);

        bitSet = BitSet.valueOf(new byte[]{(new Integer(1)).byteValue()});
        PeerNode newNode = new PeerNode(new BinarySet(bitSet)); //KEY = 001
        rt.add(newNode);

        assertEquals(nodes, rt.getKClosest());

    }

}