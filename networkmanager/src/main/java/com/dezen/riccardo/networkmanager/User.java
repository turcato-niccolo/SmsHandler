package com.dezen.riccardo.networkmanager;

/**
 * Interface similar to Peer. Peer can be used based on decisions made regarding module separation
 * and dependencies
 */
public interface User<N>{
    N getName();
}