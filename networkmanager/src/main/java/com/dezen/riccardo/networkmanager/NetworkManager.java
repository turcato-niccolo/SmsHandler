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
    private static class Actions {
        /**
         * @param action the int value to be checked
         * @return true if the int matches an action, false if not
         */
        static boolean isValid(int action){
            return action >= MIN_ACTION && action <= MAX_ACTION;
        }

        /**
         * @param action the int value to be checked. Actions returning false for isValid also return
         *               false here
         * @return true if the given action uses the "ARGUMENT" part of the message
         */
        static boolean usesArg(int action){
            return isValid(action) && action >= ADD_USER;
        }

        /**
         * @param action the int value to be checked. Actions returning false for isValid also return
         *               false here
         * @return true if the given action uses the "EXTRA" part of message
         */
        static boolean usesExtra(int action){
            return action == ADD_RESOURCE;
        }

        static final int MIN_ACTION = 0;
        static final int MAX_ACTION = 7;

        //[ACTION] [ARGUMENT] [EXTRA]

        //<#>INVITE [IGNORED] [IGNORED]
        static final int INVITE = 0;
        //<#>ACCEPT [IGNORED] [IGNORED]
        static final int ANSWER_INVITE = 1;
        //<#>ADD_USER [PEER] [IGNORED]
        static final int ADD_USER = 2;
        //<#>REMOVE_USER [PEER] [IGNORED]
        static final int REMOVE_USER = 3;
        //<#>GREET_USER [PEER] [IGNORED]
        static final int GREET_USER = 4;
        //<#>MSG [MESSAGE] [IGNORED]
        static final int MSG = 5;
        //<#>REMOVE_RESOURCE [KEY] [IGNORED]
        static final int REMOVE_RESOURCE = 6;
        //<#>ADD_RESOURCE [KEY] [VALUE]
        static final int ADD_RESOURCE = 7;
    }

    private static final String SEPARATOR =  "\r";
    private static final String DEFAULT_IGNORED = "";
    private static final String MSG_SYNTAX_ACTION_ERR = "Parameter action out of range. Expected {0-7}, got: ";
    private static final String MSG_SYNTAX_ARG_ERR = "Parameter \"arg\" can't be empty for this action";
    private static final String MANAGER_LOG_TAG = "NETWORK_MANAGER";
    private static final int ACTION_POSITION = 0, ARG_POSITION = 1, EXTRA_POSITION = 2;

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
     * Method to build the body of a message that travels through the network
     * @param action the action to be sent
     * @param arg the argument for the action such as the key for a Resource
     * @param extra any extras related to the argument such as the value for a Resource
     * @throws InvalidMsgSyntaxException if the given parameters do not meet the criteria
     */
    private String composeMessageBody(int action, String arg, String extra) throws InvalidMsgSyntaxException {
        if(!Actions.isValid(action))
            throw new InvalidMsgSyntaxException(MSG_SYNTAX_ACTION_ERR + action);
        if(Actions.usesArg(action) && (arg == null || arg.isEmpty()))
            throw new InvalidMsgSyntaxException(MSG_SYNTAX_ARG_ERR);
        //String concatenation on null String turns it into "null". Should not be a problem.
        return action+SEPARATOR+arg+SEPARATOR+extra;
    }

    /**
     * Send a specific action to a single Peer
     * @param action the action to be sent
     * @param arg the argument for the action
     * @param extra any extras related to the argument
     * @param peer the peer to which the action is sent
     */
    private void send(int action, String arg, String extra, SMSPeer peer){
        if(peer.equals(myPeer)) return;
        String body = composeMessageBody(action,arg,extra);
        SMSMessage message = new SMSMessage(peer,body);
        handler.sendMessage(message);
    }

    /**
     * Method to send some kind of broadcast to the whole network
     * @param action the action to broadcast
     * @param arg the argument for the action
     * @param extra any extras related to the argument
     */
    private void broadcast(int action, String arg, String extra) {
        for(SMSPeer peer : getAvailablePeers()) {
            if (!peer.equals(myPeer))
                send(action, arg, extra, peer);
        }
    }

    /**
     * Method to send an invitation to a new User (Peer)
     * @param newPeer the Peer to invite
     */
    @Override
    public void invite(SMSPeer newPeer) {
        if(!isConnectedToPeer(newPeer))
        send(Actions.INVITE, DEFAULT_IGNORED, DEFAULT_IGNORED, newPeer);
    }

    /**
     * Method to accept an invitation received
     * @param inviter the user that sent the invitation
     */
    @Override
    public void acceptInvite(SMSPeer inviter) {
        if(!isPartOfNetwork){
            send(Actions.ANSWER_INVITE, DEFAULT_IGNORED, DEFAULT_IGNORED, inviter);
            dictionary.addPeer(inviter);
            isPartOfNetwork = true;
        }
    }

    /**
     * @author Turcato
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
        broadcast(Actions.GREET_USER, invited.getAddress(), DEFAULT_IGNORED);
        //This Peer wasn't part of a network but now it is since someone accepted its invitation.
        if(!isPartOfNetwork) isPartOfNetwork = true;
    }

    /**
     * Method to create a new valid Resource, and notify the network
     * @param newResource the new Resource
     */
    public void createResource(StringResource newResource) {
        dictionary.addResource(newResource);
        broadcast(Actions.ADD_RESOURCE,newResource.getName(),newResource.getValue());
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
        String usefulMessage = message.getData().substring(SMSHandler.APP_KEY.length());
        String[] splitMessage = usefulMessage.split(SEPARATOR);
        int action = Integer.parseInt(splitMessage[ACTION_POSITION]);
        String arg = "", extra = "";
        if(splitMessage.length > 2){
            arg = splitMessage[ARG_POSITION];
            extra = splitMessage[EXTRA_POSITION];
        }
        if(splitMessage.length > 1){
            arg = splitMessage[ARG_POSITION];
        }
        if(Actions.isValid(action)){
            performAction(action, arg, extra, message.getPeer());
        }
    }

    /**
     * Method to perform an action received through the Network
     * @param action the action to broadcast
     * @param arg the argument for the action
     * @param extra any extras related to the argument
     */
    private void performAction(int action, String arg, String extra, SMSPeer sendingPeer){
        switch(action){
            case Actions.INVITE:
                acceptInvite(sendingPeer);
                break;

            case Actions.ANSWER_INVITE:
                onInviteAccepted(sendingPeer);
                break;

            case Actions.ADD_USER:
                dictionary.addPeer(new SMSPeer(arg));
                break;

            case Actions.GREET_USER:
                SMSPeer newPeer = new SMSPeer(arg);
                dictionary.addPeer(newPeer);
                send(Actions.ADD_USER, myPeer.getAddress(), DEFAULT_IGNORED, newPeer);
                break;

            case Actions.REMOVE_USER:
                dictionary.removePeer(new SMSPeer(arg));
                break;

            case Actions.MSG:
                if(listener != null) listener.onMessageReceived(new SMSMessage(sendingPeer, arg));
                break;

            case Actions.ADD_RESOURCE:
                dictionary.addResource(new StringResource(arg, extra));
                break;

            case Actions.REMOVE_RESOURCE:
                dictionary.removeResource(new StringResource(arg,""));
                break;
        }
    }
}
