package com.dezen.riccardo.networkmanager;

/**
 * Interface similar to Message, except it is meant to represent a resource in the peersVocabulary instead
 * of a message travelling through the network
 */
public interface Resource<N, V>{
    N getName();
    V getValue();
}
