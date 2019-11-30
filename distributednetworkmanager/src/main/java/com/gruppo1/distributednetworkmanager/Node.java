package com.gruppo1.distributednetworkmanager;


import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.Peer;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.BitSet;

/**
 * @author Niccolo' Turcato
 * Class that represents the Node of DistributedNetwork
 */
class Node extends Peer<BitSet> {

    private BitSet key;
    private SMSPeer physicalPeer;
    static final private int minLength = 64;

    /**
     * Constructor: creates an instance of Node, based on the SMSPeer address
     * Considers all the digits (except the '+') generates the key with the public Hash method
     * @param numBits number of bits which constitute the Key, must be multiple of 64 and >0
     * @param buildingPeer the physical Peer on which this node is based
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     */
    public Node(int numBits, SMSPeer buildingPeer){
        if(buildingPeer.isValid()) {
            if (numBits > 0 && numBits % minLength == 0) {
                key = hash(buildingPeer, numBits);
                physicalPeer = buildingPeer;
            }
            else throw new IllegalArgumentException("numBits isn't > 0 or a multiple of 64");
        }
        else throw new IllegalArgumentException("buildingPeer isn't a valid peer, see SMSPeer.isValid()");
    }

    public BitSet getAddress() {
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
     * @param peer the peer of which generate hashcode
     * @param numBits number of bits that the hash code will be constituted of, must be multiple of 64 and >0
     *
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     *
     * @return the bitSet containing the hash of the peer's address, bitSet's length is a multiple of 64
     */
    public static BitSet hash(SMSPeer peer, int numBits){
        String phoneNum = peer.getAddress();
        if(peer.isValid()) {
            if (numBits > 0 && numBits % minLength == 0) {
                int numLong = numBits/minLength;
                long[] numbers = new long[numLong];
                for (int i = 0; i < numLong; i++){
                    numbers[i] = Double.doubleToLongBits(hash(peer.getAddress()) * (1-0.01*i))  ;
                }
                return BitSet.valueOf(numbers);
            }
            else throw new IllegalArgumentException("numBits isn't > 0 or a multiple of 64");
        }
        else throw new IllegalArgumentException("buildingPeer isn't a valid peer, see SMSPeer.isValid()");
    }

    /**
     * Based on String.hashCode()
     * @param key string of which generate hash code on 64 bits
     * @return a 64 bits hash of the given String
     */
    private static long hash(String key) {
        long num = 0;
        int n = key.length();
        for (int i = 0; i < n; i++){
            num += key.charAt(i) * (63 ^ (n - (i+1))); //original has 31 instead of 63
        }
        return num;
    }

    /**
     * Method to compare two bitSets, useful to compare Distances
     * compare(b1.xor(b2), b1.xor(b3)) ==> D(b1, b2) ? D(b1, b3)
     * That is: is b1 closer to b2 or b3?
     *
     * @param lhs first bitSet
     * @param rhs second bitSet
     * @return 1 if rhs > lhs, -1 rhs < lhs, 0 rhs = lhs
     */
    public int compare(@NonNull BitSet lhs,@NonNull BitSet rhs) {
        if (lhs.equals(rhs)) return 0;
        BitSet distance = (BitSet)lhs.clone();
        distance.xor(rhs);
        int firstDifferent = distance.length()-1;
        if(firstDifferent==-1)
            return 0;
        return rhs.get(firstDifferent) ? 1 : -1;
    }
}
