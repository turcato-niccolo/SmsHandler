package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.Message;
import com.dezen.riccardo.smshandler.Peer;

/**
 * Interface defining generic behaviour of a Network: inviting, joining, checking availability of an
 * User (Peer) or a Resource. The Network is assumed to contain any kind of Resource.
 * @author Riccardo De Zen.
 * @param <M> The type of Message this Network uses to communicate
 * @param <P> The type of Peer this Network connects
 */
public interface NetworkInterface<M extends Message, P extends Peer>{

    /**
     * Method to send an invitation to a new User (Peer)
     * @param newPeer the Peer to invite
     */
    void invite(P newPeer);

    /**
     * Method to accept an invitation received
     * @param inviter the user that sent the invitation
     */
    void acceptInvite(P inviter);

    /**
     * Method to check whether a Resource is available.
     * @param key the key of Resource to find.
     * @return true if the Resource is available, false otherwise.
     */
    boolean isResourceAvailable(String key);

    /**
     * Method to check whether a Peer is connected to the Network
     * @param peer the Peer to check
     * @return true if the Peer is considered connected, false otherwise
     */
    boolean isPeerConnected(P peer);
}
