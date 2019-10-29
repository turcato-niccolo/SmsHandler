package com.dezen.riccardo.smshandler;

import android.app.Notification;
import android.os.Build;
import android.provider.Telephony;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

public class NotificationCatcherService extends NotificationListenerService {

    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        String defaultSmsApplication;
        String notificationText = "";
        defaultSmsApplication = Telephony.Sms.getDefaultSmsPackage(getApplicationContext());
        if(notification.tickerText != null)
            notificationText = notification.tickerText.toString();

        //Structure of SMS Notification:
        // "ContactName: <#>text"
        if(sbn.getPackageName().equals(defaultSmsApplication) && !notificationText.equals("") && notificationText.contains(SmsHandler.APP_KEY))
            cancelNotification(sbn.getKey()); //blocks notifications
    }
}
