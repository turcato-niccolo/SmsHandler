package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.Message;
import com.dezen.riccardo.smshandler.Peer;

/**
 * Interface defining generic behaviour of a Network using replicated data: a list of all Peers and
 * Resources is always available. Since we do not know the keys for Peer and Resources, get methods
 * are not specified.
 * Network is assumed to contain any kind of Resource.
 * @author Riccardo De Zen.
 * @param <M> The type of Message this Network uses to communicate
 * @param <P> The type of Peer this Network connects
 */
public interface ReplicatedNetworkInterface<M extends Message, P extends Peer>
        extends NetworkInterface<M, P> {
    /**
     * @return an array containing all the Peers in the Network.
     */
    P[] getAvailablePeers();

    /**
     * @return a list of all available Resources in the Network.
     */
    Resource[] getAvailableResources();
}
