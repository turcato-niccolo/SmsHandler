package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.BinarySet;
import com.gruppo1.distributednetworkmanager.BitSetUtils;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.NodeUtils;
import com.gruppo1.distributednetworkmanager.PeerNode;
import com.gruppo1.distributednetworkmanager.listeners.FindNodeResultListener;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class FindNodePendingRequest implements PendingRequest {

    private static final int DEF_PARTS = 1;
    private static final int K = 5;
    private static final int N = 128;

    private int stepsTaken = 0;
    private int operationId;
    private BinarySet targetId;
    private ActionPropagator actionPropagator;
    private NodeDataProvider<BinarySet, PeerNode> nodeProvider;
    private FindNodeResultListener resultListener;

    private TreeMap<BinarySet, PeerNode> visitedNodes = new TreeMap<>();
    private Set<PeerNode> peerBuffer = new TreeSet<>();
    private Set<PeerNode> waitingForAnswer = new TreeSet<>();
    private int neededResponses = 0;

    /**
     * Default constructor
     * @param targetId the target Node
     * @param actionPropagator
     * @param nodeProvider
     * @param resultListener the listener to this Request's Result. Must not be null.
     */
    public FindNodePendingRequest(
            int operationId,
            @NonNull BinarySet targetId,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> nodeProvider,
            @NonNull FindNodeResultListener resultListener
    ){
        this.operationId = operationId;
        this.targetId = targetId;
        this.actionPropagator = actionPropagator;
        this.nodeProvider = nodeProvider;
        this.resultListener = resultListener;
    }

    /**
     * @return the number of steps performed (number of times nextStep took a valid Action and acted
     * accordingly). Should be either 0 or 1.
     */
    @Override
    public int getStepsTaken(){
        return stepsTaken;
    }

    /**
     * @return the unique Code for this PendingRequest
     */
    @Override
    public int getOperationId() {
        return operationId;
    }

    /**
     * Method used to start the PendingRequest, propagating its first Action
     */
    @Override
    public void start(){
        List<PeerNode> closestNodes = nodeProvider.getKClosest(K,targetId);
        for(PeerNode node : closestNodes){
            waitingForAnswer.add(node);
            KadAction findNodeAction = buildAction(node.getPhysicalPeer());
            actionPropagator.propagateAction(findNodeAction);
        }
    }

    /**
     * @return true if the given action can be used to continue the Request.
     * The Action is pertinent if it contains a Find Node Response with a matching id and containing
     * the address of a Peer
     */
    @Override
    public boolean isActionPertinent(KadAction action){
        return KadAction.ActionType.FIND_NODE_ANSWER == action.getActionType() &&
                getOperationId() == action.getOperationId();
    }

    /**
     * Method to perform the next step for this PendingRequest. Receiving a response is already
     *
     * @param action the Action triggering the step
     */
    @Override
    public void nextStep(KadAction action) {
        if(!isActionPertinent(action)) return;
        handleResponse(action);
        checkStatus();
        stepsTaken++;
    }

    /**
     * Method adding a Node to the visitedNodes map, with the distance from the target Node as its key
     * @param nodeToMark the Node to be added to the set
     */
    private void markVisited(PeerNode nodeToMark){
        visitedNodes.put(targetId.getDistance(nodeToMark.getAddress()), nodeToMark);
    }

    /**
     *
     * @param action
     */
    private void handleResponse(KadAction action){
        PeerNode sender = NodeUtils.getNodeForPeer(action.getPeer(), N);
        if(waitingForAnswer.contains(sender)){
            markVisited(sender);
            waitingForAnswer.remove(sender);
            neededResponses += action.getTotalParts();
        }
        if(action.getPayloadType() == KadAction.PayloadType.PEER_ADDRESS){
            PeerNode responseNode = NodeUtils.getNodeForPeer(new SMSPeer(action.getPayload()), N);
            if(!visitedNodes.containsValue(responseNode))
                peerBuffer.add(responseNode);
        }
        neededResponses--;
    }

    /**
     *
     */
    private void checkStatus(){
        if(waitingForAnswer.isEmpty() && neededResponses == 0){
            if(peerBuffer.isEmpty()){
                finalStep();
            }
            else nextRoundOfRequests();
        }
    }

    /**
     * The buffer is empty, so no better solutions have been found than the ones in the backup buffer
     */
    private void finalStep(){
        PeerNode closestNode = visitedNodes.get(visitedNodes.firstKey());
        if(closestNode != null){
            SMSPeer closestPeer = closestNode.getPhysicalPeer();
            resultListener.onFindNodeResult(operationId,targetId.getKey(),closestPeer);
        }
        else{
            //TODO I have visited nobody, I'm probably the closest
        }
    }

    /**
     * Method to perform the next round of Requests
     */
    private void nextRoundOfRequests(){
        List<PeerNode> listBuffer = Arrays.asList(peerBuffer.toArray(new PeerNode[0]));
        List<PeerNode> newClosest = nodeProvider.filterKClosest(K, targetId,listBuffer);
        propagateToAll(newClosest);
        peerBuffer = new ArraySet<>();
    }

    /**
     * Method to propagate an Action to all the peerNodes in a list
     * @param peerNodes a list containing PeerNodes
     */
    private void propagateToAll(List<PeerNode> peerNodes){
        for(PeerNode node : peerNodes){
            actionPropagator.propagateAction(buildAction(node.getPhysicalPeer()));
        }
    }

    /**
     * //TODO specs
     * @param peer
     * @return
     */
    private KadAction buildAction(SMSPeer peer){
        System.out.println(BitSetUtils.BitSetsToHex(targetId.getKey()));
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
