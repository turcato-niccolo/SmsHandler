package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.NodeUtils;
import com.gruppo1.distributednetworkmanager.listeners.InviteResultListener;

/**
 * Class defining an implementation of {@link PendingRequest} for an INVITE type Request, as defined
 * in the Kademlia protocol.
 * After completion the {@code PendingRequest} should not receive further calls to
 * {@link PendingRequest#nextStep(KadAction)}.
 *
 * @author Riccardo De Zen
 */
public class InvitePendingRequest implements PendingRequest {

    //TODO remove as soon as KadActionBuilder is ready.
    /**
     * A non-sensed payload because anything can go into the INVITE Action payload, but the
     * default constructor requires one.
     */
    private static final String PAYLOAD = "owO what's this";
    private static final int DEF_PARTS = 1;

    private int stepsTaken = 0;
    private int operationId;
    private KadAction inviteAction;
    private ActionPropagator actionPropagator;
    private NodeDataProvider<BinarySet, PeerNode> nodeProvider;
    private InviteResultListener resultListener;

    /**
     * Default constructor.
     *
     * @param operationId      the unique id for this PendingRequest operation.
     * @param peerToInvite     the {@code Peer} to ping.
     * @param actionPropagator a valid {@link ActionPropagator}.
     * @param nodeProvider     a valid {@link NodeDataProvider}.
     * @param resultListener   a valid listener to this {@code PendingRequest}'s Result.
     */
    public InvitePendingRequest(
            int operationId,
            @NonNull SMSPeer peerToInvite,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> nodeProvider,
            @NonNull InviteResultListener resultListener
    ) {
        this.operationId = operationId;
        this.actionPropagator = actionPropagator;
        this.nodeProvider = nodeProvider;
        this.resultListener = resultListener;
        this.inviteAction = buildAction(peerToInvite);
    }

    /**
     * @return the number of steps performed by the operation.
     * An {@code InvitePendingRequest} should perform only one step (when the answer to the
     * Invite came back).
     * @see PendingRequest#getTotalStepsTaken()
     */
    @Override
    public int getTotalStepsTaken() {
        return stepsTaken;
    }

    /**
     * @see PendingRequest#getOperationId()
     */
    @Override
    public int getOperationId() {
        return inviteAction.getOperationId();
    }

    /**
     * @see PendingRequest#start()
     * The only propagated Action is a Request of type
     * {@link com.gruppo1.distributednetworkmanager.KadAction.ActionType#INVITE}.
     */
    @Override
    public void start() {
        actionPropagator.propagateAction(inviteAction);
    }

    /**
     * @return true if the given action can be used to continue the operation, false otherwise.
     * The action is "pertinent" if:
     * - The {@code ActionType} of {@code action} is
     * {@link com.gruppo1.distributednetworkmanager.KadAction.ActionType#INVITE_ANSWER}.
     * - The {@code operationId} matches.
     * - The {@code Peer} of {@code action} matches the {@code Peer} of {@code inviteAction}.
     * @see PendingRequest#isActionPertinent(KadAction)
     */
    @Override
    public boolean isActionPertinent(@NonNull KadAction action) {
        return KadAction.ActionType.INVITE_ANSWER == action.getActionType() &&
                action.getOperationId() == inviteAction.getOperationId() &&
                action.getPeer().equals(inviteAction.getPeer());
    }

    /**
     * For a PING type Request, an incoming pertinent Action is already a valid completion criteria.
     *
     * @param action a pertinent Action attempting to continue the operation.
     */
    @Override
    public void nextStep(@NonNull KadAction action) {
        if (!isActionPertinent(action)) return;
        boolean inviteAccepted = Boolean.valueOf(action.getPayload());
        PeerNode invitedNode = NodeUtils.getNodeForPeer(action.getPeer(),
                NodeUtils.DEFAULT_KEY_LENGTH);
        nodeProvider.visitNode(invitedNode);
        resultListener.onInviteResult(getOperationId(), invitedNode, inviteAccepted);
        stepsTaken++;
    }

    /**
     * //TODO remove as soon as KadActionBuilder is completed.
     * Method to return the correct Action for a Peer
     *
     * @param peer
     * @return
     */
    private KadAction buildAction(SMSPeer peer) {
        return new KadAction(
                peer,
                KadAction.ActionType.INVITE, operationId,
                DEF_PARTS,
                DEF_PARTS,
                KadAction.PayloadType.IGNORED, PAYLOAD
        );
    }
}
