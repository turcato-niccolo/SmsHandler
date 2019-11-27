package com.dezen.riccardo.smshandler;

import android.telephony.SmsMessage;

import androidx.annotation.NonNull;


/**
 * Class implementing Message to represent an SMS-type message.
 * @author Riccardo De Zen based on decisions of whole class.
 */
public class SMSMessage extends Message<String, SMSPeer>{

    public static final int MAX_MESSAGE_LENGTH = 160;

    private String data;
    private SMSPeer peer;

    /**
     * @param peer the Peer associated with this Message
     * @param data the data to be contained in the message
     */
    public SMSMessage(SMSPeer peer, String data){
        this.peer = peer;
        this.data = data;
    }

    /**
     * Constructor from a valid SmsMessage
     */
    public SMSMessage(SmsMessage message){
        this.peer = new SMSPeer(message.getOriginatingAddress());
        this.data = message.getMessageBody();
    }

    /**
     * @return the data of this message
     */
    @Override
    public String getData() {
        return data;
    }

    /**
     * @return the peer of this message
     */
    @Override
    public SMSPeer getPeer() {
        return peer;
    }

    /**
     * @return true if this message is not empty and has a valid peer
     */
    public boolean isValid(){
        return peer.isValid() && isMessageValid(data) == MessageValidity.MESSAGE_VALID;
    }

    /**
     * @param message the message whose validity should be checked
     * @return An enum value to indicate what is wrong with the message or that nothing is wrong
     */
    public static MessageValidity isMessageValid(String message){
        if(message.length() > MAX_MESSAGE_LENGTH)
            return MessageValidity.MESSAGE_TOO_LONG;

        if(message.isEmpty())
            return MessageValidity.MESSAGE_EMPTY;

        return MessageValidity.MESSAGE_VALID;
    }

    /**
     * @return the String type representation of this object
     */
    @NonNull
    public String toString(){
        return "Peer: "+peer.toString()+"\nData: "+data;
    }

    /**
     * Enum with values aimed to describe message validity
     */
    public enum MessageValidity{
        MESSAGE_TOO_LONG,
        MESSAGE_EMPTY,
        MESSAGE_VALID
    }
}
