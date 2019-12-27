package com.gruppo1.distributednetworkmanager.listeners;

import androidx.annotation.NonNull;

import java.util.BitSet;

/**
 * Interface defining the default behaviour for a Class wanting to work as a listener for events
 * related to one or more
 * {@link com.gruppo1.distributednetworkmanager.pendingrequests.FindNodePendingRequest}.
 *
 * @author Riccardo De Zen
 */
public interface FindNodeResultListener {
    /**
     * Method called when a Find Node operation has been completed.
     *
     * @param operationId the id for the {@code PendingRequest} that reached a conclusion.
     * @param target      the Target Node
     * @param closest     the closest Peer
     */
    void onFindNodeResult(int operationId, @NonNull BitSet target, @NonNull PeerNode closest);
}
