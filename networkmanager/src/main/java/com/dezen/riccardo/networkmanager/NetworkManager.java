package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.SMSPeer;

public class NetworkManager extends NetworkInterface<SMSPeer,StringResource,NetworkVocabulary> {
    /**
     * Method to send an invitation to a new User (Peer)
     * @param newPeer the Peer to invite
     */
    @Override
    public void invite(SMSPeer newPeer) {

    }

    /**
     * Method to accept an invitation received
     * @param inviter the user that sent the invitation
     */
    @Override
    public void acceptInvite(SMSPeer inviter) {

    }

    /**
     * Method to create a new valid Resource, become it's owner and send it through the network
     * @param newResource the new Resource
     */
    @Override
    public void createResource(StringResource newResource) {

    }

    /**
     * Method to request a Resource from the network.
     * @param resource the Resource to request.
     */
    @Override
    public void requestResource(StringResource resource) {

    }

    /**
     * Method to get an array of the Peers.
     * @return array containing all Available Peers.
     */
    @Override
    public SMSPeer[] getAvailablePeers() {
        return new SMSPeer[0];
    }

    /**
     * Method to get an array of the Resources.
     * @return array containing all Available Resources.
     */
    @Override
    public StringResource[] getAvailableResources() {
        return new StringResource[0];
    }

    /**
     * Setter for a listener that should listen for Resources being obtained.
     * @param listener the class listening for Resource events.
     */
    @Override
    public void setListener(OnResourceObtainedListener<StringResource> listener) {

    }
}
