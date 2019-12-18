package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.BinarySet;
import com.gruppo1.distributednetworkmanager.BitSetUtils;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.NodeUtils;
import com.gruppo1.distributednetworkmanager.PeerNode;

import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class FakeNetwork implements ActionPropagator {
    final static int KEY_LENGTH = 128;
    //Value for k, actually representing
    final int CUBIC_K;
    final int SQUARED_K;
    final int BASE_K;

    private List<FakeClient> allClients = new ArrayList<>();
    private FakeClient myClient;
    private PendingRequest currentRequest;
    /**
     * @param numberOfNodes
     */
    public FakeNetwork(int numberOfNodes, PeerNode firstPeer){
        CUBIC_K = numberOfNodes;
        //Surely lower than MAX_INT
        SQUARED_K = (int)Math.max(Math.round(Math.pow((double)numberOfNodes, 2.0/3.0)),1);
        BASE_K = (int)Math.max(Math.sqrt(SQUARED_K),1);
        myClient = new FakeClient(firstPeer);
        fillList();
        generateMeetings();
    }

    /**
     * Class defining the fake Clients
     */
    public class FakeClient implements NodeDataProvider<BinarySet,PeerNode> {
        private PeerNode peer;
        private TreeMap<BinarySet,PeerNode> fakeTable = new TreeMap<>();
        //Constructor
        public FakeClient(PeerNode owner){
            peer = owner;
        }
        //Method to add a Node to the table
        public void meet(PeerNode metNode){
            if(!metNode.equals(peer))
                fakeTable.put(peer.getDistance(metNode), metNode);
        }
        //Getter for peer
        public PeerNode getPeer() {
            return peer;
        }
        //Getter for size of Routing Table
        public int size(){
            return fakeTable.size();
        }
        @Override
        public PeerNode getClosest(BinarySet target) {
            return fakeTable.get(fakeTable.firstKey());
        }
        @Override
        public List<PeerNode> getKClosest(int k, BinarySet target) {
            TreeMap<BinarySet,PeerNode> buffer = new TreeMap<>();
            List<PeerNode> result = new ArrayList<>();
            while(result.size() < k && !fakeTable.isEmpty()){
                PeerNode possibleAddition = fakeTable.get(fakeTable.firstKey());
                if(possibleAddition != null && possibleAddition.getAddress().getDistance(target).compareTo(peer.getAddress().getDistance(target)) < 0)
                    result.add(possibleAddition);
                buffer.put(fakeTable.firstKey(), fakeTable.remove(fakeTable.firstKey()));
            }
            //Emptying the buffer
            while(buffer.size() > 0) fakeTable.put(buffer.firstKey(),buffer.remove(buffer.firstKey()));
            return result;
        }
        @Override
        public List<PeerNode> filterKClosest(int k, BinarySet target, List<PeerNode> nodes) {
            TreeMap<BinarySet,PeerNode> buffer = new TreeMap<>();
            for(PeerNode node : nodes){
                buffer.put(target.getDistance(node.getAddress()), node);
            }
            List<PeerNode> result = new ArrayList<>();
            while(result.size() < k && !buffer.isEmpty()){
                result.add(buffer.get(buffer.firstKey()));
            }
            return result;
        }
    }

    //Getter for myClient
    public FakeClient getMyClient(){
        return myClient;
    }

    /**
     * @return a randomly generated Peer
     */
    public static SMSPeer randomPeer(){
        StringBuilder address = new StringBuilder("+");
        int digits = Math.abs(new Random().nextInt() % 10) + 5;
        for(int i = 0; i < digits; i++){
            int nextChar = Math.abs(new Random().nextInt() % 10);
            address.append(nextChar);
        }
        return new SMSPeer(address.toString());
    }

    /**
     * Method filling the list with the correct amount of Fake Clients
     */
    private void fillList(){
        allClients.add(myClient);
        while(allClients.size() < CUBIC_K){
            PeerNode node = NodeUtils.getNodeForPeer(randomPeer(),KEY_LENGTH);
            allClients.add(new FakeClient(node));
        }
    }

    /**
     * Method adding SQUARED_K nodes to each table.
     * Every node gets to know the next in the list to be sure the Network is connected
     */
    private void generateMeetings(){
        Random random = new Random();
        for(int i = 0; i < allClients.size(); i++){
            FakeClient client = allClients.get(i);
            FakeClient nextClient = allClients.get((i+1)%allClients.size());
            client.meet(nextClient.getPeer());
            while(client.size() < SQUARED_K){
                int index = Math.abs(random.nextInt()) % allClients.size();
                client.meet(allClients.get(index).getPeer());
            }
        }
    }

    /**
     * @param target a BinarySet
     * @return the closest BinarySet to target
     */
    public PeerNode getExpectedClosest(BinarySet target){
        TreeMap<BinarySet, PeerNode> buffer = new TreeMap<>();
        for(FakeClient client : allClients)
            buffer.put(target.getDistance(client.getPeer().getAddress()), client.getPeer());
        return buffer.get(buffer.firstKey());
    }

    /**
     * @param request a request to start and handle if possible
     */
    public void handleRequest(FindNodePendingRequest request){
        currentRequest = request;
        currentRequest.start();
    }

    /**
     * @param client the client that answers the Request
     */
    public void answer(int id, FakeClient client, BinarySet target){
        List<PeerNode> closestNodes = client.getKClosest(BASE_K, target);
        if(closestNodes.isEmpty())
            currentRequest.nextStep(
                    new KadAction(
                            client.getPeer().getPhysicalPeer(),
                            KadAction.ActionType.FIND_NODE_ANSWER,
                            id,1,1,
                            KadAction.PayloadType.IGNORED,
                            "owO")
                    );
        for(int i = 1; i <= closestNodes.size(); i++){
            currentRequest.nextStep(
                    new KadAction(
                            client.getPeer().getPhysicalPeer(),
                            KadAction.ActionType.FIND_NODE_ANSWER,
                            id,i,closestNodes.size(),
                            KadAction.PayloadType.NODE_ID,
                            BitSetUtils.BitSetsToHex(closestNodes.get(i-1).getAddress().getKey())
                    )
            );
        }
    }

    @Override
    public void propagateAction(KadAction action) {
        System.out.println("Sup");
        if(action.getActionType() == KadAction.ActionType.FIND_NODE){
            int id = action.getOperationId();
            PeerNode destination = NodeUtils.getNodeForPeer(action.getPeer(), KEY_LENGTH);
            BinarySet target = new BinarySet(BitSetUtils.decodeHexString(action.getPayload()));
            FakeClient client = null;
            for(FakeClient eachClient : allClients){
                if(eachClient.getPeer().equals(destination)){
                    client = eachClient;
                    break;
                }
            }
            if(client != null){
                System.out.println("Gonna answer bruv");
                answer(id, client, target);
            }
        }
    }
}
