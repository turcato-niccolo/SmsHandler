package com.dezen.riccardo.smshandler;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SmsUtils {
    private static String[] inboxProjection = {
            Telephony.Sms._ID,
            Telephony.Sms.ADDRESS,
            Telephony.Sms.SUBJECT,
            Telephony.Sms.BODY
    };
    public static List<String> getInbox(Context context){
        List<String> list = new ArrayList<>();
        String[] selectionArgs = null;
        String selectionClause = null;
        Cursor mCursor = context.getContentResolver().query(
                Telephony.Sms.CONTENT_URI,
                inboxProjection, selectionClause, selectionArgs,
                Telephony.Sms._ID);
        if(mCursor.getCount() >= 1){
            mCursor.moveToFirst();
            while(!mCursor.isLast()){
                StringBuilder sb = new StringBuilder()
                        .append(mCursor.getString(0)+" ")
                        .append(mCursor.getString(1)+" ")
                        .append(mCursor.getString(2)+" ")
                        .append(mCursor.getString(3));
                list.add(sb.toString());
                Log.d("SMSUtils", sb.toString());
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return list;
    }
}
