package com.gruppo1.distributednetworkmanager.listeners;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.smshandler.SMSPeer;

public interface StoreResultListener {
    /**
     * Method called when a Store operation has been completed
     * @param operationId the id for the PendingRequest that came to an end
     * @param storedResource the Resource that got successfully stored
     * @param newOwner the Peer that stored the Resource
     */
    void onStoreResult(int operationId, Resource storedResource, SMSPeer newOwner);
}
