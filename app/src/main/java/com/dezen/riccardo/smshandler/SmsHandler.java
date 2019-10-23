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

public class SmsHandler {

    private SmsManager smsManager;
    private String scAddress;
    private PendingIntent sentIntent;
    private PendingIntent deliveryIntent;
    private SMSReceiver smsReceiver;
    private OnSmsReceivedListener listener;

    public SmsHandler(){
        smsManager = SmsManager.getDefault();
        scAddress = null;
        sentIntent = null;
        deliveryIntent = null;
        listener = null;
        smsReceiver = new SMSReceiver();
    }

    private class SMSReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(intent.getAction() != null && intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION) && bundle != null){
                Object[] pdus = (Object[]) bundle.get("pdus");
                if(pdus != null && pdus.length > 0){
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    if(listener != null) listener.onReceive(messages);
                }
            }
        }
    }

    public interface OnSmsReceivedListener{
        void onReceive(SmsMessage[] messages);
    }

    public void sendSMS(String destination, String message){
        if(PhoneNumberUtils.isGlobalPhoneNumber(destination) && PhoneNumberUtils.isWellFormedSmsAddress(destination))
            smsManager.sendTextMessage(destination,scAddress,message,sentIntent,deliveryIntent);
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

    public void registerReceiver(Context context){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        context.registerReceiver(smsReceiver,filter);
    }

    public void unregisterReceiver(Context context){
        context.unregisterReceiver(smsReceiver);
    }

    public void setListener(OnSmsReceivedListener listener){
        this.listener = listener;
    }

    public void clearListener(){ listener = null;}
}
