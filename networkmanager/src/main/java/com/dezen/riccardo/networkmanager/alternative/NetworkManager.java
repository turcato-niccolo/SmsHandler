package com.dezen.riccardo.networkmanager.alternative;

import java.util.List;

/**
 * Class to manage network access
 */
public abstract class NetworkManager<I extends Invitation, U extends User, R extends Resource>{
    /**
     * Method to be called by a user wishing to join the network
     * @param invitation the invitation to a network
     * @param newUser the user object identifying the user wishing to join
     */
    public abstract void join(I invitation, U newUser);

    /**
     * Method to be called by a user wishing to leave the network
     */
    public abstract void leave();

    /**
     * @return true if the Network is ready to perform operations
     */
    public abstract boolean isReady();

    /**
     * @return a list with copies of the users in the network
     */
    public abstract List<U> getUsers();

    /**
     * @return a list with copies of the resources shared by the network
     */
    public abstract List<R> getResources();
}
