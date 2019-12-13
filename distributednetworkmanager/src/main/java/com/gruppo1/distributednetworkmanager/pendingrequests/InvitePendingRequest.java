package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.gruppo1.distributednetworkmanager.exceptions.CanceledRequestException;

import java.util.List;

public class InvitePendingRequest implements PendingRequest {
    private final ANSWER_CODE = KadAction...;
    private P invited;
    private int myCode;
    private boolean canceled = false;

    public InvitePendingRequest(int requestCode){
        myCode = requestCode;
        invited = startingAction.getDestination();
    }

    @Override
    public int getCode() {
        return myCode;
    }

    @Override
    public List<KadAction<P>> start(KadAction<P> startingAction) {
        if(canceled) throw new CanceledRequestException();
        invited = startingAction.getDestination();
        return null;
    }

    /**
     * Method to perform the next step for this InvitePendingRequest
     *
     * @param action the Action triggering the step
     * @return
     */
    @Override
    public List<KadAction<P>> nextStep(KadAction<P> action) {
        if(canceled) return null;
        //TODO if type matches, do the thing
        if(action.getType == INVITE_ANSWER && peer_corresponds){
            //call on listener cuz we done here boyz
        }
        return null;
    }

    @Override
    public List<KadAction<P>> cancel() {
        //call on listener for invite failed???
        return null;
    }
}
