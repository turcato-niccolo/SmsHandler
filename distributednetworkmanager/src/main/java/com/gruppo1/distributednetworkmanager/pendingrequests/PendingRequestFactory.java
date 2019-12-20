package com.gruppo1.distributednetworkmanager.pendingrequests;

import androidx.annotation.NonNull;

import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.listeners.FindNodeResultListener;
import com.gruppo1.distributednetworkmanager.listeners.FindValueResultListener;
import com.gruppo1.distributednetworkmanager.listeners.InviteResultListener;
import com.gruppo1.distributednetworkmanager.listeners.PingResultListener;
import com.gruppo1.distributednetworkmanager.listeners.StoreResultListener;

/**
 * Class defining the various PendingRequest behaviours
 */
public class PendingRequestFactory{
    private static final String INVALID_ACTION_ERR = "The provided Action is invalid, only Request " +
            "type Actions can be used.";

    public static PendingRequest getPingPendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull PingResultListener resultListener
    ){
        return null;
    }

    public static PendingRequest getInvitePendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull InviteResultListener resultListener
    ){
        return null;
    }

    public static PendingRequest getFindNodePendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull FindNodeResultListener resultListener
    ){
        return null;
    }

    public static PendingRequest getFindValuePendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull FindValueResultListener resultListener
    ){
        return null;
    }

    public static PendingRequest getStorePendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull StoreResultListener resultListener
    ){
        return null;
    }
}
