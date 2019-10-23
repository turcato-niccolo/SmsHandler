package com.dezen.riccardo.smshandler;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import androidx.annotation.NonNull;

public class SmsHandler {

    private SmsManager smsManager;
    private String scAddress;
    private PendingIntent sentIntent;
    private PendingIntent deliveryIntent;
    private SMSReceiver smsReceiver;
    private OnSmsReceivedListener listener;

    /**
     * Default constructor. SmsManager.getDefault() can behave unpredictably if called from a
     * background thread in multi-SIM systems.
     */
    public SmsHandler(){
        smsManager = SmsManager.getDefault();
        scAddress = null;
        sentIntent = null;
        deliveryIntent = null;
        listener = null;
        smsReceiver = new SMSReceiver();
    }

    private class SMSReceiver extends BroadcastReceiver{
        /**
         * Default method for BroadcastReceivers. Verifies that there are incoming textmessages and
         * forwards them to a listener, if avaiable.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(intent.getAction() != null && intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION) && bundle != null){
                Object[] pdus = (Object[]) bundle.get("pdus");
                if(pdus != null && pdus.length > 0){
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++)
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    if(listener != null) listener.onReceive(messages);
                }
            }
        }
    }

    /**
     * Interface meant to be implemented by any class wanting to listen for incoming SMS messages.
     */
    public interface OnSmsReceivedListener{
        void onReceive(SmsMessage[] messages);
    }

    /**
     * Method that sends a text message through SmsManager
     * @param destination the destination address for the message, in phone number format
     * @param message the body of the message to be sent
     * @return true if the destination address was valid, and therefore a sending attempt was made, false otherwise
     */
    public boolean sendSMS(String destination, @NonNull String message){
        if(message.isEmpty()) return false;
        if(PhoneNumberUtils.isGlobalPhoneNumber(destination) && PhoneNumberUtils.isWellFormedSmsAddress(destination)){
            smsManager.sendTextMessage(destination,scAddress,message,sentIntent,deliveryIntent);
            return true;
        }
        return false;
    }

    public void setScAddress(String scAddress) {
        this.scAddress = scAddress;
    }

    public void setSentIntent(PendingIntent sentIntent) {
        this.sentIntent = sentIntent;
    }

    public void setDeliveryIntent(PendingIntent deliveryIntent) {
        this.deliveryIntent = deliveryIntent;
    }

    /**
     * Method that registers an instance of SmsReceiver
     * @param context the Context which wishes to register the receiver
     *                multiple calls should not be made before unregistering
     */
    public void registerReceiver(Context context){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        context.registerReceiver(smsReceiver,filter);
    }

    /**
     * Method that unregisters the instance of SmsReceiver
     * @param context which wishes to unregister the receiver
     */
    public void unregisterReceiver(Context context){
        context.unregisterReceiver(smsReceiver);
    }

    public void setListener(OnSmsReceivedListener listener){
        this.listener = listener;
    }

    public void clearListener(){ listener = null;}
}
