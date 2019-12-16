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
import com.gruppo1.distributednetworkmanager.exceptions.EmptyNodeBufferException;
import com.gruppo1.distributednetworkmanager.listeners.FindNodeResultListener;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FindNodePendingRequest implements PendingRequest {

    private static final int DEF_PARTS = 1;

    private static final int K = 5;
    private static final int N = 128;

    private int stepsTaken = 0;
    private int operationId;
    private PeerNode targetNode;
    private ActionPropagator actionPropagator;
    private NodeDataProvider<BinarySet, PeerNode> nodeProvider;
    private FindNodeResultListener resultListener;

    private Set<PeerNode> backupBuffer = new ArraySet<>();
    private Set<PeerNode> peerBuffer = new ArraySet<>();
    private Set<PeerNode> waitingForAnswer = new ArraySet<>();
    private int neededResponses = 0;

    /**
     * Default constructor
     * @param targetNode the target Node
     * @param actionPropagator
     * @param nodeProvider
     * @param resultListener the listener to this Request's Result. Must not be null.
     */
    public FindNodePendingRequest(
            int operationId,
            @NonNull PeerNode targetNode,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> nodeProvider,
            @NonNull FindNodeResultListener resultListener
    ){
        this.operationId = operationId;
        this.targetNode = targetNode;
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
        List<PeerNode> closest = nodeProvider.getKClosest(K,targetNode.getAddress());
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
            else nextRoundOfRequests();
        }
    }

    /**
     * The buffer is empty, so no better solutions have been found than the ones in the backup buffer
     */
    private void finalStep(){
        List<PeerNode> listBuffer = Arrays.asList(backupBuffer.toArray(new PeerNode[0]));
        List<PeerNode> closestNode = nodeProvider.filterKClosest(1,targetNode.getAddress(),listBuffer);
        if(closestNode.size() == 1){
            SMSPeer closestPeer = closestNode.get(0).getPhysicalPeer();
            resultListener.onFindNodeResult(operationId,targetNode.getAddress().getKey(),closestPeer);
        }
        else throw new EmptyNodeBufferException();
    }

    /**
     * Method to perform the next round of Requests
     */
    private void nextRoundOfRequests(){
        List<PeerNode> listBuffer = Arrays.asList(peerBuffer.toArray(new PeerNode[0]));
        List<PeerNode> newClosest = nodeProvider.filterKClosest(K, targetNode.getAddress(),listBuffer);
        propagateToAll(newClosest);
        backupBuffer = peerBuffer;
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
        return new KadAction(
                peer,
                KadAction.ActionType.FIND_NODE,
                operationId,
                DEF_PARTS,
                DEF_PARTS,
                KadAction.PayloadType.NODE_ID,
                BitSetUtils.BitSetsToHex(targetNode.getAddress().getKey())
        );
    }
}
