package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.smshandler.Peer;

import java.util.List;

public interface PendingRequest<P extends Peer>{
    /**
     * @return the unique Code for this PendingRequest
     */
    int getCode();

    /**
     * Method to start the PendingRequest
     * @return A list of Actions to be performed following the startup of this Request, if any
     */
    List<KadAction<P>> start(KadAction<P> action);

    /**
     * Method to perform the next step for this PendingRequest
     * @param action the Action triggering the step
     * @return a List containing the Actions that should be performed following the step, if any
     */
    List<KadAction<P>> nextStep(KadAction<P> action);

    /**
     * Method called to cancel a PendingRequest, further calls to this Request should do nothing
     * @return A list of Actions to be performed following the cancellation of this Request, if any
     */
    List<KadAction<P>> cancel();
}
