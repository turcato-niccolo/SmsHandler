package com.gruppo1.distributednetworkmanager;

import com.gruppo1.distributednetworkmanager.exceptions.InvalidRequestException;
import com.gruppo1.distributednetworkmanager.pendingrequests.PendingRequestFactory;

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

    @Test(expected = InvalidRequestException.class)
    public void factoryWontBuildFromResponse(){

    }
}