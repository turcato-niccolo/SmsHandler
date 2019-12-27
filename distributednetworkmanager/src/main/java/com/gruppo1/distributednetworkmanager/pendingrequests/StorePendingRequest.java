package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.NodeUtils;
import com.gruppo1.distributednetworkmanager.listeners.StoreResultListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class StorePendingRequest implements PendingRequest {

    /**
     * Enum defining possible States for this PendingRequest:
     * SEARCHING: The PendingRequest is searching for the most suitable new Owner.
     * STORING: The PendingRequest is trying to store the Resource into the Owner found in the
     * previous phase.
     */
    private enum RequestState {
        SEARCHING,
        STORING
    }

    private static final String SEPARATOR = "\r";
    private static final int DEF_PARTS = 1;
    private static final int K = 5;
    private static final int N = 128;

    private int totalStepsTaken = 0;
    private RequestState requestState = RequestState.SEARCHING;
    private int operationId;
    private StringResource resourceToStore;
    private BinarySet targetId;
    private ActionPropagator actionPropagator;
    private NodeDataProvider<BinarySet, PeerNode> nodeProvider;
    private StoreResultListener resultListener;

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
     * @param operationId      the unique id for this {@code PendingRequest} operation.
     * @param resourceToStore  the {@code Resource} that needs to be relocated.
     * @param actionPropagator a valid {@link ActionPropagator}.
     * @param nodeProvider     a valid {@link NodeDataProvider}.
     * @param resultListener   a valid listener to this {@code PendingRequest}'s Result.
     */
    public StorePendingRequest(
            int operationId,
            @NonNull StringResource resourceToStore,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> nodeProvider,
            @NonNull StoreResultListener resultListener
    ) {
        this.operationId = operationId;
        this.resourceToStore = resourceToStore;
        this.targetId = new BinarySet(NodeUtils.getIdForResource(resourceToStore, N));
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
     * A {@link StorePendingRequest} propagates a fixed amount of Actions of type
     * {@link com.gruppo1.distributednetworkmanager.KadAction.ActionType#STORE} on startup.
     */
    @Override
    public void start() {
        List<PeerNode> closestNodes = nodeProvider.getKClosest(K, targetId);
        propagateSearch(closestNodes);
    }

    /**
     * @return true if the given action can be used to continue the operation, false otherwise.
     * The action is "pertinent" if:
     * - The {@code ActionType} of {@code action} is
     * {@link com.gruppo1.distributednetworkmanager.KadAction.ActionType#STORE_ANSWER}.
     * - The {@code operationId} matches.
     * @see PendingRequest#isActionPertinent(KadAction)
     */
    @Override
    public boolean isActionPertinent(@NonNull KadAction action) {
        return KadAction.ActionType.STORE_ANSWER == action.getActionType() &&
                getOperationId() == action.getOperationId();
    }

    /**
     * @param action a pertinent Action attempting to continue the operation.
     */
    @Override
    public void nextStep(@NonNull KadAction action) {
        if (!isActionPertinent(action)) return;
        switch (requestState){
            case SEARCHING:
                handleSearchResponse(action);
                break;
            case STORING:
                handleStoreResponse(action);
                break;
        }
        totalStepsTaken++;
    }

    /**
     * Method adding a Node to {@link StorePendingRequest#visitedNodes}, with the distance
     * from the target Node as its key.
     * Also notifies {@link StorePendingRequest#nodeProvider} that the Node has been visited.
     *
     * @param visitedNode the Node to be marked as visited.
     */
    private void markVisited(PeerNode visitedNode) {
        visitedNodes.put(targetId.getDistance(visitedNode.getAddress()), visitedNode);
        nodeProvider.visitNode(visitedNode);
    }

    /**
     * Method used to handle a phase 1 Response, working with the various Node Maps and Sets
     * associated
     * with this {@code PendingRequest}.
     *
     * @param action the Response Action. Must be pertinent.
     */
    private void handleSearchResponse(@NonNull KadAction action) {
        PeerNode sender = NodeUtils.getNodeForPeer(action.getPeer(), N);
        if (pendingResponses.contains(sender)) {
            markVisited(sender);
            pendingResponses.remove(sender);
            expectedResponses += action.getTotalParts();
        }
        if (action.getPayloadType() != KadAction.PayloadType.PEER_ADDRESS) return;
        PeerNode responseNode = NodeUtils.getNodeForPeer(new SMSPeer(action.getPayload()), N);
        if (!visitedNodes.containsValue(responseNode))
            peerBuffer.add(responseNode);
        expectedResponses--;
        checkStatus();
    }

    /**
     * Method used to handle a Response during , working with the various Node Maps and Sets
     * associated
     * with this {@code PendingRequest}.
     *
     * @param action the Response Action. Must be pertinent.
     */
    private void handleStoreResponse(@NonNull KadAction action) {
        if(action.getPayloadType() != KadAction.PayloadType.BOOLEAN) return;
        if(Boolean.parseBoolean(action.getPayload()))
            storeSuccess(NodeUtils.getNodeForPeer(action.getPeer(), N));
        else
            storeFail();
    }

    /**
     * Method determining which state the {@code PendingRequest} is in. The states are defined as
     * follows:
     * - WAITING: not all expected Responses came from all Nodes that were expected to answer.
     * - ROUND_FINISHED: all expected Responses came, but some Nodes closer to our target are in
     * {@link StorePendingRequest#peerBuffer}, so another round of Request propagation is due.
     * - OWNER_FOUND: same as round finished but no closer Nodes are available and the
     * Resource hasn't been found, which means the Resource doesn't exist.
     */
    private void checkStatus() {
        if (!(pendingResponses.isEmpty() && expectedResponses == 0)) return;
        if (peerBuffer.isEmpty()) {
            onOwnerFound();
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
        propagateSearch(newClosest);
    }

    /**
     * This method should only be called during OWNER_FOUND state, it completes the Request.
     * If no Node has been visited, it means I'm the best Owner for this Resource.
     */
    private void onOwnerFound() {
        PeerNode closestNode = visitedNodes.get(visitedNodes.firstKey());
        if (closestNode != null) {
            propagateStore(closestNode);
        } else {
            storeFail();
        }
    }

    /**
     * Method to be called if the Store operation was overall successful.
     * @param newOwner the new owner for the {@code Resource}.
     */
    private void storeSuccess(PeerNode newOwner){
        resultListener.onStoreResult(operationId, resourceToStore, newOwner);
    }

    /**
     * Method to be called if the Store operation was overall successful.
     */
    private void storeFail(){
        resultListener.onStoreResult(operationId, resourceToStore, null);
    }

    /**
     * Method to propagate an Action to all the peerNodes in a given list.
     *
     * @param peerNodes a list containing PeerNodes.
     */
    private void propagateSearch(List<PeerNode> peerNodes) {
        List<KadAction> actions = new ArrayList<>();
        for (PeerNode node : peerNodes) {
            actions.add(buildSearchAction(node.getPhysicalPeer()));
        }
        actionPropagator.propagateActions(actions);
    }

    /**
     * Method to propagate the final STORE Request.
     *
     * @param owner the target for the final Request.
     */
    private void propagateStore(PeerNode owner) {
        actionPropagator.propagateAction(buildStoreAction(owner.getPhysicalPeer()));
    }

    /**
     * //TODO remove once KadActionBuilder is ready
     *
     * @param peer
     * @return
     */
    private KadAction buildSearchAction(SMSPeer peer) {
        return new KadAction(
                peer,
                KadAction.ActionType.STORE,
                operationId,
                DEF_PARTS,
                DEF_PARTS,
                KadAction.PayloadType.NODE_ID,
                BitSetUtils.BitSetsToHex(targetId.getKey())
        );
    }

    private KadAction buildStoreAction(SMSPeer peer) {
        return new KadAction(
                peer,
                KadAction.ActionType.STORE,
                operationId,
                DEF_PARTS,
                DEF_PARTS,
                KadAction.PayloadType.RESOURCE,
                resourceToStore.getName() + SEPARATOR + resourceToStore.getValue()
        );
    }
}
