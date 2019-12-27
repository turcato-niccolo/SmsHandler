package com.gruppo1.distributednetworkmanager.listeners;

import androidx.annotation.NonNull;

/**
 * Interface defining the default behaviour for a Class wanting to work as a listener for events
 * related to one or more
 * {@link com.gruppo1.distributednetworkmanager.pendingrequests.PingPendingRequest}.
 *
 * @author Riccardo De Zen
 */
public interface PingResultListener {
    /**
     * Method called when a Ping operation came to an end.
     *
     * @param operationId the id for the {@code PendingRequest} that reached a conclusion.
     * @param pinged      the {@code Peer} that got pinged.
     * @param isOnline    this parameter is true if {@code pinged} answered the Ping, false
     *                    otherwise.
     */
    void onPingResult(int operationId, @NonNull PeerNode pinged, boolean isOnline);
}
