package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.gruppo1.distributednetworkmanager.DistributedNetworkAction;
import com.gruppo1.distributednetworkmanager.exceptions.CanceledRequestException;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

import java.util.List;

class PingPendingRequest implements PendingRequest {

    private static final String CON_ERR = "Tried to initialize Ping Request with a different Request Type: ";

    private final DistributedNetworkAction pingAction;

    private boolean canceled = false;

    /**
     * Default constructor
     * @param action the Action from which to build this PendingRequest
     * @throws InvalidActionException if the Action is not a Ping Action
     */
    public PingPendingRequest(DistributedNetworkAction action, ) throws InvalidActionException{
        if(action.getAction() != DistributedNetworkAction.Type.PING)
            throw new InvalidActionException(CON_ERR);
        pingAction = action;
    }

    /**
     * @return the unique Code for this PendingRequest
     */
    @Override
    public int getCode() {
        return Integer.parseInt(pingAction.getActionID());
    }

    /**
     * @return true if the given action can be used to continue the Request (i.e. appropriate type
     * and code), and false otherwise.
     */
    @Override
    public boolean isPertinent(DistributedNetworkAction action){
        //The Request can only continue if it receives an answer to the ping
        return DistributedNetworkAction.Type.ANSWER_PING == action.getAction() &&
                pingAction.getActionID().equals(action.getActionID());
    }

    /**
     * Method called to cancel a PendingRequest, further calls to this Request's nextStep()
     * will return null
     *
     * @return A list of Actions to be performed following the cancellation of this Request, null if list
     * would be empty
     */
    @Override
    public List<DistributedNetworkAction> cancel() {
        canceled = true;
        return null;
    }

    /**
     * Method to perform the next step for this PendingRequest. Receiving a response is already
     *
     * @param action the Action triggering the step
     * @return a List containing the Actions that should be performed following the step, null if list
     * would be empty
     */
    @Override
    public List<DistributedNetworkAction> nextStep(DistributedNetworkAction action) {
        if(canceled) throw new CanceledRequestException();
        if(!isPertinent(action)) return null;

        return null;
    }
}
