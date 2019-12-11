package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.smshandler.Peer;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class meant to handle running operations in the Network.
 * Does not answer to incoming requests, only handles response to sent requests.
 * @author Riccardo De Zen
 */
public class PendingRequestManager<P extends Peer>{
    private int requestCode = 0;
    private int activeRequests = 0;
    private List<PendingRequest<P>> currentRequests = new ArrayList<>();

    public PendingRequestManager(){

    }

    public boolean enqueueInvite(){
        //TODO parameters
        //TODO generate actions
        return false;
    }

    public boolean enqueuePing(){
        return false;
    }

    public boolean enqueueFindNode(){
        return false;
    }

    public boolean enqueueFindValue(){
        return false;
    }

    public boolean enqueueStore(){
        return false;
    }

    /**
     * Method to enqueue a PendingRequest from an Action
     * @param action the Action starting this PendingRequest
     * @return true if the PendingRequest was enqueued, false if the maximum amount of Requests has
     * been reached.
     */
    private boolean enqueueRequest(KadAction<P> action) throws InvalidRequestException{
        if(currentRequests.size() >= KadAction.MAX_CODE) return false;
        PendingRequestFactory<P> factory = new PendingRequestFactory<>();
        PendingRequest<P> request = factory.getPendingRequest(action, requestCode);
        currentRequests.add(request);
        return true;
    }

    /**
     * Method to continue the elaboration of a Pending Request
     * @param action The action continuing the corresponding Request
     * @return true if a matching Request was found and executed, false otherwise
     */
    public boolean continueRequest(KadAction<P> action){
        int code = action.getCode();
        for(PendingRequest<P> request : currentRequests){
            if(request.getCode() == code){
                request.nextStep(action);
            }
        }
    }
}
