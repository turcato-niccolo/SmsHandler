package com.gruppo1.distributednetworkmanager;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.dezen.riccardo.smshandler.SMSPeer;

import org.junit.Before;

import static org.junit.Assert.*;

public class DistributedNetworkManagerTest {

    private DistributedNetworkManager manager;
    private SMSPeer myPeer = new SMSPeer("+393456781999");
    private Context instrumentationContext;

    @Before
    public void init(){
        instrumentationContext = InstrumentationRegistry.getInstrumentation().getContext();
        manager = new DistributedNetworkManager(instrumentationContext, myPeer);
    }


}