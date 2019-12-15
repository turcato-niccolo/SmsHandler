package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.networkmanager.ActionStructure;
import com.dezen.riccardo.networkmanager.NetworkAction;
import com.gruppo1.distributednetworkmanager.exceptions.IllegalNumberOfParamsException;


public class ActionFactory<T extends Comparable<T>> {
    private static final String ILLEGAL_NUMBER_OF_PARAMS_MSG = "Illegal number of params";
    private static final int NUMBER_PARAMS_REPLICATED_N = 2;
    private static final int NUMBER_PARAMS_DISTRIBUTED_N = 3;
    //Looks like hard wiring, but the specific classed define a finite number of params in their interface

    public enum Network {
        DISTRIBUTED,
        REPLICATED
    }

    /**
     * @param network    type of network for which generate an action
     * @param actionType a numeric code that defines the action type (found in the specific class)
     * @param params     an array containing the parameter for the action to build
     * @return an object of the selected type of action inside the abstract container, containing a formatted action command
     * @throws IllegalArgumentException       if parameters aren't valid (refer to the specific action class)
     * @throws IllegalNumberOfParamsException if the number of given params is not coherent with the type of network
     */
    public ActionStructure<String> getStringAction(Network network, int actionType, String[] params) {
        switch (network) {
            case REPLICATED:
                if (params.length == getNumOfParams(Network.REPLICATED)) {
                    return new NetworkAction(actionType, params[0], params[1]);
                } else throw new IllegalNumberOfParamsException(ILLEGAL_NUMBER_OF_PARAMS_MSG);

            case DISTRIBUTED:
                if (params.length == getNumOfParams(Network.DISTRIBUTED)) {
                    return new DistributedNetworkAction(actionType, params[0], params[1], params[2]);
                } else throw new IllegalNumberOfParamsException(ILLEGAL_NUMBER_OF_PARAMS_MSG);
        }
        return null;
    }

    /**
     * @param network type of Network (enum Network)
     * @return the number of params defined by the specific action classes of the indicated Network, 0 if the number of params is not defined
     */
    private static int getNumOfParams(Network network) {
        switch (network) {
            case REPLICATED:
                return NUMBER_PARAMS_REPLICATED_N;
            case DISTRIBUTED:
                return NUMBER_PARAMS_DISTRIBUTED_N;
        }
        return 0;
    }


}
