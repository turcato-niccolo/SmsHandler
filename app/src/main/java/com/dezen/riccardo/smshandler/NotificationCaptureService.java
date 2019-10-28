package com.dezen.riccardo.smshandler;

import android.app.Notification;
import android.provider.Settings;
import android.provider.Telephony;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;
import android.os.Build;

public class NotificationCaptureService extends NotificationListenerService {
    public static final String SMS_DEFAULT_APPLICATION = "sms_default_application";
    public static final String APP_KEY = "<#>";


    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        // you can get notification info here

        String defaultSmsApplication;
        String notificationText = "";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            defaultSmsApplication = Telephony.Sms.getDefaultSmsPackage(getApplicationContext());
        else
            defaultSmsApplication = "vnd.android-dir/mms-sms"; //meglio testarlo

        Toast.makeText(getApplicationContext(), notification.tickerText + " " + defaultSmsApplication + " " + sbn.getPackageName(), Toast.LENGTH_LONG).show();

        if(notification.tickerText != null)
            notificationText = notification.tickerText.toString();

        //Structure of SMS Notification:
        // "ContactName: <#>text"


        if(sbn.getPackageName().equals(defaultSmsApplication) && !notificationText.equals("") && notificationText.contains(APP_KEY))
            cancelNotification(sbn.getKey()); //blocks notifications

    }
}