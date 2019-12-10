package com.gruppo1.distributednetworkmanager;

import android.content.Context;

import com.dezen.riccardo.networkmanager.*;
import com.dezen.riccardo.smshandler.*;
import com.gruppo1.distributednetworkmanager.deprecated.NetworkDictionaryWithOwnership;


/**
 * @author Niccolo' Turcato
 * Basic implementation of a distibuted Network based on Kademlia
 * Using the already built NetworkDictionary which has a list of
 */

public class DistributedNetworkManager implements NetworkInterface<SMSMessage, SMSPeer, StringResource, NetworkDictionary>, ReceivedMessageListener<SMSMessage> {

    /**
     * Actions the network can send and receive.
     * Current syntax for messages is as follows:
     * <SMSLibrary-TAG>[ACTION]<separation>[PARAMETER]<separation>[EXTRA]
     */
    private static class DistributedActions {
        static String PING() {
            return "PING";
        }

        static String STORE() {
            return "STORE";
        }

        static String FIND_NODE() {
            return "FIND_NODE";
        }

        static String FIND_VALUE() {
            return "FIND_VALUE";
        }
    }

    private NetworkDictionaryWithOwnership dictionary;
    private boolean isPartOfNetwork;
    private SMSPeer myPeer;
    private OnNetworkEventListener<SMSMessage, StringResource> listener;
    private CommunicationHandler<SMSMessage> handler;
    private Context context;
    private final String MANAGER_TAG = "DISTRIBUTED_MANAGER_TAG";


    public DistributedNetworkManager(Context registerContext) {
        dictionary = new NetworkDictionaryWithOwnership(registerContext);
        handler = SMSManager.getInstance(registerContext);
        handler.setReceiveListener(this);
        context = registerContext;
        isPartOfNetwork = false;
    }

    /**
     * This version of the constructor should be used to insert the peer that builds the object
     * (if it is a peer for the sms Network)
     *
     * @param firstPeer the peer tha builds the network
     */
    public DistributedNetworkManager(Context registerContext, SMSPeer firstPeer) {
        this(registerContext);
        dictionary.addPeer(firstPeer);
        myPeer = firstPeer;
    }

    /**
     * Method to send an invitation to a new User (Peer)
     *
     * @param newPeer the Peer to invite
     */
    public void invite(SMSPeer newPeer) {

    }

    /**
     * Method to accept an invitation received
     *
     * @param inviter the user that sent the invitation
     */
    public void acceptInvite(SMSPeer inviter) {

    }

    /**
     * Method to request a Resource from the network.
     *
     * @param key the key of Resource to request.
     */
    public StringResource getResource(String key) {
        return null;
    }

    /**
     * Method to get an array of the Peers.
     *
     * @return array containing all Available Peers.
     */
    public SMSPeer[] getAvailablePeers() {
        return null;
    }

    /**
     * Method to get an array of the Resources.
     *
     * @return array containing all Available Resources.
     */
    public StringResource[] getAvailableResources() {
        return null;
    }

    /**
     * Setter for a listener that should listen for Resources being obtained.
     *
     * @param listener the class listening for Resource events.
     */
    public void setListener(OnNetworkEventListener<SMSMessage, StringResource> listener) {

    }

    public void ping(SMSPeer peerToPing) {
        //Async Task maybe
    }

    public void storeResource(StringResource resourceToSostore, SMSPeer destination) {

    }

    public void findNode() {

    }

    /**
     * Called when a message is received
     *
     * @param message the received message that needs to be forwarded
     */
    public void onMessageReceived(SMSMessage message) {

    }
}
