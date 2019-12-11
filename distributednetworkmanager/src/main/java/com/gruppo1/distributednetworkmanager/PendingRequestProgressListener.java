package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.smshandler.Peer;

/**
 * Interface declaring standard behaviour for a class listening to Results for a PendingRequest.
 * @author Riccardo De Zen
 */
public interface PendingRequestProgressListener<P extends Peer>{
    /**
     * Method to be called by a PendingRequest when a Peer is encountered and might need to get added
     * to the Routing Table
     * @param encounteredPeer the encountered Peer
     */
    void onPeerEncountered(P encounteredPeer);

    //TODO
    void onInviteResult();
    /**
     *
     */
    void onPingResult();

    void onStoreResult();

    void onFindNodeResult();

    void onFindValueResult();
}
