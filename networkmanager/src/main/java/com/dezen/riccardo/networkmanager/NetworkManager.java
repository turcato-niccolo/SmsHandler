package com.dezen.riccardo.networkmanager;

import android.content.Context;
import android.util.Log;

import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;
import com.dezen.riccardo.smshandler.SmsHandler;

import java.util.ArrayList;

public class NetworkManager extends NetworkInterface<SMSPeer,StringResource,NetworkVocabulary> implements SmsHandler.OnSmsEventListener {
    NetworkVocabulary peersVocabulary;
    SmsHandler handler;
    private final String[] invitationMessages = {"INVITATION_PROPOSED", "INVITATION_ACCEPTED"};
    private final int propose = 0, accept = 1;
    Context context;
    ArrayList<String> pendingInvitations;
    private final String MANAGER_TAG = "MANAGER_TAG";

    public NetworkManager(Context registerContext)
    {
         peersVocabulary = new NetworkVocabulary();
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
    public NetworkManager(Context registerContext, SMSPeer firstPeer)
     {
        this(registerContext);
        peersVocabulary.addPeer(firstPeer);
     }

    /**
     * Method to send an invitation to a new User (Peer)
     * @param newPeer the Peer to invite
     */
    @Override
    public void invite(SMSPeer newPeer) {
        handler.sendSMS(context, newPeer.getAddress(), invitationMessages[propose]);
        pendingInvitations.add(newPeer.getAddress());
    }

    /**
     * Method to accept an invitation received
     * @param inviter the user that sent the invitation
     */
    @Override
    public void acceptInvite(SMSPeer inviter) {
        handler.sendSMS(context, inviter.getAddress(), invitationMessages[accept]);
        peersVocabulary.addPeer(inviter);
    }

    private void onInviteAccepted(SMSPeer invited)
    {
        Log.d(MANAGER_TAG, invited.getAddress() +" has accepted");
        peersVocabulary.addPeer(invited);
            //pendingInvitations.remove(invited.getAddress());

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
    public void setListener(OnResourceObtainedListener<StringResource> listener) {

    }

    @Override
    public void onReceive(SMSMessage message)
    {
        String receivedMessageString = message.getData();
        if(receivedMessageString.contains(invitationMessages[propose]))
        { //Message contains invitation
            SMSPeer inviter = message.getPeer();
            acceptInvite(inviter);
        }

        if(receivedMessageString.contains(invitationMessages[accept]))
        {//Message contains invitation acceptance
            SMSPeer acceptingPeer = message.getPeer();
            onInviteAccepted(acceptingPeer);
        }
    }
    @Override
    public void onSent(int resultCode, SMSMessage message)
    {

    }

    @Override
    public void onDelivered(int resultCode, SMSMessage message)
    {

    }
}
