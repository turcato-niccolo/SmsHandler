package com.gruppo1.distributednetworkmanager.listeners;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.smshandler.SMSPeer;

public interface FindValueResultListener {
    /**
     * Method called when a Resource has been found, and who had it
     * @param operationId the id for the PendingRequest that came to an end
     * @param owner the Peer that had this Resource
     * @param resource the Resource found, complete of key and value
     */
    void onFindValueResult(int operationId, SMSPeer owner, Resource resource);
}
