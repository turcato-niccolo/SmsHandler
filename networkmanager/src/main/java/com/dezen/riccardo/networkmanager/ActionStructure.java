package com.dezen.riccardo.networkmanager;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.Peer;
import com.dezen.riccardo.smshandler.SMSMessage;

/**
 * @author Niccolo' Turcato
 *
 * Structure of an Action that uses numerical code identification and generic types Parameter and extra Parameter
 */

abstract class ActionStructure<T extends Comparable<T>> {

    abstract public boolean isValid();

    abstract public SMSMessage generateMessage();

    abstract public int getAction();

    abstract public T getParam();

    abstract public T getExtra();

    abstract public void setDestinationPeer(@NonNull Peer<T> peer);

}