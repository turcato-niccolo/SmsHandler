package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.NodeUtils;
import com.gruppo1.distributednetworkmanager.listeners.PingResultListener;

/**
 * Class defining an implementation of {@link PendingRequest} for a PING type request, as defined
 * in the Kademlia protocol.
 * After completion the {@code PendingRequest} should not receive further calls to
 * {@link PendingRequest#nextStep(KadAction)}.
 *
 * @author Riccardo De Zen
 */
public class PingPendingRequest implements PendingRequest {

    private static final String PAYLOAD = "owO what's this";
    private static final int DEF_PARTS = 1;

    private int totalStepsTaken = 0;
    private int operationId;
    private ActionPropagator actionPropagator;
    private NodeDataProvider<BinarySet, PeerNode> nodeProvider;
    private PingResultListener resultListener;
    private KadAction pingAction;

    /**
     * Default constructor.
     *
     * @param operationId      the unique id for this PendingRequest operation.
     * @param peerToPing       the {@code Peer} to ping.
     * @param actionPropagator a valid {@link ActionPropagator}.
     * @param nodeProvider     a valid {@link NodeDataProvider}.
     * @param resultListener   a valid listener to this {@code PendingRequest}'s Result.
     */
    public PingPendingRequest(
            int operationId,
            @NonNull SMSPeer peerToPing,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> nodeProvider,
            @NonNull PingResultListener resultListener
    ) {
        this.operationId = operationId;
        this.actionPropagator = actionPropagator;
        this.nodeProvider = nodeProvider;
        this.resultListener = resultListener;
        this.pingAction = buildAction(peerToPing);
    }

    /**
     * @return the number of steps performed by the operation.
     * A {@code PingPendingRequest} should perform only one step (when the answer to the Ping
     * came back).
     * @see PendingRequest#getTotalStepsTaken()
     */
    @Override
    public int getTotalStepsTaken() {
        return totalStepsTaken;
    }

    /**
     * @see PendingRequest#getOperationId()
     */
    @Override
    public int getOperationId() {
        return pingAction.getOperationId();
    }

    /**
     * @see PendingRequest#start()
     * The only propagated Action is a Request of type
     * {@link com.gruppo1.distributednetworkmanager.KadAction.ActionType#PING}.
     */
    @Override
    public void start() {
        actionPropagator.propagateAction(pingAction);
    }

    /**
     * @return true if the given action can be used to continue the operation, false otherwise.
     * The action is "pertinent" if:
     * - The {@code ActionType} of {@code action} is
     * {@link com.gruppo1.distributednetworkmanager.KadAction.ActionType#PING_ANSWER}.
     * - The {@code operationId} matches.
     * - The {@code Peer} of {@code action} matches the {@code Peer} of {@code pingAction}.
     * @see PendingRequest#isActionPertinent(KadAction)
     */
    @Override
    public boolean isActionPertinent(@NonNull KadAction action) {
        return KadAction.ActionType.PING_ANSWER == action.getActionType() &&
                pingAction.getOperationId() == action.getOperationId() &&
                pingAction.getPeer().equals(action.getPeer());
    }

    /**
     * For a PING type Request, an incoming pertinent Action is already a valid completion criteria.
     *
     * @param action a pertinent Action attempting to continue the operation.
     */
    @Override
    public void nextStep(@NonNull KadAction action) {
        if (!isActionPertinent(action)) return;
        PeerNode pingedNode = NodeUtils.getNodeForPeer(action.getPeer(),
                NodeUtils.DEFAULT_KEY_LENGTH);
        nodeProvider.visitNode(pingedNode);
        resultListener.onPingResult(getOperationId(), pingedNode, true);
        totalStepsTaken++;
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
                KadAction.ActionType.PING, operationId,
                DEF_PARTS,
                DEF_PARTS,
                KadAction.PayloadType.IGNORED, PAYLOAD
        );
    }
}
