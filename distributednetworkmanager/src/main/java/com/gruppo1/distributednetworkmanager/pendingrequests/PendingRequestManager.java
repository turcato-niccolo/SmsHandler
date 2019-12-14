package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.smshandler.Peer;
import com.gruppo1.distributednetworkmanager.Node;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class meant to handle running operations in the Network.
 * Does not answer to incoming requests, only handles response to sent requests.
 * @author Riccardo De Zen
 */
public class PendingRequestManager<P extends Peer>{
    private List<PendingRequest<P>> currentRequests = new ArrayList<>();
    private PendingRequestFactory<P> factory = new PendingRequestFactory<>();
    private RequestPropagator requestPropagator;
    private NodeDataProvider nodeProvider;
    private ActionResultListener resultListener;

    public PendingRequestManager(RequestPropagator propagator, NodeDataProvider provider, ActionResultListener listener){
        requestPropagator = propagator;
        nodeProvider = provider;
        resultListener = listener;
    }

    /**
     * Method to enqueue an Invitation Request
     * @param invitingPeer the inviting Peer
     * @param invitedPeer the invited Peer
     * @return true if the Request was enqueued, false otherwise
     */
    public boolean enqueueInvite(P invitingPeer, P invitedPeer){
        //TODO generate action
        return enqueueRequest(inviteAction);
    }

    /**
     * Method to enqueue a Ping Request
     * @param pingingPeer
     * @param pingedPeer
     * @return true if the Request was enqueued, false otherwise
     */
    public boolean enqueuePing(P pingingPeer, P pingedPeer){
        //TODO generate action
        return enqueueRequest(pingAction);
    }

    /**
     * Method to enqueue a Find Node Request
     * @param askingPeer
     * @param target
     * @return true if the Request was enqueued, false otherwise
     */
    public boolean enqueueFindNode(P askingPeer, Node target){
        //TODO generate action
        return enqueueRequest(findNodeAction);
    }

    /**
     * Method to enqueue a Find Value Request
     * @param askingPeer
     * @param key
     * @return true if the Request was enqueued, false otherwise
     */
    public boolean enqueueFindValue(P askingPeer, String key){
        //TODO generate action
        return enqueueRequest(findValueAction);
    }

    /**
     * Method to enqueue a Store Request
     * @param askingPeer
     * @param resourceToStore
     * @return true if the Request was enqueued, false otherwise
     */
    public boolean enqueueStore(P askingPeer, Resource resourceToStore){
        //TODO generate action
        return enqueueRequest(storeAction);
    }

    /**
     * Method to enqueue a PendingRequest from an Action
     * @param action the Action starting this PendingRequest
     * @return true if the PendingRequest was enqueued, false if the maximum amount of Requests has
     * been reached.
     */
    private boolean enqueueRequest(KadAction<P> action) throws InvalidActionException {
        if(currentRequests.size() >= KadAction.MAX_CODE) return false;
        PendingRequestFactory<P> factory = new PendingRequestFactory<>();
        PendingRequest<P> request = factory.getPendingRequest(action.getType(), generateRequestCode());
        currentRequests.add(request);
        request.start(action);
        return true;
    }

    /**
     * Method to continue the elaboration of a Pending Request. Only Response type Actions will be
     * attempted
     * @param action The action continuing the corresponding Request
     * @return true if a matching Request was found and executed, false otherwise
     */
    public boolean continueRequest(KadAction<P> action){
        int code = action.getCode();
        if(!KadAction.isResponse(code)) return false;
        List<KadAction<P>> followUp = new ArrayList<>();
        for(PendingRequest<P> request : currentRequests){
            if(request.getCode() == code){
                performFollowing(request.nextStep(action));
                return true;
            }
        }
        return false;
    }

    /**
     * Method to find the lowest available code to give to a new Request
     * @return the lowest available code for a Request, -1 if no code is available (the queue is full)
     */
    private int generateRequestCode(){
        int max = KadAction.MAX_CODE;
        for(int i = 0; i < max; i++){
            if(isCodeAvailable(i)) return i;
        }
        return -1;
    }

    /**
     * Method to find whether a code is available or not
     * @param code the code whose availability is to be checked
     * @return true if the code is available, false otherwise
     */
    private boolean isCodeAvailable(int code){
        for(PendingRequest<P> request : currentRequests){
            if(request.getCode() == code){
                return false;
            }
        }
        return true;
    }
}
