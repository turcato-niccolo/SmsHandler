package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.ActionPropagator;
import com.gruppo1.distributednetworkmanager.KadAction;
import com.gruppo1.distributednetworkmanager.Node;
import com.gruppo1.distributednetworkmanager.NodeDataProvider;
import com.gruppo1.distributednetworkmanager.listeners.PingResultListener;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class PingPendingRequestTest {

    private int propagatedActions = 0;
    private boolean pingEnded = false;

    private ActionPropagator propagator = new StubActionPropagator();
    private NodeDataProvider provider = new StubNodeProvider();
    private PingResultListener listener = new StubPingListener();

    private SMSPeer testPeer = new SMSPeer("+39892424");
    private final KadAction pingAction = new KadAction(testPeer, KadAction.ActionType.PING,1,1,1, KadAction.PayloadType.IGNORED, " ");
    private final KadAction pingResponse = new KadAction(testPeer, KadAction.ActionType.PING_ANSWER,1,1,1, KadAction.PayloadType.IGNORED, " ");
    //private final KadAction invalidAction = new KadAction(testPeer, KadAction.ActionType.STORE,1,1,1, KadAction.PayloadType.RESOURCE, "a\ra");

    //Stub propagator
    private class StubActionPropagator implements ActionPropagator{
        @Override
        public void propagateAction(KadAction action) {
            propagatedActions++;
        }
    }
    //Stub provider
    private class StubNodeProvider implements NodeDataProvider{
        @Override
        public Node getClosest(Object target) {
            return null;
        }
        @Override
        public List getKClosest(int k, Object target) {
            return null;
        }
        @Override
        public List filterKClosest(int k, Object target, List nodes) {
            return null;
        }
    }
    //Stub listener
    private class StubPingListener implements PingResultListener{
        @Override
        public void onPingResult(int operationId, SMSPeer pinged, boolean isOnline) {
            pingEnded = true;
        }
    }

    /*@Test
    public void pingPR_takesPingAction(){
        PingPendingRequest request = new PingPendingRequest(pingAction, new StubListener());
        assertNotNull(request);
    }

    @Test(expected = InvalidActionException.class)
    public void pingPR_onlyTakesPingAction(){
        //TODO all enum values
        new PingPendingRequest(invalidAction, new StubListener());
    }

    @Test
    public void getCode_returns(){
        PingPendingRequest request = new PingPendingRequest(pingAction, new StubListener());
        assertEquals(Integer.parseInt(pingAction.getActionID()), request.getOperationId());
    }

    @Test
    public void isPertitent_valid(){
        PingPendingRequest request = new PingPendingRequest(pingAction, new StubListener());
        assertTrue(request.isPertinent(pingAction));
    }

    @Test
    public void isPertinent_invalid(){
        PingPendingRequest request = new PingPendingRequest(pingAction, new StubListener());
        assertFalse(request.isPertinent(invalidAction));
    }

    @Test
    public void pingPR_continuesOnValidAction(){
        PingPendingRequest request = new PingPendingRequest(pingAction, new StubListener());
        request.nextStep(responseAction);
        assertEquals(1, counter);
    }

    @Test
    public void pingPR_wontContinueOnInvalidAction(){
        PingPendingRequest request = new PingPendingRequest(pingAction, new StubListener());
        request.nextStep(invalidAction);
        assertEquals(0, counter);
    }*/
}
