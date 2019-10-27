package com.dezen.riccardo.smshandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.room.Room;

import com.dezen.riccardo.smshandler.database.SmsDatabase;
import com.dezen.riccardo.smshandler.database.SmsEntity;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            if(messages != null && messages.length > 0){
                Log.e("SmsReceiver", "U got mail");
                //write new sms to local database asynchronously
                SmsDatabase db = Room.databaseBuilder(context, SmsDatabase.class, context.getString(R.string.sms_local_database))
                        .enableMultiInstanceInvalidation()
                        .build();
                new WriteToDbTask(messages,db).execute();
                //broadcast local intent to wake the local receiver if the app is running
                Intent local_intent = new Intent();
                local_intent.replaceExtras(intent);
                local_intent.setAction(context.getString(R.string.sms_handler_received_broadcast));
                local_intent.setPackage(context.getApplicationContext().getPackageName());
                context.sendBroadcast(local_intent);
            }
        }
    }

    private static class WriteToDbTask extends AsyncTask<String,Integer,Void>{
        private SmsMessage[] smsMessages;
        private SmsDatabase db;

        public WriteToDbTask(SmsMessage[] smsMessages, SmsDatabase db) {
            this.smsMessages = smsMessages;
            this.db = db;
        }

        @Override
        protected Void doInBackground(String... strings) {
            for(SmsMessage sms : smsMessages){
                SmsEntity s = new SmsEntity(db.access().getCount(),
                        sms.getOriginatingAddress(),
                        sms.getDisplayMessageBody());
                db.access().insert(s);
            }
            return null;
        }
    }
}
