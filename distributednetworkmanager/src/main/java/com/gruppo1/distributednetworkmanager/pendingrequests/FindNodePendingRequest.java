package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.NodeUtils;
import com.gruppo1.distributednetworkmanager.listeners.FindNodeResultListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Class defining an implementation of {@link PendingRequest} for a FIND_NODE type Request, as
 * defined in the Kademlia protocol.
 * After completion the {@code PendingRequest} should not receive further calls to
 * {@link PendingRequest#nextStep(KadAction)}.
 *
 * @author Riccardo De Zen
 */
public class FindNodePendingRequest implements PendingRequest {

    private static final int DEF_PARTS = 1;
    private static final int K = 5;
    private static final int N = 128;

    private int totalStepsTaken = 0;
    private int operationId;
    private BinarySet targetId;
    private ActionPropagator actionPropagator;
    private NodeDataProvider<BinarySet, PeerNode> nodeProvider;
    private FindNodeResultListener resultListener;

    //Map used to keep track of all visited Nodes
    private TreeMap<BinarySet, PeerNode> visitedNodes = new TreeMap<>();

    //Set used to keep track of the Peers from which we are still waiting for a Response.
    private Set<PeerNode> pendingResponses = new TreeSet<>();

    //Set used to keep track of the Peers we received as Responses from the Peers we contacted.
    private Set<PeerNode> peerBuffer = new TreeSet<>();

    //The number of Responses we expect. It increases every time a new Node Response and it
    // decreases by one for every received Response.
    private int expectedResponses = 0;

    /**
     * Default constructor.
     *
     * @param operationId      the unique id for this PendingRequest operation.
     * @param targetId         the id of the Node we are looking for.
     * @param actionPropagator a valid {@link ActionPropagator}.
     * @param nodeProvider     a valid {@link NodeDataProvider}.
     * @param resultListener   a valid listener to this {@code PendingRequest}'s Result.
     */
    public FindNodePendingRequest(
            int operationId,
            @NonNull BinarySet targetId,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> nodeProvider,
            @NonNull FindNodeResultListener resultListener
    ) {
        this.operationId = operationId;
        this.targetId = targetId;
        this.actionPropagator = actionPropagator;
        this.nodeProvider = nodeProvider;
        this.resultListener = resultListener;
    }

    /**
     * @return the number of steps performed by the operation.
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
        return operationId;
    }

    /**
     * @see PendingRequest#start()
     * A {@link FindNodePendingRequest} propagates a fixed amount of Actions of type
     * {@link com.gruppo1.distributednetworkmanager.KadAction.ActionType#FIND_NODE} on startup.
     */
    @Override
    public void start() {
        List<PeerNode> closestNodes = nodeProvider.getKClosest(K, targetId);
        propagateToAll(closestNodes);
    }

    /**
     * @return true if the given action can be used to continue the operation, false otherwise.
     * The action is "pertinent" if:
     * - The {@code ActionType} of {@code action} is
     * {@link com.gruppo1.distributednetworkmanager.KadAction.ActionType#FIND_NODE_ANSWER}.
     * - The {@code operationId} matches.
     * @see PendingRequest#isActionPertinent(KadAction)
     */
    @Override
    public boolean isActionPertinent(@NonNull KadAction action) {
        return KadAction.ActionType.FIND_NODE_ANSWER == action.getActionType() &&
                getOperationId() == action.getOperationId();
    }

    /**
     * @param action a pertinent Action attempting to continue the operation.
     */
    @Override
    public void nextStep(@NonNull KadAction action) {
        if (!isActionPertinent(action)) return;
        handleResponse(action);
        checkStatus();
        totalStepsTaken++;
    }

    /**
     * Method adding a Node to {@link FindNodePendingRequest#visitedNodes}, with the distance
     * from the target Node as its key.
     * Also notifies {@link FindNodePendingRequest#nodeProvider} that the Node has been visited.
     *
     * @param visitedNode the Node to be marked as visited.
     */
    private void markVisited(PeerNode visitedNode) {
        visitedNodes.put(targetId.getDistance(visitedNode.getAddress()), visitedNode);
        nodeProvider.visitNode(visitedNode);
    }

    /**
     * Method used to handle a Response, working with the various Node Maps and Sets associated
     * with this {@code PendingRequest}.
     *
     * @param action the Response Action. Must be pertinent.
     */
    private void handleResponse(@NonNull KadAction action) {
        PeerNode sender = NodeUtils.getNodeForPeer(action.getPeer(), N);
        if (pendingResponses.contains(sender)) {
            markVisited(sender);
            pendingResponses.remove(sender);
            expectedResponses += action.getTotalParts();
        }
        //Following condition should always be true. The check is just a fail-safe.
        if (action.getPayloadType() != KadAction.PayloadType.PEER_ADDRESS) return;
        PeerNode responseNode = NodeUtils.getNodeForPeer(new SMSPeer(action.getPayload()), N);
        if(!visitedNodes.containsValue(responseNode))
            peerBuffer.add(responseNode);
        expectedResponses--;
    }

    /**
     * Method determining which state the {@code PendingRequest} is in. The states are defined as
     * follows:
     * - WAITING: not all expected Responses came from all Nodes that were expected to answer.
     * - ROUND_FINISHED: all expected Responses came, but some Nodes closer to our target are in
     * {@link FindNodePendingRequest#peerBuffer}, so another round of Request propagation is due.
     * - CLOSEST_FOUND: same as round finished but no closer Nodes are available. Which means the
     * closest reachable Node has already been encountered and is in
     * {@link FindNodePendingRequest#visitedNodes}.
     */
    private void checkStatus() {
        if (!(pendingResponses.isEmpty() && expectedResponses == 0)) return;
        if (peerBuffer.isEmpty()) {
            finalStep();
        } else nextRoundOfRequests();
    }

    /**
     * Method to perform the next round of Requests, should only be called while in
     * ROUND_FINISHED state.
     */
    private void nextRoundOfRequests() {
        //Converting Set to List to be used as parameter.
        List<PeerNode> listBuffer = Arrays.asList(peerBuffer.toArray(new PeerNode[0]));
        List<PeerNode> newClosest = nodeProvider.filterKClosest(K, targetId, listBuffer);

        pendingResponses.addAll(newClosest);
        peerBuffer.clear();
        propagateToAll(newClosest);
    }

    /**
     * This method should only be called while in CLOSEST_FOUND state, it completes the Request.
     * If no Nodes were added to {@link FindNodePendingRequest#visitedNodes} it probably means I
     * am the closest Node I know to the target id.
     */
    private void finalStep() {
        PeerNode closestNode = visitedNodes.get(visitedNodes.firstKey());
        if (closestNode != null) {
            resultListener.onFindNodeResult(operationId, targetId.getKey(), closestNode);
        } else {
            resultListener.onFindNodeResult(operationId, targetId.getKey(), null);
        }
    }

    /**
     * Method to propagate an Action to all the peerNodes in a given list.
     *
     * @param peerNodes a list containing PeerNodes
     */
    private void propagateToAll(List<PeerNode> peerNodes) {
        List<KadAction> actions = new ArrayList<>();
        for (PeerNode node : peerNodes) {
            actions.add(buildAction(node.getPhysicalPeer()));
        }
        actionPropagator.propagateActions(actions);
    }

    /**
     * //TODO remove once KadActionBuilder is ready
     *
     * @param peer
     * @return
     */
    private KadAction buildAction(SMSPeer peer) {
        return new KadAction(
                peer,
                KadAction.ActionType.FIND_NODE,
                operationId,
                DEF_PARTS,
                DEF_PARTS,
                KadAction.PayloadType.NODE_ID,
                BitSetUtils.BitSetsToHex(targetId.getKey())
        );
    }
}
