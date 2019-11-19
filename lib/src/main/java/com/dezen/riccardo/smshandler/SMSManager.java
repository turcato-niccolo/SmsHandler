package com.dezen.riccardo.smshandler;

import android.content.Context;

/**
 * Class to implement CommunicationHandler through use of SMS messages.
 * @author Riccardo De Zen based on decisions of whole class. Model proposed by Marco Cognolato and Luca Crema.
 */
public class SMSManager extends CommunicationHandler<SMSMessage>{
    private static SMSManager instance;

    private static Context currentContext;

    private SMSHandler smsHandler;

    private SMSManager(){
        //prevent use of reflection to change constructor to public at runtime
        if (instance != null)
            throw new RuntimeException("This class uses the singleton building pattern. Use getInstance() to get a reference to the single instance of this class");
        smsHandler = new SMSHandler(currentContext);
    }

    public static SMSManager getInstance(Context context){
        if(instance == null){
            SMSManager.currentContext = context;
            instance = new SMSManager();
        }
        return instance;
    }

    /**
     * Method that should be called when currentContext stops being valid or instance stops being used to
     * avoid a memory leak.
     * @param context the owning currentContext dropping the ownership.
     */
    public static void onContextDestroyed(Context context){
        if(context.equals(SMSManager.currentContext)){
            instance.removeReceiveListener();
            instance.smsHandler.onContextDestroyed();
            SMSManager.currentContext = null;
        }
    }

    /**
     * method to send an SMSMessage to its associated Peer
     * @param message the valid SMSMessage to send
     * @return true if the message is valid and it has been sent, false otherwise
     */
    @Override
    public boolean sendMessage(SMSMessage message) {
        if(message.isValid()) {
            smsHandler.sendSMS(message.getPeer().getAddress(), message.getData(), false);
            return true;
        }
        return false;
    }

    public boolean sendUrgentMessage(SMSMessage message) {
        if(message.isValid()) {
            smsHandler.sendSMS(message.getPeer().getAddress(), message.getData(), true);
            return true;
        }
        return false;
    }

    @Override
    public void setReceiveListener(ReceivedMessageListener<SMSMessage> listener) {
        removeReceiveListener();
        smsHandler.setReceivedListener(listener);
    }

    @Override
    public void removeReceiveListener() {
        smsHandler.clearReceivedListener();
    }

    /**
     * Setter for sentListener
     * @param newSentListener the new listener
     */
    public void setSentListener(SentMessageListener<SMSMessage> newSentListener) {
        removeSentListener();
        smsHandler.setSentListener(newSentListener);
    }

    /**
     * Method to remove sentListener
     */
    public void removeSentListener(){
        smsHandler.clearSentListener();
    }

    /**
     * Setter for deliveredListener
     * @param newDeliveredListener the new listener
     */
    public void setDeliveredListener(DeliveredMessageListener<SMSMessage> newDeliveredListener) {
        removeDeliveredListener();
        smsHandler.setDeliveredListener(newDeliveredListener);
    }

    /**
     * Method to remove deliveredListener
     */
    public void removeDeliveredListener(){
        smsHandler.clearDeliveredListener();
    }
}
