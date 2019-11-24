package com.dezen.riccardo.smshandler;

/**
 * @author Riccardo De Zen. Based on suggestion from Dr. Li Dao Hong.
 * @param <T> The type of address that identifies the Peer.
 */

public abstract class Peer<T> {
    /**
     * @return the address of this Peer
     */
    public abstract T getAddress();
}
