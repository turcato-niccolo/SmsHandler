package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.smshandler.Peer;

/**
 * Interface declaring standard behaviour for a class listening to Results for a PendingRequest.
 * @author Riccardo De Zen
 */
public interface PendingRequestProgressListener<P extends Peer>{
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
