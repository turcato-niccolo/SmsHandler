package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.Peer;
import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.DistributedNetworkAction;
import com.gruppo1.distributednetworkmanager.RequestResultListener;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

/**
 * Class defining the various PendingRequest behaviours
 * @param <P> the type of Peer the PendingRequests should use
 */
public class PendingRequestFactory<P extends Peer>{
    //TODO generification
    private static final String INVALID_ACTION_ERR = "The provided Action is invalid, only Request " +
            "type Actions can be used.";

    /**
     * Method to get the appropriate PendingRequest implementation based on the desired Request
     * @param action containing all the info about the Request
     * @return the built PendingRequest
     * @throws InvalidActionException if the supplied requestType does not correspond to a Request
     */
    public PendingRequest<SMSPeer> getPendingRequest(@NonNull DistributedNetworkAction action, @NonNull RequestResultListener<SMSPeer> listener) throws InvalidActionException {
        //Getting the type of the action and returning the appropriate PendingRequest implementation
        switch(action.getAction()){
            case DistributedNetworkAction.Type.INVITE:
                return new InvitePendingRequest(action, listener);
            case DistributedNetworkAction.Type.PING:
                return new PingPendingRequest(action, listener);
                //TODO other actions
            default:
                throw new InvalidActionException(INVALID_ACTION_ERR);
        }
    }
}
