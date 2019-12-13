package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.smshandler.Peer;

import java.util.List;

/**
 * Interface defining the standard behaviour of PendingRequests. A canceled PendingRequest should be
 * made inaccessible, because of this it's not advisable to pass PendingRequests as parameters.
 * @author Riccardo De Zen
 * @param <P> Type of Peer the implementing class handles
 */
public interface PendingRequest<P extends Peer>{
    /**
     * @return the unique Code for this PendingRequest
     */
    int getCode();

    /**
     * Method to start the PendingRequest
     * @return A list of Actions to be performed following the startup of this Request, null if list
     * would be empty
     */
    List<KadAction<P>> start(KadAction<P> action);

    /**
     * Method to perform the next step for this PendingRequest
     * @param action the Action triggering the step
     * @return a List containing the Actions that should be performed following the step, null if list
     * would be empty
     */
    List<KadAction<P>> nextStep(KadAction<P> action);

    /**
     * Method called to cancel a PendingRequest, further calls to this Request will return null
     * @return A list of Actions to be performed following the cancellation of this Request, null if list
     * would be empty
     */
    List<KadAction<P>> cancel();
}
