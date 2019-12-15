package com.gruppo1.distributednetworkmanager.listeners;

import com.dezen.riccardo.smshandler.SMSPeer;

public interface PingResultListener {
    /**
     * Method called when a Ping came back or the timeout expired
     * @param operationId the id for the PendingRequest that came to an end
     * @param pinged the SMSPeer that got Pinged
     * @param isOnline true if the Peer answered the ping, false otherwise
     */
    void onPingResult(int operationId, SMSPeer pinged, boolean isOnline);
}
