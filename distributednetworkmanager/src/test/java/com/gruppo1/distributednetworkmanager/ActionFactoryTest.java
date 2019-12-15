package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.networkmanager.NetworkAction;
import com.dezen.riccardo.smshandler.SMSPeer;
import com.gruppo1.distributednetworkmanager.exceptions.IllegalNumberOfParamsException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActionFactoryTest {
    ActionFactory<String> factory;

    @Before
    public void init(){
        factory = new ActionFactory<>();
    }

    @Test
    public void ActionFactoryDistributedActionTest(){
        String[] params = new String[]{
                "FFF000",
                DistributedNetworkAction.DEFAULT_IGNORED,
                DistributedNetworkAction.DEFAULT_IGNORED
        };
        DistributedNetworkAction action = (DistributedNetworkAction)factory.getStringAction(
                ActionFactory.Network.DISTRIBUTED, DistributedNetworkAction.Type.PING, params);
        action.setDestinationPeer(new SMSPeer("+390425000"));
        assertTrue(action.isValid());
    }

    @Test(expected = IllegalNumberOfParamsException.class)
    public void ActionFactoryDistributedActionWrongNumParamsTest(){
        String[] params = new String[]{
                DistributedNetworkAction.DEFAULT_IGNORED,
        };
        DistributedNetworkAction action = (DistributedNetworkAction)factory.getStringAction(
                ActionFactory.Network.DISTRIBUTED, DistributedNetworkAction.Type.PING, params);
    }

    @Test
    public void ActionFactoryReplicatedActionTest(){
        String[] params = new String[]{
                NetworkAction.DEFAULT_IGNORED,
                NetworkAction.DEFAULT_IGNORED,
        };
        NetworkAction action = (NetworkAction) factory.getStringAction(
                ActionFactory.Network.REPLICATED, NetworkAction.Type.INVITE, params);
        action.setDestinationPeer(new SMSPeer("+390425"));
        assertTrue(action.isValid());
    }

    @Test(expected = IllegalNumberOfParamsException.class)
    public void ActionFactoryReplicatedActionWrongNumParamsTest(){
        String[] params = new String[]{
                NetworkAction.DEFAULT_IGNORED,
        };
        NetworkAction action = (NetworkAction) factory.getStringAction(
                ActionFactory.Network.REPLICATED, NetworkAction.Type.INVITE, params);
    }

}