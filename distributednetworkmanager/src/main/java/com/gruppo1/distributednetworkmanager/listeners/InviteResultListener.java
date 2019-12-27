package com.gruppo1.distributednetworkmanager.listeners;

import androidx.annotation.NonNull;

/**
 * Interface defining the default behaviour for a Class wanting to work as a listener for events
 * related to one or more
 * {@link com.gruppo1.distributednetworkmanager.pendingrequests.InvitePendingRequest}.
 *
 * @author Riccardo De Zen
 */
public interface InviteResultListener {
    /**
     * Method called when an Invite operation has come to an end.
     *
     * @param operationId the id for the {@code PendingRequest} that reached a conclusion.
     * @param invited     the {@code Peer} that answered to the invite.
     * @param accepted    this parameter is true if {@code invited} accepted the invite and false
     *                    if the invite was refused or the operation otherwise failed.
     */
    void onInviteResult(int operationId, @NonNull PeerNode invited, boolean accepted);
}
