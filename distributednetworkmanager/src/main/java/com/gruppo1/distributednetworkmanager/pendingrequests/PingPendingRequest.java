package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.DistributedNetworkAction;
import com.gruppo1.distributednetworkmanager.RequestResultListener;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

import java.util.List;

class PingPendingRequest implements PendingRequest<SMSPeer>{

    private static final String CON_ERR = "Tried to initialize Ping Request with a different Request Type: ";

    private final DistributedNetworkAction pingAction;
    private final RequestResultListener<SMSPeer> resultListener;

    /**
     * Default constructor
     * @param action the Action from which to build this PendingRequest. Must not be null
     * @param listener the listener to this Request's Result. Must not be null.
     * @throws InvalidActionException if the Action is not a Ping Action
     */
    public PingPendingRequest(@NonNull DistributedNetworkAction action, @NonNull RequestResultListener<SMSPeer> listener) throws InvalidActionException{
        if(action.getAction() != DistributedNetworkAction.Type.PING)
            throw new InvalidActionException(CON_ERR);
        pingAction = action;
        resultListener = listener;
    }

    /**
     * @return the unique Code for this PendingRequest
     */
    @Override
    public int getCode() {
        return Integer.parseInt(pingAction.getActionID());
    }

    /**
     * @return true if the given action can be used to continue the Request, false otherwise. The action
     * can be used if Type and ID match, and if this request hasn't been canceled yet.
     */
    @Override
    public boolean isPertinent(DistributedNetworkAction action){
        //The Request can only continue if it receives an answer to the ping
        return DistributedNetworkAction.Type.ANSWER_PING == action.getAction() &&
                pingAction.getActionID().equals(action.getActionID());
    }

    /**
     * Method to perform the next step for this PendingRequest. Receiving a response is already
     *
     * @param action the Action triggering the step
     * @return null
     */
    @Override
    public List<DistributedNetworkAction> nextStep(DistributedNetworkAction action) {
        if(!isPertinent(action)) return null;
        //The Ping, one way or another, came back, so the result is positive and no further Actions are needed
        resultListener.onPingResult(action.getSender(), true);
        return null;
    }
}
