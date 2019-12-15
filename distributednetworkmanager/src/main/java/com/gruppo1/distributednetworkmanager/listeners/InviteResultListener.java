package com.gruppo1.distributednetworkmanager.listeners;

import com.dezen.riccardo.smshandler.SMSPeer;

public interface InviteResultListener {
    /**
     * Method called when an Invite has been accepted or refused
     * @param operationId the id for the PendingRequest that came to an end
     * @param invited the Peer that answered to the invite
     * @param accepted true if the Peer accepted the invite and false if the invite was refused or
     *                 the timeout expired
     */
    void onInviteResult(int operationId, SMSPeer invited, boolean accepted);
}
