package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.gruppo1.distributednetworkmanager.exceptions.InvalidActionException;

import org.junit.Test;

/**
 * Tests for {@link PendingRequestFactory}
 * @author Riccardo De Zen
 */
public class PendingRequestFactoryTest {
    @Test
    public void factoryBuildsInviteRequest(){

    }

    @Test
    public void factoryBuildsPingRequest(){

    }

    @Test
    public void factoryBuildsFindNodeRequest(){

    }

    @Test
    public void factoryBuildsFindValueRequest(){

    }

    @Test
    public void factoryBuildsStoreRequest(){

    }

    @Test(expected = InvalidActionException.class)
    public void factoryWontBuildFromResponse(){

    }
}