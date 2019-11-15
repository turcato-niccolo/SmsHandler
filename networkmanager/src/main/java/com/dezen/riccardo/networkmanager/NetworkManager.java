package com.dezen.riccardo.networkmanager;

import android.content.Context;
import android.util.Log;

import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;
import com.dezen.riccardo.smshandler.SmsHandler;

import java.util.ArrayList;

/**
 * @author Niccolò Turcato. Invitation sending and receiving. Switch actions on Message received.
 * @author Riccardo De Zen. User management and synchronization. Enumeration to define actions.
 */
public class NetworkManager extends NetworkInterface<SMSPeer,StringResource, NetworkDictionary> implements SmsHandler.OnSmsEventListener {
    private enum Actions{
        INVITE(0),ACCEPT(1),
        ADD_USER(2),REMOVE_USER(3),
        NOTIFY_USER(4),SMILE(5);
        private int val;
        Actions(int num){
            val = num;
        }
        public int value(){return val;}
    }
    private static final String[] actionMessages = {
            "INVITE",
            "ACCEPT",
            "ADD_USER",
            "REMOVE_USER",
            "NOTIFY_USER",
            "SMILE"
    };

    private SMSPeer myPeer;
    private OnNetworkEventListener<StringResource> listener;
    private NetworkDictionary peersVocabulary;
    private SmsHandler handler;
    private Context context;
    private ArrayList<String> pendingInvitations;
    private final String MANAGER_TAG = "MANAGER_TAG";

    public NetworkManager(Context registerContext) {
         peersVocabulary = new NetworkDictionary(registerContext);
         handler = new SmsHandler();
         handler.registerReceiver(registerContext, true, true, true);
         handler.setListener(this);
         context = registerContext;
         pendingInvitations = new ArrayList<String>();
    }

    /**
     * This version of the constructor should be used to insert the peer that builds the object
     * (if it is a peer for the sms Network)
     * @param firstPeer
     */
    public NetworkManager(Context registerContext, SMSPeer firstPeer) {
        this(registerContext);
        peersVocabulary.addPeer(firstPeer);
        myPeer = firstPeer;
    }

    /**
     * Method to send an invitation to a new User (Peer)
     * @param newPeer the Peer to invite
     */
    @Override
    public void invite(SMSPeer newPeer) {
        handler.sendSMS(context, newPeer.getAddress(), actionMessages[Actions.INVITE.value()]);
        pendingInvitations.add(newPeer.getAddress());
    }

    /**
     * Method to accept an invitation received
     * @param inviter the user that sent the invitation
     */
    @Override
    public void acceptInvite(SMSPeer inviter) {
        handler.sendSMS(context, inviter.getAddress(), actionMessages[Actions.ACCEPT.value()]);
        peersVocabulary.addPeer(inviter);
    }

    /**
     * Method for when a user has accepted the invitation
     * @param invited
     */
    private void onInviteAccepted(SMSPeer invited) {
        Log.d(MANAGER_TAG, invited.getAddress() +" has accepted");
        peersVocabulary.addPeer(invited);
        broadcast(actionMessages[Actions.ADD_USER.value()]+" "+invited.getAddress());
        broadcast(actionMessages[Actions.NOTIFY_USER.value()]+" "+invited.getAddress());
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
        return peersVocabulary.getPeers();
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
    public void setListener(OnNetworkEventListener<StringResource> listener) {
        this.listener = listener;
    }

    /**
     * Method to send some kind of broadcast to the whole network
     * @param action the action to broadcast
     */
    @Override
    public void broadcast(String action) {
        for(SMSPeer peer : getAvailablePeers()){
            if(!peer.equals(myPeer))handler.sendSMS(context, peer.getAddress(), action);
        }
    }

    public void smile(){
        broadcast(actionMessages[Actions.SMILE.value()]);
    }

    @Override
    public void onReceive(SMSMessage message) {
        String receivedMessageString = message.getData();
        if(receivedMessageString.contains(actionMessages[Actions.INVITE.value()])){
            //Message contains invitation
            SMSPeer inviter = message.getPeer();
            acceptInvite(inviter);
        }

        if(receivedMessageString.contains(actionMessages[Actions.ACCEPT.value()])){
            //Message contains invitation acceptance
            SMSPeer acceptingPeer = message.getPeer();
            onInviteAccepted(acceptingPeer);
        }

        if(receivedMessageString.contains(actionMessages[Actions.ADD_USER.value()])){
            //Add the specified user
            peersVocabulary.addPeer(new SMSPeer(receivedMessageString.split(" ")[1]));
        }

        if(receivedMessageString.contains(actionMessages[Actions.NOTIFY_USER.value()])){
            //Notifies the specified user that it should add this user
            broadcast(actionMessages[Actions.ADD_USER.value()]+" "+myPeer.getAddress());
        }

        if(receivedMessageString.contains(actionMessages[Actions.SMILE.value()])){
            if(listener != null) listener.onMessageReceived(message);
        }
    }
    @Override
    public void onSent(int resultCode, SMSMessage message) {

    }

    @Override
    public void onDelivered(int resultCode, SMSMessage message) {

    }
}
