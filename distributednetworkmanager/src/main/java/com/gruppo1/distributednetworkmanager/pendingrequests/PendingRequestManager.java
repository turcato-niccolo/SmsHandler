package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.smshandler.Peer;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.DistributedNetworkAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.RequestResultListener;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class meant to handle running operations in the Network.
 * Does not answer to incoming requests, only handles response to sent requests.
 * @author Riccardo De Zen
 */
public class PendingRequestManager<P extends Peer>{
    //TODO generification
    public static final int MAX_REQUESTS = 10;

    private List<PendingRequest<P>> currentRequests = new ArrayList<>();
    private PendingRequestFactory<P> factory = new PendingRequestFactory<>();
    private ActionPropagator actionPropagator;
    private NodeDataProvider nodeProvider;
    private RequestResultListener resultListener;

    public PendingRequestManager(ActionPropagator propagator, NodeDataProvider provider, RequestResultListener listener){
        actionPropagator = propagator;
        nodeProvider = provider;
        resultListener = listener;
    }

    /**
     * Method to enqueue a PendingRequest from an Action
     * @param action the Action starting this PendingRequest
     * @return true if the PendingRequest was enqueued, false if the maximum amount of Requests has
     * been reached.
     */
    private boolean enqueueRequest(DistributedNetworkAction action) throws InvalidActionException {
        if(currentRequests.size() >= 10) return false;
        PendingRequestFactory<P> factory = new PendingRequestFactory<>();
        PendingRequest<P> request = factory.getPendingRequest(action, resultListener);
        currentRequests.add(request);
        actionPropagator.propagateAction(action);
        return true;
    }

    /**
     * Method to continue the elaboration of a Pending Request. Only Response type Actions will be
     * attempted
     * @param action The action continuing the corresponding Request
     * @return true if a matching Request was found and executed, false otherwise.
     */
    public boolean continueRequest(DistributedNetworkAction action){
        int code = action.getAction();
        //TODO if it's not a Response then wtf return false
        for(PendingRequest<P> request : currentRequests){
            if(request.isPertinent(action)){
                propagate(request.nextStep(action));
                return true;
            }
        }
        return false;
    }

    /**
     * Method to propagate DistributedNetworkActions
     * @param actions the Actions needing to be propagated
     */
    private void propagate(List<DistributedNetworkAction> actions){
        for(DistributedNetworkAction action : actions){
            actionPropagator.propagateAction(action);
        }
    }

    /**
     * Method to find the lowest available code to give to a new Request
     * @return the lowest available code for a Request, -1 if no code is available (the queue is full)
     */
    private int generateRequestCode(){
        for(int i = 0; i < MAX_REQUESTS; i++){
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
