package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class NodeTest {

    Node testNode;
    String testString = "This is a random Key String";

    @Before
    public void initialize(){
        testNode = new Node(64, Node.hash(testString, 64));
    }

    @Test(expected = IllegalArgumentException.class)
    public void Node_Constructor_NumBitsIllegalArgumentExceptionTest(){
        Node node = new Node(127, Node.hash(testString, 128));
    }

    @Test
    public void Node_hashTest(){
        BitSet hash = Node.hash(testString, testNode.keyLength());
        assertEquals(hash, testNode.getKey());
    }

    @Test
    public void Node_comparePositiveTest(){
        BitSet big = new BitSet(128);
        BitSet small = new BitSet(128);

        big.set(10, 100);
        small.set(10, 80);
        assertEquals(Node.compare(small, big), 1);
        assertEquals(Node.compare(big, small), -1);
        assertEquals(Node.compare(big, big), 0);
    }

    @Test
    public void Node_distanceFromTest(){
        BitSet A = Node.hash(testString, testNode.keyLength());

        String newString = "Another valid string key";
        Node newPeerNode = new Node(testNode.keyLength(), Node.hash(newString, testNode.keyLength()));
        BitSet B = Node.hash(newString, testNode.keyLength());

        A.xor(B);
        assertEquals(A, testNode.distanceFrom(newPeerNode));
        assertEquals(A, newPeerNode.distanceFrom(testNode));
    }

    @Test
    public void Node_compareDistancePositiveTest(){
        BitSet A = Node.hash(testString, testNode.keyLength());

        String newString = "Another valid string key";
        Node newPeerNode = new Node(testNode.keyLength(), Node.hash(newString, testNode.keyLength()));
        BitSet B = Node.hash(newString, testNode.keyLength());

        //A = testNode.getKey(), B = newPeerNode.getKey()

        BitSet C = new BitSet(testNode.keyLength()); //000...000
        C.set(0,4); //...00011111

        Node confrontNode = new Node(testNode.keyLength(), C);

        A.xor(C);
        B.xor(C);

        assertEquals(Node.compare(A,B), Node.compare(confrontNode.distanceFrom(testNode), confrontNode.distanceFrom(newPeerNode)));
        assertEquals(Node.compare(B,A), Node.compare(confrontNode.distanceFrom(newPeerNode), confrontNode.distanceFrom(testNode)));
        assertEquals(Node.compare(A,A), Node.compare(confrontNode.distanceFrom(testNode), confrontNode.distanceFrom(testNode)));
    }
}