package com.gruppo1.distributednetworkmanager.pendingrequests;

import com.gruppo1.distributednetworkmanager.NodeUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;

import java.util.Arrays;
import java.util.Collection;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class FakeNetworkTest {

    private FakeNetwork testNetwork;
    private int size;
    private int expectedSquared;
    private int expectedBase;

    @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"K^3 = 3",3,2,1},
                {"K^3 = 8",8,4,2},
                {"K^3 = 125",125,25,5},
                {"K^3 = 8000",8000,400,20}
        });
    }

    /**
     * Constructor for this parameterized test
     * @param testName
     * @param size
     * @param expectedSquared
     * @param expectedBase
     */
    public FakeNetworkTest(String testName, int size, int expectedSquared, int expectedBase) {
        this.size = size;
        this.expectedSquared = expectedSquared;
        this.expectedBase = expectedBase;
    }

    @Before
    public void setup(){
        testNetwork = new FakeNetwork(
                size,
                NodeUtils.getNodeForPeer(FakeNetwork.randomPeer(), FakeNetwork.KEY_LENGTH)
        );
    }

    @Test
    public void constructor_size(){
        Assert.assertEquals(testNetwork.BASE_K,expectedBase);
        Assert.assertEquals(testNetwork.SQUARED_K,expectedSquared);
        Assert.assertEquals(testNetwork.CUBIC_K,size);
    }

    @Test
    public void bruteForceRandomPeer(){
        for(int i = 0; i < 100; i++){
            FakeNetwork.randomPeer();
        }
    }
}
