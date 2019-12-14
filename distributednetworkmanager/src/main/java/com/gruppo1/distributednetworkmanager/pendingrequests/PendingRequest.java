package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.smshandler.Peer;
import com.gruppo1.distributednetworkmanager.DistributedNetworkAction;

import java.util.List;

/**
 * Interface defining the standard behaviour of PendingRequests. A canceled PendingRequest should be
 * made inaccessible, because of this it's not advisable to pass PendingRequests as parameters.
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

    /**
     * Method called to cancel a PendingRequest, further calls to this Request will return null
     *
     * @return A list of Actions to be performed following the cancellation of this Request, null if list
     * would be empty
     */
    List<DistributedNetworkAction> cancel();
}
