package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;
import com.gruppo1.distributednetworkmanager.listeners.InviteResultListener;

public class InvitePendingRequest implements PendingRequest{

    private static final String CON_ERR = "Tried to initialize Invite Request with a different Request Type: ";

    private KadAction inviteAction;
    private ActionPropagator actionPropagator;
    private NodeDataProvider nodeProvider;
    private InviteResultListener resultListener;

    /**
     * Default constructor
     * @param action
     * @param actionPropagator
     * @param nodeProvider
     * @param resultListener
     * @throws InvalidActionException
     */
    public InvitePendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull InviteResultListener resultListener
    ) throws InvalidActionException {
        if(action.getActionType() != KadAction.ActionType.INVITE)
            throw new InvalidActionException(CON_ERR+action.getActionType());
        inviteAction = action;
        this.resultListener = resultListener;
    }

    /**
     * @return the unique Code for this PendingRequest
     */
    @Override
    public int getOperationId() {
        return inviteAction.getOperationId();
    }

    /**
     * @return the Action that started this PingPendingRequest.
     */
    @Override
    public KadAction getStartingAction(){
        return inviteAction;
    }

    /**
     * Method used to start the PendingRequest, propagating its first Action
     */
    @Override
    public void start(){
        actionPropagator.propagateAction(inviteAction);
    }

    /**
     * @param action an Action
     * @return true if the given action can be used by the Request (i.e. same type and code) false
     * otherwise.
     */
    @Override
    public boolean isActionPertinent(KadAction action){
        return KadAction.ActionType.INVITE_ANSWER == action.getActionType() &&
                action.getOperationId() == inviteAction.getOperationId() &&
                action.getPeer().equals(inviteAction.getPeer());
    }

    /**
     * Method to perform the next step for this Invite Request.
     * If the Result came back, then the User answered to the invitation, and no further Action is needed.
     *
     * @param action the Action triggering the step
     */
    @Override
    public void nextStep(KadAction action) {
        if(!isActionPertinent(action)) return;
        boolean inviteAccepted = Boolean.valueOf(action.getPayload());
        resultListener.onInviteResult(getOperationId(), action.getPeer(), inviteAccepted);
    }
}
