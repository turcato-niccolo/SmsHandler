package com.dezen.riccardo.networkmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Interface meant to represent a resource in the peersVocabulary instead
 * of a message travelling through the network.
 * @author Riccardo De Zen.
 */
public abstract class Resource<N extends Comparable<N>, V> implements Comparable<Resource<N,V>>{
    /**
     * @return the name of this Resource
     */
    public abstract N getName();

    /**
     * @return the value of this Resource
     */
    public abstract V getValue();

    /**
     * Method to check whether this Resource is valid
     * @return true if this Resource is valid, false if not
     */
    public abstract boolean isValid();

    /**
     * @return extras attached to this Resource, if any, an empty Bundle otherwise
     */
    public abstract Bundle getExtras();

    /**
     * Two Resources are equal by default if their names are equal.
     * @param obj the other Resource
     * @return true if the other Resource equals this, false if not
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Resource){
            Resource other = (Resource) obj;
            return other.getName().equals(this.getName());
        }
        else return false;
    }

    /**
     * Two Resources can, by default, be ordered using their name as a key.
     * @param resource the Resource to compare
     * @return the result of the comparison between the names
     */
    @Override
    public int compareTo(@NonNull Resource<N,V> resource) {
        return resource.getName().compareTo(this.getName());
    }
}
