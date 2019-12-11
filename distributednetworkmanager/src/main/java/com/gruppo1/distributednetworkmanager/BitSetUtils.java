package com.gruppo1.distributednetworkmanager;

import android.util.Log;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

class BitSetUtils {

    private static int minLength = 64;
    private static final String NOT_VALID_NUMBIT_EXCEPTION_MSG = "numBits isn't > 0 or a multiple of 64";
    private static final String UTILS_TAG = BitSetUtils.class.toString();
    private static final String SHA_1 = "SHA-1";

    /**
     * @param
     * @return The distance of the Keys, calculated in XOR logic
     */
    public static BitSet distanceFrom(@NonNull BitSet first, @NonNull BitSet second) {
        BitSet distance = (BitSet) first.clone();
        distance.xor(second);
        return distance;
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
     * @param toHash  the String (a key) of which generate hashcode
     * @param numBits number of bits that the hash code will be constituted of, must be > 0
     * @return the bitSet containing the hash (SHA-1) of the bytes in input, truncated to numBits, bitSet's size is <= numBits
     * @throws IllegalArgumentException if peer isn't valid or the numBit isn't multiple of 64
     */
    public static BitSet hash(@NonNull byte[] toHash, int numBits) {
        byte[] digest = {0};
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_1);
            md.update(toHash);
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            Log.e(UTILS_TAG, e.getMessage());
            //Shouldn't happen, reported to log
        }
        if(digest.length*8 > numBits){
            byte[] trunk = new byte[(numBits%8 > 0) ? numBits/8+1 : numBits/8];
            System.arraycopy(digest, 0, trunk, 0, trunk.length);
            digest = trunk;
        }
        return BitSet.valueOf(digest);
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


}
