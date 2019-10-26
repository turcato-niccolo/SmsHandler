package com.dezen.riccardo.smshandler;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

public class NotificationCaptureService extends NotificationListenerService {
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        // you can get notification info here

        Toast.makeText(getApplicationContext(), sbn.getPackageName(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "Categoria: " +notification.category, Toast.LENGTH_LONG).show();

        // you can cancel the notification in this way
        cancelNotification(sbn.getKey());
    }
}