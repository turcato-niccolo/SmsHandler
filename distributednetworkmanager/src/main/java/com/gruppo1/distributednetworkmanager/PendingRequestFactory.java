package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.smshandler.Peer;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidRequestException;

import java.util.List;

/**
 * Class defining the various PendingRequest behaviours
 * @param <P> the type of Peer the PendingRequests should use
 */
public class PendingRequestFactory<P extends Peer>{

    private static final String INVALID_ACTION_ERR = "The provided Action is invalid, only Request " +
            "type Actions can be used.";

    public PendingRequest<P> getPendingRequest(KadAction<P> startingAction, int requestCode)
            throws InvalidRequestException{
        //Getting the type of the action and returning the appropriate PendingRequest implementation
        switch(startingAction.getType()){
            case KadAction.Request.INVITE:
                return new InvitePendingRequest(startingAction, requestCode);
            case KadAction.Request.PING:
                break;
                //TODO rest of actions
            default:
                throw new InvalidRequestException(INVALID_ACTION_ERR);
        }
        return null;
    }

    private class InvitePendingRequest implements PendingRequest<P>{
        private P invited;
        private int myCode;
        public InvitePendingRequest(KadAction<P> startingAction, int requestCode){
            myCode = requestCode;
            invited = startingAction.getDestination();
        }

        @Override
        public int getCode() {
            return myCode;
        }

        @Override
        public void start() {

        }

        /**
         * Method to perform the next step for this InvitePendingRequest
         *
         * @param action the Action triggering the step
         * @return
         */
        @Override
        public List<KadAction<P>> nextStep(KadAction<P> action) {
            return null;
        }

        @Override
        public void cancel() {

        }
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
