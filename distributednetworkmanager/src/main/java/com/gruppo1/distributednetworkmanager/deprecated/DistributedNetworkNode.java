package com.gruppo1.distributednetworkmanager.deprecated;

import androidx.annotation.NonNull;


import com.gruppo1.distributednetworkmanager.Node;

import java.util.BitSet;

/**
 * @author Niccolo' Turcato
 * Class rapresenting a generic DistributedNetworkNode of the distributed Network
 */
public class DistributedNetworkNode implements Comparable<Node<BitSet>> {
    private BitSet key;
    private int keyLength;
    static final private int minLength = 64;

    private static final String NOT_SAME_ADDR_SPACE_EXCEPTION_MSG = "the given DistributedNetworkNode isn't part of the same address space as this";
    private static final String NOT_VALID_NUMBIT_EXCEPTION_MSG = "numBits isn't > 0 or a multiple of 64";

    /**
     * Constructor that initializes the key with the given value
     *
     * @param buildingKey given value for the node's Key
     * @param numBits     number of bits that compose the key (must be > length of buildingKey)
     * @throws IllegalArgumentException if the numBit isn't multiple of 64 or numBits <= buildingKey.length()
     */
    public DistributedNetworkNode(int numBits, @NonNull BitSet buildingKey) {
        if (numBits % minLength == 0 && numBits > buildingKey.length()) {
            keyLength = numBits;
            key = (BitSet) buildingKey.clone();
        } else
            throw new IllegalArgumentException("numBits isn't > 0 or a multiple of 64 or numBits <= buildingKey.length()");
    }

    /**
     * @return number of bits that compose the BitSet key
     */
    public int keyLength() {
        return keyLength;
    }

    /**
     * @return the bitset containing the node's key
     */
    public BitSet getKey() {
        return (BitSet) key.clone();
    }

    /**
     * @param address the phoneNumber to convert, must be a valid phone number (base 10 digits)
     * @return -1 if address is't a number, otherwise the phoneNumber converted to long
     */
    static private long getLongValueFromAddress(@NonNull String address) {
        long value = -1;
        if (address.length() != 0) {
            try {
                //FIXME or we can delete it
                value = Long.getLong(address.replace("+", ""));
            } catch (Exception e) {

            }
        }
        return value;
    }


    /**
     * @param toHash  the String (a key) of which generate hashcode
     * @param numBits number of bits that the hash code will be constituted of, must be multiple of 64 and >0
     * @return the bitSet containing the hash of the peer's address, bitSet's length is a multiple of 64
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     */
    public static BitSet hash(@NonNull String toHash, int numBits) {
        if (numBits > 0 && numBits % minLength == 0) {
            int numLong = numBits / minLength;
            long[] numbers = new long[numLong];
            for (int i = 0; i < numLong; i++) {
                numbers[numLong - (i + 1)] = Double.doubleToLongBits(hash(toHash) / (i + 1));
            }
            return BitSet.valueOf(numbers);
        } else throw new IllegalArgumentException(NOT_VALID_NUMBIT_EXCEPTION_MSG);
    }

    /**
     * Based on String.hashCode()
     *
     * @param key string of which generate hash code on 64 bits
     * @return a 64 bits hash of the given String
     */
    private static long hash(@NonNull String key) {
        long num = 0;
        int n = key.length();
        for (int i = 0; i < n; i++) {
            num += key.charAt(i) * (63 ^ (n - (i + 1))); //original has 31 instead of 63
        }
        return num;
    }

    /**
     * Method to compare two bitSets, useful to compare Distances
     * compare(b1.xor(b2), b1.xor(b3)) ==> compare(D(b1, b2), D(b1, b3))
     * ==> D(b1, b2) ? D(b1, b3)
     * That is: is b1 closer to b2 or b3?
     *
     * @param lhs first bitSet
     * @param rhs second bitSet
     * @return a negative integer, zero, or a positive integer as rhs < lhs, rhs = lhs, rhs > lhs.
     */
    public static int compare(@NonNull BitSet lhs, @NonNull BitSet rhs) {
        if (lhs.equals(rhs)) return 0;
        BitSet distance = (BitSet) lhs.clone();
        distance.xor(rhs);
        int firstDifferent = distance.length() - 1;
        if (firstDifferent == -1)
            return 0;
        return rhs.get(firstDifferent) ? 1 : -1;
    }

    /**
     * @param other the DistributedNetworkNode for which calculate distance from this DistributedNetworkNode
     * @return The distance of the Keys, calculated as XOR of BitSets
     * @throws IllegalArgumentException if the given DistributedNetworkNode isn't part of this node's address space
     */
    public BitSet distanceFrom(@NonNull DistributedNetworkNode other) {
        if (other.keyLength() == this.keyLength()) {
            BitSet distance = this.getKey();
            distance.xor(other.getKey());
            return distance;
        } else throw new IllegalArgumentException(NOT_SAME_ADDR_SPACE_EXCEPTION_MSG);
    }

    /**
     * @return a new PeerNode equal to this
     */
    @Override
    public DistributedNetworkNode clone() {
        return new DistributedNetworkNode(keyLength, getKey());
    }

    /**
     * @param other peer to confront
     * @return true if this peer and the other are equals, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (this == other)
            return true;
        if (other instanceof DistributedNetworkNode)
            return this.getKey().equals(((DistributedNetworkNode) other).getKey());
        return false;
    }

    /**
     * @param other another object of class node to compare to this node
     * @return a negative integer, zero, or a positive integer as this DistributedNetworkNode is less than, equal to, or greater than the specified DistributedNetworkNode.
     * @throws IllegalArgumentException if the given DistributedNetworkNode isn't part of this node's address space
     */

    public int compareTo(@NonNull DistributedNetworkNode other) {
        if (other.keyLength() == this.keyLength()) {
            return compare(other.getKey(), getKey());
        } else throw new IllegalArgumentException(NOT_SAME_ADDR_SPACE_EXCEPTION_MSG);
    }

    public int compareTo(@NonNull BitSet other) {
        return 0;
    }

    public int compareTo(@NonNull Node other) {
        return 0;
    }
}
