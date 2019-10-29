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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.dezen.riccardo.smshandler.database.SmsDatabase;
import com.dezen.riccardo.smshandler.database.SmsEntity;

import java.util.ArrayList;
import java.util.List;

public class SmsHandler {

    /**
     * Contains references to all listeners belonging to instances of this class
     * which registered a receiver that listens for at least incoming sms.*/
    private static List<OnSmsEventListener> activeIncomingListeners = new ArrayList<>();

    private SmsManager smsManager;
    private String scAddress;
    private PendingIntent sentIntent;
    private PendingIntent deliveryIntent;

    //This instance's attached listener.
    private OnSmsEventListener listener;
    //This instance's not necessarily registered BroadcastReceiver.
    private SmsEventReceiver smsEventReceiver;
    //Whether the receiver for this instance is listening for incoming sms.
    private boolean listeningForIncoming;
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
        listeningForIncoming = false;
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
                    if (listener != null){
                        for(SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                            listener.onReceive(message.getOriginatingAddress(), message.getMessageBody());
                        }
                    }
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
        /**
         * Method called when an sms is received
         * @param from the originating address.
         * @param message the body of the message.
         */
        void onReceive(String from, String message);
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

    /**
     * Method to quickly register for received sms only.
     * @param context the Context that wishes to register the receiver.
     */
    public void registerReceiver(Context context){
        registerReceiver(context, true, false, false);
    }

    /**
     * Method that registers an instance of SmsReceiver
     * @param context the Context which wishes to register the receiver
     *                multiple calls should not be made before unregistering
     * The receiver must listen for received sms - this is a temporary solution for proper handling of shouldHandleIncomingSms
     * @param sent whether the receiver should listen for sent sms
     * @param delivered whether the receiver should listen for delivered sms
     * @throws IllegalStateException if trying to register the receiver with no action to be received
     */
    public void registerReceiver(Context context, boolean received, boolean sent, boolean delivered) throws IllegalStateException{
        /**The boolean values allow to enable the three actions the receiver might want to listen to.
         * This is done to reduce the number of BroadcastReceivers the process registers for every
         * instance of the class in order to reduce waste of system resources.
         */
        if(!received && !sent && !delivered) throw new IllegalStateException("Shouldn't register a receiver with no action.");
        IntentFilter filter = new IntentFilter();
        if(received){
            filter.addAction(context.getString(R.string.sms_handler_received_broadcast));
            listeningForIncoming = true;
            if(listener != null) activeIncomingListeners.add(listener);
        }
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

    /**
     * Method that unregisters the instance of SmsReceiver
     * @param context which wishes to unregister the receiver
     */
    public void unregisterReceiver(Context context){
        context.unregisterReceiver(smsEventReceiver);
        listeningForIncoming = false;
        if(listener != null) activeIncomingListeners.remove(listener);
        sentIntent = null;
        deliveryIntent = null;
    }

    /**
     * Method to set the listener for this instance. Listener needs to be cleared before a new one is set.
     * @param listener non-null new listener.
     * @throws IllegalStateException if a listener is already attached to this instance of SmsHandler.
     */
    public void setListener(@NonNull OnSmsEventListener listener) throws IllegalStateException{
        if(this.listener != null) throw new IllegalStateException("A listener is already attached to this instance.");
        this.listener = listener;
        if(listeningForIncoming) activeIncomingListeners.add(listener);
    }

    /**
     * Method to clear this instance's attached listener. Albeit not necessary, a listener should
     * only try to unregister itself.
     */
    public void clearListener(){
        if(listener == null) return;
        activeIncomingListeners.remove(listener);
        listener = null;
    }

    /**
     * Method to communicate whether at least one listener is attached to an instance of this class
     * whose BroadcastReceiver is listening for incoming Sms, and is thus requiring to be notified.
     * @return true if the activeIncomingListeners list is not empty.
     */
    public static boolean shouldHandleIncomingSms(){ return !activeIncomingListeners.isEmpty();}

    /**
     * Method to clear and forward the unread messages from the database to the listener. Due to database access restrictions
     * this method cannot be thrown from the main thread. If no listener is present, this method simply clears
     * the database returning the cleared values.
     * @param context the calling context, used to instantiate the database.
     * @return an array containing the SmsEntity object containing the unread sms data.
     * @throws IllegalStateException if it's run from the main Thread.
     */
    public SmsEntity[] fetchUnreadMessages(Context context){
        SmsDatabase db = Room.databaseBuilder(context, SmsDatabase.class, context.getString(R.string.sms_local_database))
                .enableMultiInstanceInvalidation()
                .build();
        SmsEntity[] messages = db.access().loadAllSms();
        for(SmsEntity sms : messages){
            db.access().deleteSms(sms);
            if(listener != null) listener.onReceive(sms.address, sms.body);
            Log.e("Unread Message", sms.address+" "+sms.body);
        }
        return messages;
    }
}
