package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.BinarySet;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.NodeUtils;
import com.gruppo1.distributednetworkmanager.PeerNode;
import com.gruppo1.distributednetworkmanager.listeners.FindNodeResultListener;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FindNodePendingRequest implements PendingRequest {

    private static final int K = 5;
    private static final int N = 128;

    private int stepsTaken = 0;
    private int operationId;
    private PeerNode target;
    private ActionPropagator actionPropagator;
    private NodeDataProvider<BinarySet, PeerNode> nodeProvider;
    private FindNodeResultListener resultListener;

    private Set<PeerNode> backupBuffer = new ArraySet<>();
    private Set<PeerNode> peerBuffer = new ArraySet<>();
    private Set<PeerNode> waitingForAnswer = new ArraySet<>();
    private int neededResponses = 0;

    /**
     * Default constructor
     * @param target the target Node
     * @param actionPropagator
     * @param nodeProvider
     * @param resultListener the listener to this Request's Result. Must not be null.
     */
    public FindNodePendingRequest(
            int operationId,
            @NonNull PeerNode target,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> nodeProvider,
            @NonNull FindNodeResultListener resultListener
    ){
        this.operationId = operationId;
        this.target = target;
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
        //TODO fix generics
        List<PeerNode> closest = nodeProvider.getKClosest(K,target.getAddress());
        for(PeerNode node : closest){
            waitingForAnswer.add(node);
            //TODO create action
            KadAction findNodeAction = null;
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
        PeerNode sender = NodeUtils.getNodeForPeer(action.getPeer(), N);
        if(waitingForAnswer.contains(sender)){
            waitingForAnswer.remove(sender);
            neededResponses += action.getTotalParts();
        }
        if(action.getPayloadType() == KadAction.PayloadType.PEER_ADDRESS){
            PeerNode responseNode = NodeUtils.getNodeForPeer(new SMSPeer(action.getPayload()), N);
            peerBuffer.add(responseNode);
        }
        neededResponses--;
        stepsTaken++;
        if(waitingForAnswer.isEmpty() && neededResponses == 0){
            if(peerBuffer.isEmpty()){
                finalStep();
            }
            else nextRound();
            //Next round of propagation
            List<PeerNode> listBuffer = Arrays.asList(peerBuffer.toArray(new PeerNode[0]));
            List<PeerNode> newClosest = nodeProvider.filterKClosest(K, target.getAddress(),listBuffer);
            propagateToAll(newClosest);
            backupBuffer = peerBuffer;
            peerBuffer = new ArraySet<>();
        }
    }

    /**
     * The buffer is empty, so no better solutions have been found than the ones in the backup buffer
     */
    private void finalStep(){
        //TODO
    }

    /**
     * Method to perform the next round of Requests
     */
    private void nextRound(){
        //TODO
    }

    /**
     * Method to propagate an Action to all the peerNodes in a list
     * @param peerNodes a list containing PeerNodes
     */
    private void propagateToAll(List<PeerNode> peerNodes){
        //TODO for each create an Action and send it
    }
}
