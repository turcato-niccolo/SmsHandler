package com.gruppo1.distributednetworkmanager.listeners;

import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.BitSet;

public interface FindNodeResultListener {
    /**
     * Method called when a Find Node operation has been completed
     * @param operationId the id for the PendingRequest that came to an end
     * @param target the Target Node
     * @param closest the closest Peer
     */
    void onFindNodeResult(int operationId, BitSet target, SMSPeer closest);
}
