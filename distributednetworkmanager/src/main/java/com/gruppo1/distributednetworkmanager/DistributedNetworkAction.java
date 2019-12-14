package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.smshandler.Message;
import com.dezen.riccardo.smshandler.Peer;

/**
 * Interface defining standard behaviour for an Action travelling through a Network.
 * @param <P> type of Peer
 * @param <M> type of Message
 * @author Turcato, De Zen
 */
public interface DistributedNetworkAction<D, P extends Peer, M extends Message<D, P>> {
    /**
     * @return true if the defined action is valid and fits into a Message
     */
    boolean isValid();

    /**
     * @return the Peer for this Action
     */
    P getPeer();

    /**
     * @return a Message, containing the formatted action command, ready to be sent
     */
    M toMessage();

    /**
     * @return the D type representation of this Action, as it would be written in the Message.
     */
    D getPayload();
}
