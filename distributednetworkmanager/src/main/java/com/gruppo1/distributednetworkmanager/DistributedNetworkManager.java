package com.gruppo1.distributednetworkmanager;

import android.content.Context;

import androidx.collection.ArraySet;

import com.dezen.riccardo.networkmanager.NetworkDictionary;
import com.dezen.riccardo.networkmanager.NetworkInterface;
import com.dezen.riccardo.networkmanager.OnNetworkEventListener;
import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.CommunicationHandler;
import com.dezen.riccardo.smshandler.ReceivedMessageListener;
import com.dezen.riccardo.smshandler.SMSManager;
import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.BitSet;
import java.util.Set;


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

    private int PENDING_ACTION;

    private RoutingTable routingTable;
    private Set<Resource> resources;
    private boolean isPartOfNetwork;
    private SMSPeer myPeer;
    private Node<BinarySet> myNode;
    private OnNetworkEventListener<SMSMessage, StringResource> listener;
    private CommunicationHandler<SMSMessage> handler;
    private Context context;
    private final String MANAGER_TAG = "DISTRIBUTED_MANAGER_TAG";
    private final int ADDRESS_LENGHT = 128;

    /**
     * This version of the constructor should be used to insert the peer that builds the object
     * (if it is a peer for the sms Network)
     *
     * @param firstPeer the peer tha builds the network
     */
    public DistributedNetworkManager(Context registerContext, SMSPeer firstPeer) {
        //TODO instantiate routing table
        routingTable = null;
        resources = new ArraySet<>();
        handler = SMSManager.getInstance(registerContext);
        handler.setReceiveListener(this);
        context = registerContext;
        myPeer = firstPeer;
        BinarySet myAddress = new BinarySet(BitSetUtils.hash(firstPeer.getAddress().getBytes(), ADDRESS_LENGHT));
        myNode = new PeerNode(myAddress);
        ((PeerNode)myNode).setPhysicalPeer(myPeer);
        isPartOfNetwork = false;
    }

    /**
     * Method to send an invitation to a new User (Peer)
     *
     * @param newPeer the Peer to invite
     */
    public void invite(SMSPeer newPeer) {
        //TODO build an invite action
        // set PENDING_ACTION to the value for that action
        // convert the Action to sms and send it to newPeer
    }

    /**
     * Method to accept an invitation received
     *
     * @param inviter the user that sent the invitation
     */
    public void acceptInvite(SMSPeer inviter) {
        //TODO answer with a positive or negative action
    }

    /**
     * Method to perform the bootstrap of the network
     * Asks to the inviter Nodes to insert in its own rt
     * @param inviter the Peer of the network who invited myPeer
     */
    private void bootstrap(PeerNode inviter){
        BinarySet mySet = myNode.getKey();
        NodeActionStructure searchAction;
        for (int i = 0; i < ADDRESS_LENGHT; i++){

            Node<BinarySet> searchNode = new PeerNode(getFurthest(mySet, ADDRESS_LENGHT-(i+1)));

            //Create action Find Node,  forward to inviter
            //No need to wait here, PendingRequestManager will add the searched node once there will be a response
            //If the prManager wont receive response it will act accordingly to its setup
        }
    }

    /**
     * @param binaryAddress a given binary address of the network
     * @param index the index of the first most significant bit to keep equal to the given address
     * @return the furthest Binary address with the most significant bits (Starting from bit i) equal to given set
     */
    private BinarySet getFurthest(BinarySet binaryAddress, int index){
        BitSet address = binaryAddress.getKey();
        for (int i = 0; i < index; i++)
            address.set(i, !address.get(i));

        return new BinarySet(address);
    }

    /**
     * Method to request a Resource from the network.
     *
     * @param key the key of Resource to request.
     */
    public StringResource getResource(String key) {
        //TODO answer with the resource if available
        return null;
    }

    /**
     * Method to get an array of the Peers.
     *
     * @return array containing all Available Peers.
     */
    public SMSPeer[] getAvailablePeers() {
        //TODO return the peers from the routing table
        return null;
    }

    /**
     * Method to get an array of the Resources.
     *
     * @return array containing all Available Resources.
     */
    public StringResource[] getAvailableResources() {
        //yadda yadda
        return null;
    }

    /**
     * Setter for a listener that should listen for Resources being obtained.
     *
     * @param listener the class listening for Resource events.
     */
    public void setListener(OnNetworkEventListener<SMSMessage, StringResource> listener) {

    }

    public void ping(SMSPeer pingedPeer) {
        //TODO build ping action
    }

    public void storeResource(StringResource resourceToSostore, SMSPeer destination) {
        //TODO build ping action
    }

    public void findNode() {
        //TODO build findNode action
    }

    /**
     * Called when a message is received
     *
     * @param message the received message that needs to be forwarded
     */
    public void onMessageReceived(SMSMessage message) {
        //TODO if it's a Request then Answer, else Elaborate the result if it is a Response
    }
}
