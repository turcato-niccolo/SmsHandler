package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.smshandler.Peer;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

/**
 * Class defining the various PendingRequest behaviours
 * @param <P> the type of Peer the PendingRequests should use
 */
public class PendingRequestFactory<P extends Peer>{

    private static final String INVALID_ACTION_ERR = "The provided Action is invalid, only Request " +
            "type Actions can be used.";
    private static final String CANCELED_REQUEST_ERR = "Tried to access a PendingRequest that already " +
            "got canceled.";

    /**
     * Method to get the appropriate PendingRequest implementation based on the desired Request
     * @param requestType the type of Request
     * @param requestCode the unique code for the Request
     * @return the built PendingRequest
     * @throws InvalidActionException if the supplied requestType does not correspond to a Request
     */
    public PendingRequest<P> getPendingRequest(int requestType, int requestCode)
            throws InvalidActionException {
        //Getting the type of the action and returning the appropriate PendingRequest implementation
        switch(requestType){
            case KadAction.Request.INVITE:
                return new InvitePendingRequest(requestCode);
            case KadAction.Request.PING:
                break;
                //TODO rest of actions
            default:
                throw new InvalidActionException(INVALID_ACTION_ERR);
        }
        return null;
    }

    /**
     * Class defining the Ping Request
     */
    /*private class PingPendingRequest implements PendingRequest<P>{

    }*/

    /**
     * Class defining the Store Request
     */
    /*private class StorePendingRequest implements PendingRequest<P>{

    }*/

    /**
     * Class defining the Find Node Request
     */
    /*private class FindNodePendingRequest implements PendingRequest<P>{

    }*/

    /**
     * Class defining the Find Value Request
     */
    /*private class FindValuePendingRequest implements PendingRequest<P>{

    }*/
}
