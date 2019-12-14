package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.smshandler.Peer;
import com.gruppo1.distributednetworkmanager.DistributedNetworkAction;

import java.util.List;

/**
 * Interface defining the standard behaviour of PendingRequests.
 * A PendingRequest considers the Action used to instantiate it to have already been propagated towards
 * it's destination.
 * Actions should throw an Exception when instantiated with an invalid Action type, but will simply
 * ignore attempts to perform further steps with impertinent Actions. The User should always call
 * isPertinent() before attempting to continue the Action.
 *
 * @author Riccardo De Zen
 * @param <P> Type of Peer the implementing class handles
 */
public interface PendingRequest<P extends Peer>{

    /**
     * @return the unique Code for this PendingRequest
     */
    int getCode();

    /**
     * @param action an Action
     * @return true if the given action can be used by the Request (i.e. same type and code) false
     * otherwise.
     */
    boolean isPertinent(DistributedNetworkAction action);

    /**
     * Method to perform the next step for this PendingRequest. The User should always check with
     * isPertinent(action) beforehand to know whether the Action can be used.
     *
     * @param action the Action triggering the step
     * @return a List containing the Actions that should be performed following the step, null if list
     * would be empty
     */
    List<DistributedNetworkAction> nextStep(DistributedNetworkAction action);
}
