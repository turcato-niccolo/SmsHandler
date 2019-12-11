package com.gruppo1.distributednetworkmanager;

import androidx.annotation.NonNull;

import java.math.BigInteger;
import java.util.BitSet;


/**
 * This class was made to extend BitSet as comparable
 */
public class BinarySet implements Comparable<BinarySet>, Cloneable {
    private BitSet key;
    private String EMPTY_KEY_EXCEPTION = "EMPTY_KEY_EXCEPTION";

    /**
     * Constructor that initializes the key with the given value
     *
     * @param buildingKey given value for the Key
     */
    public BinarySet(@NonNull BitSet buildingKey) {
        if(buildingKey.size() > 0)
            key = (BitSet) buildingKey.clone();
        else throw new IllegalArgumentException(EMPTY_KEY_EXCEPTION);
    }

    /**
     * @param set another object of BinarySet to compare
     * @return @return a negative integer, zero, or a positive integer as this < other set, the two are equal, this > other set.
     */
    public int compareTo(@NonNull BinarySet set) {
        if (this.equals(set)) return 0;
        BitSet distance = set.getKey();
        distance.xor(key);
        int firstDifferent = distance.length() - 1;
        if (firstDifferent == -1)
            return 0;
        return key.get(firstDifferent) ? 1 : -1;
    }

    /**
     * @return new BinarySet equals to this
     */
    public Object clone() {
        return new BinarySet((BitSet) key.clone());
    }

    /**
     * @return number of bits that compose the BitSet key
     */
    public int keyLength() {
        return key.size();
    }

    /**
     * @param set node of which calculate distance
     * @return the distance in XOR metric
     */
    public BinarySet getDistance(@NonNull BinarySet set) {
        BitSet distance = getKey();
        distance.xor(set.getKey());
        return new BinarySet(distance);
    }

    /**
     * @param set node of which calculate distance
     * @return the distance in XOR metric
     */
    public int getDistanceInteger(@NonNull BinarySet set) {
        return new BigInteger(getDistance(set).getKey().toByteArray()).intValue();
    }

    /**
     * @return the BitSet key
     */
    public BitSet getKey() {
        return (BitSet) key.clone();
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
        if (other instanceof BinarySet)
            return key.equals(((BinarySet) other).getKey());
        return false;
    }

}
