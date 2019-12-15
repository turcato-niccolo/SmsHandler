package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.DistributedNetworkAction;
import com.gruppo1.distributednetworkmanager.RequestResultListener;
import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PingPendingRequestTest {

    /**
     * DistributedNetworkAction is not currently fully working, mock uses expected behaviour
     */
    @Mock
    DistributedNetworkAction pingAction;
    @Mock
    DistributedNetworkAction responseAction;
    @Mock
    DistributedNetworkAction invalidAction;

    private int counter = 0;

    private final String CORRECT_ID = "0";
    private final String INCORRECT_ID = "4";

    private class StubListener implements RequestResultListener<SMSPeer>{
        @Override
        public void onInviteResult(SMSPeer invited, boolean accepted) {
        }
        @Override
        public void onPingResult(SMSPeer pinged, boolean isOnline) {
            counter++;
        }
        @Override
        public void onStoreResult(Resource storedResource, SMSPeer newOwner) {
        }
        @Override
        public void onFindValueResult(SMSPeer owner, Resource resource) {
        }
    }

    @Before
    public void initMocks(){
        counter = 0;
        when(pingAction.getAction()).thenReturn(DistributedNetworkAction.Type.PING);
        when(responseAction.getAction()).thenReturn(DistributedNetworkAction.Type.ANSWER_PING);
        when(invalidAction.getAction()).thenReturn(DistributedNetworkAction.Type.STORE);
        when(pingAction.getActionID()).thenReturn(CORRECT_ID);
        when(responseAction.getActionID()).thenReturn(CORRECT_ID);
        when(invalidAction.getActionID()).thenReturn(INCORRECT_ID);
    }

    @Test(expected = NullPointerException.class)
    public void pingPR_NonNull(){
        new PingPendingRequest(null, null);
    }

    @Test
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
        assertEquals(Integer.parseInt(pingAction.getActionID()), request.getCode());
    }

    @Test
    public void isPertitent_valid(){

    }

    @Test
    public void isPertinent_invalid(){

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
    }
}
