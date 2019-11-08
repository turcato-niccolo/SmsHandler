package com.dezen.riccardo.networkmanager;

/**
 * Class to manage network access
 */
public abstract class NetworkManager<I extends Invitation>{
    public abstract void join(I invitation, User newUser);
    public abstract void leave();
    public abstract boolean isReady();
}
