package com.dezen.riccardo.networkmanager;

import android.content.Context;
import android.util.Log;

import com.dezen.riccardo.smshandler.CommunicationHandler;
import com.dezen.riccardo.smshandler.ReceivedMessageListener;
import com.dezen.riccardo.smshandler.SMSHandler;
import com.dezen.riccardo.smshandler.SMSManager;
import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.nio.charset.Charset;

/**
 * @author Niccolò Turcato. Invitation sending and receiving. Switch actions on Message received.
 * @author Riccardo De Zen. User management and synchronization. Enumeration to define actions.
 */
public class NetworkManager extends NetworkInterface<SMSMessage, SMSPeer,StringResource, NetworkDictionary> implements ReceivedMessageListener<SMSMessage> {
    /**
     * Actions the network can send and receive.
     * Current syntax for messages is as follows:
     * <SMMLibrary-TAG>[ACTION]<separation>[PARAMETER]<separation>[EXTRA]
     */

    private enum ACTIONS {
        //<#>INVITE [IGNORED] [IGNORED]
        INVITE(0),
        //<#>ACCEPT [IGNORED] [IGNORED]
        ACCEPT(1),
        //<#>ADD_USER [PEER] [IGNORED]
        ADD_USER(2),
        //<#>REMOVE_USER [PEER] [IGNORED]
        REMOVE_USER(3),
        //<#>GREET_USER [PEER] [IGNORED]
        GREET_USER(4),
        //<#>MSG [MESSAGE]
        MSG(5),
        //<#>ADD_RESOURCE [KEY] [VALUE]
        ADD_RESOURCE(6),
        //<#>REMOVE_RESOURCE [KEY] [IGNORED]
        REMOVE_RESOURCE(7);
        private int val;
        ACTIONS(int num){
            val = num;
        }
        public int value(){return val;}
    }
    private static final String[] actionMessages = {
            "INVITE",
            "ACCEPT",
            "ADD_USER",
            "REMOVE_USER",
            "GREET_USER",
            "MSG",
            "ADD_RESOURCE",
            "REMOVE_RESOURCE"
    };

    private static class Actions
    {
        static String INVITE(){
            return "INVITE";
        }
        static String ACCEPT(){
            return "ACCEPT";
        }
        static String ADD_USER(){
            return "ADD_USER";
        }
        static String REMOVE_USER(){
            return "REMOVE_USER";
        }
        static String GREET_USER(){
            return "GREET_USER";
        }
        static String MSG(){
            return "MSG";
        }
        static String ADD_RESOURCE(){
            return "ADD_RESOURCE";
        }
        static String REMOVE_RESOURCE(){
            return "REMOVE_RESOURCE";
        }
    }

    private static final String separation =  "\r"; //TODO search for a better character
    private static final int commandPosition = 0, peerAddressPosition = 1,
            messageBodyPosition = 1,
            resourceKeyPosition = 1, resourceValuePosition = 2;

    //TODO add support for multiple networks.

    private boolean isPartOfNetwork;
    private SMSPeer myPeer;
    private OnNetworkEventListener<SMSMessage, StringResource> listener;
    private NetworkDictionary dictionary;
    private CommunicationHandler<SMSMessage> handler;
    private Context context;
    private final String MANAGER_TAG = "MANAGER_TAG";

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
     * Method to send an invitation to a new User (Peer)
     * @param newPeer the Peer to invite
     */
    @Override
    public void invite(SMSPeer newPeer) {
        if(!isConnectedToPeer(newPeer))
        handler.sendMessage(new SMSMessage(newPeer, Actions.INVITE()));
    }

    /**
     * Method to accept an invitation received
     * @param inviter the user that sent the invitation
     */
    @Override
    public void acceptInvite(SMSPeer inviter) {
        handler.sendMessage(new SMSMessage(inviter, Actions.ACCEPT()));
        dictionary.addPeer(inviter);
        isPartOfNetwork = true;
    }

    /**
     * @author Turcato
     * Since the information we have about the network are just stored in the dictionary,
     * we search the peer's presence in it
     *
     * @param peer the peer whose presence in the network has to be evaluated
     * @return true if the given peer is part of the network, false if the peer is null or isn't part of the network
     */
    public boolean isConnectedToPeer(SMSPeer peer) {
        if(peer != null) {
            return dictionary.contains(peer);
        }
        else return false;
    }

    /**
     * Method for when a user has accepted the invitation
     * @param invited invited peer
     */
    private void onInviteAccepted(SMSPeer invited) {
        Log.d(MANAGER_TAG, invited.getAddress() +" has accepted");
        dictionary.addPeer(invited);
        broadcast(ACTIONS.GREET_USER,invited.getAddress(),"");
        //This Peer wasn't part of a network but now it is since someone accepted its invitation.
        if(!isPartOfNetwork) isPartOfNetwork = true;
    }

    /**
     * Method to create a new valid Resource, and notify the network
     * @param newResource the new Resource
     */
    public void createResource(StringResource newResource) {
        dictionary.addResource(newResource);
        broadcast(ACTIONS.ADD_RESOURCE,newResource.getName(),newResource.getValue());
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

    /**
     * Method to send some kind of broadcast to the whole network
     * @param action the action to broadcast
     */
    public void broadcast(ACTIONS action, String param, String extra) {
        for(SMSPeer peer : getAvailablePeers()){
            if(!peer.equals(myPeer))
                handler.sendMessage(
                    new SMSMessage(peer, Actions.MSG()
                            + separation + param
                            + separation + extra)
                );
        }
    }

    /**
     * Method to test a generic broadcast.
     */
    public void smile(){
        broadcast(ACTIONS.MSG,"SMILE","");
    }

    @Override
    public void onMessageReceived(SMSMessage message) {
        //SWITCH???
        String receivedMessageString = message.getData();

        //Removing Library key String
        receivedMessageString = receivedMessageString.replace(SMSHandler.APP_KEY, "");


        if(receivedMessageString.contains(Actions.INVITE())){
            //Message contains invitation
            SMSPeer inviter = message.getPeer();
            acceptInvite(inviter);
        }

        if(receivedMessageString.contains(Actions.ACCEPT())){
            //Message contains invitation acceptance
            SMSPeer acceptingPeer = message.getPeer();
            onInviteAccepted(acceptingPeer);
        }

        if(receivedMessageString.contains(Actions.ADD_USER())){
            //Add the received parameter user
            dictionary.addPeer(new SMSPeer(receivedMessageString.split(separation)[peerAddressPosition]));
        }

        if(receivedMessageString.contains(Actions.GREET_USER())){
            //Notifies the received parameter user that it should add this user
            SMSPeer newPeer = new SMSPeer(receivedMessageString.split(separation)[peerAddressPosition]);
            dictionary.addPeer(newPeer);
            handler.sendMessage(new SMSMessage(newPeer, Actions.ADD_USER() + separation + myPeer));
        }

        if(receivedMessageString.contains(Actions.REMOVE_USER())){
            //Peer removal requested
            String peerAddress = receivedMessageString.split(separation)[peerAddressPosition];
            dictionary.removePeer(new SMSPeer(peerAddress));
        }

        if(receivedMessageString.contains(Actions.MSG())){
            if(listener != null) listener.onMessageReceived(message);
        }

        if(receivedMessageString.contains(Actions.ADD_RESOURCE())){
            String[] words = receivedMessageString.split(separation);
            String resourceKey = words[resourceKeyPosition];
            StringBuilder resourceValue = new StringBuilder();
            for(int i = resourceValuePosition; i < words.length; i++) resourceValue.append(words[i]);
            dictionary.addResource(new StringResource(resourceKey,resourceValue.toString()));
        }

        if(receivedMessageString.contains(Actions.REMOVE_RESOURCE())){
            String resourceKey = receivedMessageString.split(separation)[peerAddressPosition];
            dictionary.removeResource(new StringResource(resourceKey,""));
        }

    }
}
