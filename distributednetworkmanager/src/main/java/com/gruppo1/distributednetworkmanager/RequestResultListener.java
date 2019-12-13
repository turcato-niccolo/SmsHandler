package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.smshandler.Peer;

/**
 * @author Riccardo De Zen
 * Interface defining standard behaviour for a class listening to request results.
 * Currently the Listener must provide support for all 5 main Request types: INVITE, PING, FIND_NODE, FIND_VALUE, STORE
 */
public interface RequestResultListener<P extends Peer>{
    /**
     * Method called when an Invite has been accepted or refused
     * @param invited the Peer that answered to the invite
     * @param accepted true if the Peer accepted the invite and false if the invite was refused or
     *                 the timeout expired
     */
    //TODO turn the boolean into an Enum
    void onInviteResult(P invited, boolean accepted);

    /**
     * Method called when a Ping came back or the timeout expired
     * @param pinged the Peer that got Pinged
     * @param isOnline true if the Peer answered the ping, false otherwise
     */
    void onPingResult(P pinged, boolean isOnline);

    /**
     * Method called when a Store operation has been completed
     * @param storedResource the Resource that got successfully stored
     * @param newOwner the Peer that stored the Resource
     */
    void onStoreResult(Resource storedResource, P newOwner);

    //TODO should findNodeResult be added? Yes? No? Why?

    /**
     * Method called when a Resource has been found, and who had it
     * @param owner the Peer that had this Resource
     * @param resource the Resource found, complete of key and value
     */
    void onFindValueResult(P owner, Resource resource);
}
