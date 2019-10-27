package com.dezen.riccardo.smshandler;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private OnSmsEventListener listener;
    private SmsEventReceiver smsEventReceiver;
    /**
     * Default constructor. SmsManager.getDefault() can behave unpredictably if called from a
     * background thread in multi-SIM systems.
     */
    public SmsHandler(){
        smsManager = SmsManager.getDefault();
        scAddress = null;
        listener = null;
        sentIntent = null;
        deliveryIntent = null;
        smsEventReceiver = new SmsEventReceiver();
    }

    private class SmsEventReceiver extends BroadcastReceiver{
        /**
         * Default method for BroadcastReceivers. Verifies that there are incoming, sent or delivered text messages and
         * forwards them to a listener, if avaiable.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() != null){
                if(intent.getAction().equals(context.getString(R.string.sms_handler_received_broadcast))) {
                    if (listener != null)
                        listener.onReceive(Telephony.Sms.Intents.getMessagesFromIntent(intent));
                }
                if(intent.getAction().equals(context.getString(R.string.sms_handler_sent_broadcast))){
                    if(listener != null) listener.onSent(getResultCode());
                }
                if(intent.getAction().equals(context.getString(R.string.sms_handler_delivered_broadcast))){
                    if(listener != null) listener.onDelivered(getResultCode());
                }
            }
        }
    }

    /**
     * Interface meant to be implemented by any class wanting to listen for incoming SMS messages.
     */
    public interface OnSmsEventListener {
        void onReceive(SmsMessage[] messages);
        void onSent(int resultCode);
        void onDelivered(int resultCode);
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
     * @param received whether the receiver should listen for incoming sms
     * @param sent whether the receiver should listen for sent sms
     * @param delivered whether the receiver should listen for delivered sms
     */
    public void registerReceiver(Context context, boolean received, boolean sent, boolean delivered){
        if(received || sent || delivered){
            IntentFilter filter = new IntentFilter();
            if(received) filter.addAction(context.getString(R.string.sms_handler_received_broadcast));
            if(sent){
                filter.addAction(context.getString(R.string.sms_handler_sent_broadcast));
                sentIntent = PendingIntent.getBroadcast(context,0,new Intent(context.getString(R.string.sms_handler_sent_broadcast)),0);
            }
            if(delivered){
                filter.addAction(context.getString(R.string.sms_handler_delivered_broadcast));
                deliveryIntent = PendingIntent.getBroadcast(context,0,new Intent(context.getString(R.string.sms_handler_delivered_broadcast)),0);
            }
            context.registerReceiver(smsEventReceiver,filter);
        }
    }

    /**
     * Method that unregisters the instance of SmsReceiver
     * @param context which wishes to unregister the receiver
     */
    public void unregisterReceiver(Context context){
        context.unregisterReceiver(smsEventReceiver);
        sentIntent = null;
        deliveryIntent = null;
    }

    public void setListener(OnSmsEventListener listener){
        this.listener = listener;
    }

    public void clearListener(){ listener = null;}
}
