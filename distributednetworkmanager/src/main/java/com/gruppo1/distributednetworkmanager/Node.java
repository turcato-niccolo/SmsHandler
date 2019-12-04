package com.gruppo1.distributednetworkmanager;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.BitSet;

/**
 * @author Niccolo' Turcato
 * Class rapresenting a generic Node of the distributed Network
 */
public class Node {

    private BitSet key;
    private int keyLength;
    static final private int minLength = 64;

    /**
     * Constructor that initializes the key with the given value
     * @param buildingKey given value for the node's Key
     * @param numBits number of bits that compose the key (must be > length of buildingKey)
     *
     * @throws IllegalArgumentException if the numBit isn't multiple of 64 or numBits <= buildingKey.length()
     */
    public Node(int numBits, @NonNull BitSet buildingKey){
        if (numBits % minLength == 0 && numBits > buildingKey.length()) {
            keyLength = numBits;
            key = (BitSet)buildingKey.clone();
        }
        else throw new IllegalArgumentException("numBits isn't > 0 or a multiple of 64 or numBits <= buildingKey.length()");
    }

    /**
     * @return number of bits that compose the BitSet key
     */
    public int keyLength(){
        return keyLength;
    }

    /**
     * @return the bitset containing the node's key
     */
    public BitSet getKey(){
        return (BitSet) key.clone();
    }

    /**
     * @param peerAddress the phoneNumber to convert, must be a valid phone number (base 10)
     * @return -1 if address is't a number, otherwise the phoneNumber converted to long
     */
    static private long getLongValueFromAddress(@NonNull String peerAddress){
        long value = -1;
        if(peerAddress.length() != 0){
            value = Long.getLong(peerAddress.replace("+", ""));
        }
        return value;
    }

    /**
     * @param toHash the String (a key) of which generate hashcode
     * @param numBits number of bits that the hash code will be constituted of, must be multiple of 64 and >0
     *
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     *
     * @return the bitSet containing the hash of the peer's address, bitSet's length is a multiple of 64
     */
    public static BitSet hash(@NonNull String toHash, int numBits){
        if (numBits > 0 && numBits % minLength == 0) {
            int numLong = numBits/minLength;
            long[] numbers = new long[numLong];
            for (int i = 0; i < numLong; i++){
                numbers[numLong-(i+1)] = Double.doubleToLongBits(hash(toHash) / (i+1))  ;
            }
            return BitSet.valueOf(numbers);
        }
        else throw new IllegalArgumentException("numBits isn't > 0 or a multiple of 64");
    }

    /**
     * Based on String.hashCode()
     * @param key string of which generate hash code on 64 bits
     * @return a 64 bits hash of the given String
     */
    private static long hash(@NonNull String key) {
        long num = 0;
        int n = key.length();
        for (int i = 0; i < n; i++){
            num += key.charAt(i) * (63 ^ (n - (i+1))); //original has 31 instead of 63
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
     * @return 1 if rhs > lhs, -1 rhs < lhs, 0 rhs = lhs
     */
    public static int compare(@NonNull BitSet lhs,@NonNull BitSet rhs) {
        if (lhs.equals(rhs)) return 0;
        BitSet distance = (BitSet)lhs.clone();
        distance.xor(rhs);
        int firstDifferent = distance.length()-1;
        if(firstDifferent==-1)
            return 0;
        return rhs.get(firstDifferent) ? 1 : -1;
    }

    /**
     *
     * @param other the Node for which calculate distance from this Node
     * @return The distance of the Keys, calculated as XOR of BitSets
     */
    public BitSet distanceFrom(@NonNull Node other){
        BitSet distance = this.getKey();
        distance.xor(other.getKey());
        return distance;
    }

    /**
     * @return a new PeerNode equal to this
     */
    @Override
    public Node clone() {
        return new Node(keyLength, key);
    }

    /**
     *
     * @param other peer to confront
     * @return true if this peer and the other are equals, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other == null)
            return false;
        if(this == other)
            return true;
        if(other instanceof Node)
            return this.getKey().equals(((Node)other).getKey());
        return false;
    }

}
