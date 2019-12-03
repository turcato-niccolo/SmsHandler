package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.Message;
import com.dezen.riccardo.smshandler.Peer;

/**
 * Possible interface for Network Dictionary sharing. By default this interface defines only read
 * behaviour.
 * @author Riccardo De Zen.
 * @param <P>
 * @param <R>
 * @param <D>
 */
public interface NetworkInterface<M extends Message, P extends Peer, R extends Resource, D extends Dictionary<P,R>>{

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
     * Method to get a Resource from the network, if available.
     * @param key the key of Resource to request.
     * @return The Resource with the matching key if available.
     */
    R getResource(String key);

    /**
     * Method to get an array of the Peers.
     * @return array containing all Available Peers.
     */
    P[] getAvailablePeers();

    /**
     * Method to get an array of the Resources.
     * @return array containing all Available Resources.
     */
    R[] getAvailableResources();

    /**
     * Setter for a listener that should listen to various network events.
     * @param listener the class listening for events.
     */
    void setListener(OnNetworkEventListener<M, R> listener);
}
