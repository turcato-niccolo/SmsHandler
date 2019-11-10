package com.dezen.riccardo.networkmanager.alternative;

/**
 * Interface similar to Message, except it is meant to represent a resource in the vocabulary instead
 * of a message travelling through the network
 */
public interface ResourceAlternative<N, V>{
    N getName();
    V getValue();
}
