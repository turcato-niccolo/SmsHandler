package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.CommunicationHandler;
import com.dezen.riccardo.smshandler.Peer;

public abstract class NetworkInterface<P extends Peer, R extends Resource, V extends Vocabulary<P,R>>{
    //Vocabulary to assign at runtime
    protected V vocabulary;
    //CommunicationHandler to be used
    protected CommunicationHandler communicationHandler;

    /**
     * Method to send an invitation to a new User (Peer)
     * @param newPeer the Peer to invite
     */
    public abstract void invite(P newPeer);

    /**
     * Method to accept an invitation received
     * @param inviter the user that sent the invitation
     */
    public abstract void acceptInvite(P inviter);

    /**
     * Method to create a new valid Resource, become it's owner and send it through the network
     * @param newResource the new Resource
     */
    public abstract void createResource(R newResource);

    /**
     * Method to request a Resource from the network.
     * @param resource the Resource to request.
     */
    public abstract void requestResource(R resource);

    /**
     * Method to get an array of the Peers.
     * @return array containing all Available Peers.
     */
    public abstract P[] getAvailablePeers();

    /**
     * Method to get an array of the Resources.
     * @return array containing all Available Resources.
     */
    public abstract R[] getAvailableResources();

    /**
     * Setter for a listener that should listen for Resources being obtained.
     * @param listener the class listening for Resource events.
     */
    public abstract void setListener(OnResourceObtainedListener<R> listener);
}
