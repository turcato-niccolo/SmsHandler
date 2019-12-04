package com.dezen.riccardo.networkmanager;

import android.content.Context;

import com.dezen.riccardo.networkmanager.exceptions.InvalidMsgSyntaxException;
import com.dezen.riccardo.smshandler.CommunicationHandler;
import com.dezen.riccardo.smshandler.ReceivedMessageListener;
import com.dezen.riccardo.smshandler.SMSHandler;
import com.dezen.riccardo.smshandler.SMSManager;
import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

/**
 * @author Niccolò Turcato.
 * @author Riccardo De Zen.
 */
public class NetworkManager implements NetworkInterface<SMSMessage, SMSPeer,StringResource, NetworkDictionary>, ReceivedMessageListener<SMSMessage> {
    /**
     * Actions the network can send and receive.
     * Current syntax for messages is as follows:
     * <SMMLibrary-TAG>[ACTION]<SEPARATOR>[ARGUMENT]<SEPARATOR>[EXTRA]
     */


    //TODO add support for multiple networks.

    private boolean isPartOfNetwork;
    private SMSPeer myPeer;
    private OnNetworkEventListener<SMSMessage, StringResource> listener;
    private NetworkDictionary dictionary;
    private CommunicationHandler<SMSMessage> handler;
    private Context context;

    public NetworkManager(Context registerContext) {
         dictionary = new NetworkDictionary(registerContext);
         handler = SMSManager.getInstance(registerContext);
         handler.setReceiveListener(this);
         context = registerContext;
         isPartOfNetwork = false;
    }

    /**
     * This version of the constructor should be used to insert the peer that builds the object
     * (if it is a peer for the sms Network)
     * @param firstPeer the peer tha builds the network
     */
    public NetworkManager(Context registerContext, SMSPeer firstPeer) {
        this(registerContext);
        dictionary.addPeer(firstPeer);
        myPeer = firstPeer;
    }

    /**
     * Send a specific action to a single Peer
     * @param action action to perform
     */
    private void perform(ActionStructure action){
        if(action.getPeer().equals(myPeer)) return;
        SMSMessage message = action.generateMessage();
        handler.sendMessage(message);
    }

    /**
     * Method to send some kind of broadcast to the whole network
     * @param action the action to broadcast
     */
    private void broadcast(ActionStructure<String> action){
        for(SMSPeer peer : getAvailablePeers()) {
            if (!peer.equals(myPeer)) {
                action.setDestinationPeer(peer);
                perform(action);
            }
        }
    }

    /**
     * Method to send an invitation to a new User (Peer)
     * @param newPeer the Peer to invite
     */
    @Override
    public void invite(SMSPeer newPeer) {
        if(!isConnectedToPeer(newPeer))
        {
            ActionStructure<String> inviteAction = new NetworkAction(NetworkAction.Type.INVITE, NetworkAction.DEFAULT_IGNORED, NetworkAction.DEFAULT_IGNORED);
            inviteAction.setDestinationPeer(newPeer);
            perform(inviteAction);
        }
    }

    /**
     * Method to accept an invitation received
     * @param inviter the user that sent the invitation
     */
    @Override
    public void acceptInvite(SMSPeer inviter) {
        if(!isPartOfNetwork){
            ActionStructure<String> acceptInviteAction = new NetworkAction(NetworkAction.Type.ANSWER_INVITE, NetworkAction.DEFAULT_IGNORED, NetworkAction.DEFAULT_IGNORED);
            acceptInviteAction.setDestinationPeer(inviter);
            perform(acceptInviteAction);
            dictionary.addPeer(inviter);
            isPartOfNetwork = true;
        }
    }

    /**
     * Since the information we have about the network are just stored in the dictionary,
     * we search the peer's presence in it
     *
     * @param peer the peer whose presence in the network has to be evaluated (must be valid, see SMSPeer.isValid())
     * @return true if the given peer is part of the network, false if the peer is null or isn't part of the network
     */
    public boolean isConnectedToPeer(SMSPeer peer) {
        if(peer != null && peer.isValid()) {
            return dictionary.contains(peer);
        }
        else return false;
    }

    /**
     * Method for when a user has accepted the invitation
     * @param invited invited peer
     */
    private void onInviteAccepted(SMSPeer invited) {
        dictionary.addPeer(invited);
        ActionStructure<String> greetAction = new NetworkAction(NetworkAction.Type.GREET_USER, invited.getAddress(), NetworkAction.DEFAULT_IGNORED);
        broadcast(greetAction);
        //This Peer wasn't part of a network but now it is since someone accepted its invitation.
        if(!isPartOfNetwork) isPartOfNetwork = true;
        //Resources have to be sent
        sendResources(invited);
    }

    /**
     * Method to send resources to a Peer. A Peer who invites another is responsible for sending it
     * all the resources.
     * @param targetPeer the Peer which will receive the resources.
     */
    private void sendResources(SMSPeer targetPeer){
        StringResource[] existingResources = dictionary.getResources();
        for(StringResource resource : existingResources){
            send(Actions.ADD_RESOURCE, resource.getName(), resource.getValue(), targetPeer);
        }
    }

    /**
     * Method to create a new valid Resource, and notify the network
     * @param newResource the new Resource
     */
    public void createResource(StringResource newResource) {
        dictionary.addResource(newResource);
        ActionStructure<String> addResource = new NetworkAction(NetworkAction.Type.ADD_RESOURCE,
                newResource.getName(),
                newResource.getValue());
        broadcast(addResource);
    }

    /**
     * Method to request a Resource from the network.
     * @param key the Resource to request.
     */
    @Override
    public StringResource getResource(String key) {
        for(StringResource res : dictionary.getResources()){
            if(res.getName().equals(key)) return res;
        }
        return StringResource.getDefaultInvalid();
    }

    /**
     * Method to get an array of the Peers.
     * @return array containing all Available Peers.
     */
    @Override
    public SMSPeer[] getAvailablePeers() {
        return dictionary.getPeers();
    }

    /**
     * Method to get an array of the Resources.
     * @return array containing all Available Resources.
     */
    @Override
    public StringResource[] getAvailableResources() {
        return dictionary.getResources();
    }

    /**
     * Setter for a listener that should listen for Resources being obtained.
     * @param listener the class listening for Resource events.
     */
    @Override
    public void setListener(OnNetworkEventListener<SMSMessage, StringResource> listener) {
        this.listener = listener;
    }

    @Override
    public void onMessageReceived(SMSMessage message) {
        String usefulMessage = message.getData().replace(SMSHandler.APP_KEY, "");
        SMSMessage receivedMessage = new SMSMessage(message.getPeer(), usefulMessage);

        ActionStructure<String> receivedAction = new NetworkAction(receivedMessage);

        performAction(receivedAction);
    }

    /**
     * Method to perform an action received through the Network
     * @param action the action to broadcast
     */
    private void performAction(ActionStructure<String> action){
        switch(action.getAction()){
            case NetworkAction.Type.INVITE:
                acceptInvite((SMSPeer) action.getPeer());
                break;

            case NetworkAction.Type.ANSWER_INVITE:
                onInviteAccepted((SMSPeer) action.getPeer());
                break;

            case NetworkAction.Type.ADD_USER:
                dictionary.addPeer(new SMSPeer(action.getParam()));
                break;

            case NetworkAction.Type.GREET_USER:
                SMSPeer newPeer = new SMSPeer(action.getParam());
                dictionary.addPeer(newPeer);
                action = new NetworkAction(NetworkAction.Type.ADD_USER, myPeer.getAddress(), NetworkAction.DEFAULT_IGNORED);
                action.setDestinationPeer(newPeer);
                perform(action);
                break;

            case NetworkAction.Type.REMOVE_USER:
                dictionary.removePeer(new SMSPeer(action.getParam()));
                break;

            case NetworkAction.Type.MSG:
                if(listener != null) listener.onMessageReceived(new SMSMessage((SMSPeer) action.getPeer(), action.getParam()));
                break;

            case NetworkAction.Type.ADD_RESOURCE:
                dictionary.addResource(new StringResource(action.getParam(), action.getExtra()));
                break;

            case NetworkAction.Type.REMOVE_RESOURCE:
                dictionary.removeResource(new StringResource(action.getParam(),""));
                break;
        }
    }
}
