package com.dezen.riccardo.smshandler.database;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsMessage;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.dezen.riccardo.smshandler.ReceivedMessageListener;
import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

/**
 * Singleton class used to perform the two needed operations on the database containing unread sms.
 * The reason for singleton design pattern is found here https://developer.android.com/training/data-storage/room
 * @author Riccardo De Zen
 */
public class SMSDatabaseManager {

    private static final String UNREAD_SMS_DB_NAME = "unread-sms-db";

    private static SMSDatabaseManager instance;
    private SMSDatabase database;

    /**
     * Constructor, handles instantiation of the database.
     */
    private SMSDatabaseManager(Context context){
        //prevent use of reflection to change constructor to public at runtime
        if (instance != null)
            throw new RuntimeException("This class uses the singleton design pattern. Use getInstance() to get a reference to the single instance of this class");
        database = Room.databaseBuilder(context, SMSDatabase.class, UNREAD_SMS_DB_NAME)
                .enableMultiInstanceInvalidation()
                .build();
    }

    /**
     * Method to get the only valid instance of this class. A new instance is created only if it was
     * null previously. The used context is always the parent application context of the parameter.
     * @param context The calling context.
     * @return the SMSDatabaseManager instance.
     */
    public static SMSDatabaseManager getInstance(Context context){
        if(instance == null){
            instance = new SMSDatabaseManager(context);
        }
        return instance;
    }

    /**
     * Method to add SMS to the database. Starts an asynchronous task to do so.
     * @param newMessages the message/messages to be added
     */
    public void addSMS(SMSMessage... newMessages){
        if(newMessages == null || newMessages.length <= 0) return;
        new AddSMSTask(database).execute(newMessages);
    }
    public void addSMS(SmsMessage... newMessages){
        if(newMessages == null || newMessages.length <= 0) return;
        SMSMessage[] messages = new SMSMessage[newMessages.length];
        for(int i = 0; i < newMessages.length; i++){
            messages[i] = new SMSMessage(newMessages[i]);
        }
        addSMS(messages);
    }

    private static class AddSMSTask extends AsyncTask<SMSMessage,Integer,Boolean>{
        private SMSDatabase database;
        public AddSMSTask (SMSDatabase database){
            this.database = database;
        }
        @Override
        protected Boolean doInBackground(SMSMessage... smsMessages) {
            try{
                SMSDao dbAccess = database.access();
                for(SMSMessage message : smsMessages){
                    SMSEntity entity = new SMSEntity(
                            dbAccess.getCount(),
                            message.getPeer().getAddress(),
                            message.getData()
                    );
                    dbAccess.insert(entity);
                }
                return Boolean.TRUE;
            }
            catch(Exception e){
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
    }

    /**
     * Method to send all unread SMS to the given listener, SMS are deleted once listener returns.
     * Starts an asynchronous task to perform the operations.
     * @param listener the valid listener
     */
    public void forwardAllSMS(@NonNull ReceivedMessageListener<SMSMessage> listener){
        new ForwardSMSTask(database, listener).execute();
    }

    private static class ForwardSMSTask extends AsyncTask<String,Integer,Boolean>{
        private SMSDatabase database;
        private ReceivedMessageListener<SMSMessage> listener;
        public ForwardSMSTask (SMSDatabase database, ReceivedMessageListener<SMSMessage> listener){
            this.database = database;
            this.listener = listener;
        }
        @Override
        protected Boolean doInBackground(String... args) {
            SMSDao dbAccess = database.access();
            try{
                SMSEntity[] unreadMessages = dbAccess.loadAllSms();
                for(SMSEntity entity : unreadMessages){
                    SMSMessage message = new SMSMessage(
                            new SMSPeer(entity.address),
                            entity.body
                    );
                    if(message.isValid()) listener.onMessageReceived(message);
                    dbAccess.deleteSms(entity);
                }
                return Boolean.TRUE;
            }
            catch(Exception e){
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
    }
}
