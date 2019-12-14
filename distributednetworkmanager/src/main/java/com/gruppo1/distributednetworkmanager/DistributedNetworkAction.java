package com.gruppo1.distributednetworkmanager;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

/**
 * @param <T> type of the params and Payload
 *            <p>
 *            Syntax: [ID] [k/N] [HEADER:action code] [PARAM: node id] [EXTRA: resource?/resourceName] [Payload]
 *            (with separators)
 * @author Turcato
 */
public abstract class DistributedNetworkAction<T> {

    /**
     * @return true if the defined action is valid
     * (Also checks the length of the composed message)
     */
    abstract public boolean isValid();

    /**
     * @return an SMSMessage, containing the formatted action command, ready to be sent
     */
    abstract public SMSMessage generateMessage();

    /**
     * @return the identifier of the specific action command
     */
    abstract public T getActionID();

    /**
     * @return the numeric code that identifies the action
     */
    abstract public int getAction();

    /**
     * Given that One could send N messages regarding the same Action (ID)
     * @return the progressive number that indicates which message this is of the totality expected
     */
    abstract public int getProgress();

    /**
     * @return the number of Total messages expected for this Action (ID)
     */
    abstract public int getTotalExpectedMessages();

    /**
     * @return the first param of the command message
     */
    abstract public T getParam();

    /**
     * @return the extra param of the command message
     */
    abstract public T getExtra();

    /**
     * @return if present, the payload of the action message
     */

    abstract public T getPayload();

    /**
     * @param smsPeer the Node to which forward the action command
     */
    abstract public void setDestination(@NonNull SMSPeer smsPeer);

    /**
     * @return the destination Node of the action, if available
     */
    abstract public SMSPeer getDestination();

    /**
     * @return If available, the node that forwarded this action
     */
    abstract public SMSPeer getSender();

}
