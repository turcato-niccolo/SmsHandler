package com.gruppo1.distributednetworkmanager;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class BinarySetTest {
    BinarySet binarySet;
    BitSet bitSet;
    String testString = "This is random";
    String empty = "";

    @Before
    public void Init() {
        bitSet = BitSetUtils.hash(testString.getBytes(), 120);
        binarySet = new BinarySet(bitSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void BinarySet_ConstructorExceptionTest() {
        BinarySet set = new BinarySet(new BitSet(0));
    }

    @Test
    public void BinarySet_CompareEqualsTest() {
        BinarySet second = new BinarySet(BitSetUtils.hash(testString.getBytes(), 120));
        assertEquals(binarySet, second);
    }

    @Test
    public void BinarySet_ComparePositiveTest() {
        BinarySet second = new BinarySet(BitSetUtils.hash(empty.getBytes(), 120));
        assertTrue(binarySet.compareTo(second) > 0);
    }

    @Test
    public void BinarySet_ClonePositiveTest() {
        assertNotSame(binarySet, binarySet.clone());
    }

    @Test
    public void BinarySet_KeyLengthPositiveTest() {
        assertEquals(binarySet.keyLength(), bitSet.size());
    }

    @Test
    public void BinarySet_DifferentnotEqualTest() {
        BitSet set = BitSetUtils.hash("Calogero".getBytes(), 120);
        BinarySet B = new BinarySet(set);
        assertNotEquals(binarySet, B);
    }

}