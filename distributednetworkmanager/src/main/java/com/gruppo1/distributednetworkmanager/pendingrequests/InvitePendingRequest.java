package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.DistributedNetworkAction;
import com.gruppo1.distributednetworkmanager.RequestResultListener;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

import java.util.List;

public class InvitePendingRequest implements PendingRequest<SMSPeer>{

    private static final String CON_ERR = "Tried to initialize Ping Request with a different Request Type: ";
    private static final String INVITE_ACCEPTED = "1";

    private DistributedNetworkAction inviteAction;
    private RequestResultListener<SMSPeer> resultListener;

    public InvitePendingRequest(@NonNull DistributedNetworkAction action, @NonNull RequestResultListener<SMSPeer> listener) throws InvalidActionException {
        if(action.getAction() != DistributedNetworkAction.Type.INVITE)
            throw new InvalidActionException();
        inviteAction = action;
        resultListener = listener;
    }

    /**
     * @return the unique Code for this PendingRequest
     */
    @Override
    public int getCode() {
        return Integer.parseInt(inviteAction.getActionID());
    }

    /**
     * @param action an Action
     * @return true if the given action can be used by the Request (i.e. same type and code) false
     * otherwise.
     */
    @Override
    public boolean isPertinent(DistributedNetworkAction action){
        return DistributedNetworkAction.Type.ANSWER_INVITE == action.getAction() &&
                action.getActionID().equals(inviteAction.getActionID()) &&
                action.getSender().equals(inviteAction.getDestination());
    }

    /**
     * Method to perform the next step for this Invite Request.
     * If the Result came back, then the User answered to the invitation, and no further Action is needed.
     *
     * @param action the Action triggering the step
     * @return null
     */
    @Override
    public List<DistributedNetworkAction> nextStep(DistributedNetworkAction action) {
        if(!isPertinent(action)) return null;
        boolean inviteAccepted = action.getPayload().equals(INVITE_ACCEPTED);
        resultListener.onInviteResult(action.getSender(), inviteAccepted);
        return null;
    }
}
