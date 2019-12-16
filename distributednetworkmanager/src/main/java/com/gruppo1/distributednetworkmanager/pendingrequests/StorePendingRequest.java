package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.dezen.riccardo.networkmanager.StringResource;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.BinarySet;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.PeerNode;
import com.gruppo1.distributednetworkmanager.listeners.StoreResultListener;

public class StorePendingRequest implements PendingRequest {

    public StorePendingRequest(
            int operationId,
            @NonNull StringResource targetId,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> nodeProvider,
            @NonNull StoreResultListener resultListener
    ){

    }

    /**
     * @return the number of steps performed (number of times nextStep took a valid Action and acted
     * accordingly).
     */
    @Override
    public int getStepsTaken() {
        return 0;
    }

    /**
     * @return the unique Code for this PendingRequest
     */
    @Override
    public int getOperationId() {
        return 0;
    }

    /**
     * Method used to start the PendingRequest, propagating its first Action
     */
    @Override
    public void start() {

    }

    /**
     * @param action an Action
     * @return true if the given action can be used by the Request (i.e. same type and code) false
     * otherwise.
     */
    @Override
    public boolean isActionPertinent(KadAction action) {
        return false;
    }

    /**
     * Method to perform the next step for this PendingRequest. The User should always check with
     * isPertinent(action) beforehand to know whether the Action can be used.
     *
     * @param action the Action triggering the step
     */
    @Override
    public void nextStep(KadAction action) {

    }
}
