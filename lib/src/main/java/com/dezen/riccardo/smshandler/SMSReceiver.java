package com.dezen.riccardo.smshandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.dezen.riccardo.smshandler.database.SMSDatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Class meant to intercept SMSMessages coming from the Android system.
 * The filtering work on messages is voluntarily repeated between this Receiver and
 * NotificationCatcherService in order to avoid attaching the Receiver to the Service and keep it
 * lighter.
 * The class checks whether pertinent messages have been received. Then proceeds to check whether a
 * suitable listener is available for immediate response. If not then proceeds to either fire a
 * broadcast meant to wake some other process or writes the messages to a database for later use.
 * @author Riccardo De Zen
 */
public class SMSReceiver extends BroadcastReceiver {
    private boolean shouldWake = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
            SmsMessage[] messages = filter(Telephony.Sms.Intents.getMessagesFromIntent(intent));
            if(messages.length < 1) return;
            if(SMSHandler.shouldHandleIncomingSms()){
                /*
                 * SMSHandler.shouldHandleIncomingSms() returns true if a suitable listener for
                 * immediate response is available. A broadcast event is therefore fired to
                 * notify said listener through the receiver it is attached to.
                 */
                Intent localIntent = new Intent();
                localIntent.replaceExtras(intent);
                localIntent.setAction(SMSHandler.RECEIVED_BROADCAST);
                localIntent.setPackage(context.getApplicationContext().getPackageName());
                context.sendBroadcast(localIntent);
            }
            else if(shouldWake){
                wakeActivity(context, intent);
            }
            else{
                SMSDatabaseManager.getInstance(context).addSMS(messages);
            }
        }
    }

    /**
     * Method to filter messages containing SMSHandler.APP_KEY.
     * Messages are meant to be the ones coming directly from the received Intent.
     * @param messages array of SmsMessage.
     * @return list of messages containing SMSHandler.APP_KEY
     */
    private SmsMessage[] filter(SmsMessage[] messages){
        List<SmsMessage> list = new ArrayList<>();
        if(messages != null)
            for(SmsMessage sms : messages){
                if(sms.getMessageBody().contains(SMSHandler.APP_KEY)) list.add(sms);
                if(sms.getMessageBody().contains(SMSHandler.WAKE_KEY)) shouldWake = true;
            }
        return list.toArray(new SmsMessage[0]);
    }

    /**
     * Method to start an activity from its canonical name, if such an activity has been specified
     * in the apposite file.
     * @param context the context starting the Activity.
     * @param intentWithExtras the Intent containing any extras to be passed along.
     */
    private void wakeActivity(Context context, Intent intentWithExtras){
        try{
            Class activityClass = getActivityToWake();
            if(activityClass == null) return;
            Intent wakeIntent = new Intent(context, activityClass);
            wakeIntent.replaceExtras(intentWithExtras);
            wakeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(wakeIntent);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Reads from disk the name of the activity that should be started and returns its class;
     * @return The class of the Activity that should be woken up, null if none is present.
     */
    private Class getActivityToWake(){
        return null;
    }
}