package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.NodeUtils;
import com.gruppo1.distributednetworkmanager.PeerNode;
import com.gruppo1.distributednetworkmanager.listeners.FindNodeResultListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.BitSet;

@RunWith(RobolectricTestRunner.class)
public class FindNodeTest {

    private FindNodeResultListener listener = new FindNodeResultListener() {
        @Override
        public void onFindNodeResult(int operationId, BitSet target, PeerNode closest) {
            actualClosest = closest;
        }
    };

    private final int operationId = 1;
    private final int networkSize = 100;

    private FindNodePendingRequest request;
    private PeerNode hostPeer;
    private PeerNode targetPeer;
    private FakeNetwork network;

    private SMSPeer expectedClosest;
    private PeerNode actualClosest;

    @Before
    public void init(){
        hostPeer = NodeUtils.getNodeForPeer(
                FakeNetwork.randomPeer(),
                FakeNetwork.KEY_LENGTH
        );
        targetPeer = NodeUtils.getNodeForPeer(
                FakeNetwork.randomPeer(),
                FakeNetwork.KEY_LENGTH
        );
        network = new FakeNetwork(networkSize,hostPeer);
        request = new FindNodePendingRequest(
                operationId,
                targetPeer.getAddress(),
                network,
                network.getMyClient(),
                listener
        );
    }

    @Test
    public void pliz(){
        System.out.println("At least I started");
        expectedClosest = network.getExpectedClosest(targetPeer.getAddress()).getPhysicalPeer();
        network.handleRequest(request);
        Assert.assertEquals(expectedClosest,actualClosest);
    }

}
