package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.gruppo1.distributednetworkmanager.KadAction;

/**
 * Interface defining the standard behaviour of PendingRequests.
 * A PendingRequest considers the Action used to instantiate it to have already been propagated towards
 * it's destination.
 * Actions should throw an Exception when instantiated with an invalid Action type, but will simply
 * ignore attempts to perform further steps with impertinent Actions. The User should always call
 * isPertinent() before attempting to continue the Action.
 *
 * @author Riccardo De Zen
 */
public interface PendingRequest{

    /**
     * @return the unique Code for this PendingRequest
     */
    int getOperationId();

    /**
     * @return the Action that started this PendingRequest
     */
    KadAction getStartingAction();

    /**
     * Method used to start the PendingRequest, propagating its first Action
     */
    void start();

    /**
     * @param action an Action
     * @return true if the given action can be used by the Request (i.e. same type and code) false
     * otherwise.
     */
    boolean isActionPertinent(KadAction action);

    /**
     * Method to perform the next step for this PendingRequest. The User should always check with
     * isPertinent(action) beforehand to know whether the Action can be used.
     *
     * @param action the Action triggering the step
     */
    void nextStep(KadAction action);
}
