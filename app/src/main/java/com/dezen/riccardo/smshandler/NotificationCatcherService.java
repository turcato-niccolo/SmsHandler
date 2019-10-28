package com.dezen.riccardo.smshandler;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationCatcherService extends NotificationListenerService {
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        //manca controllare che notifica è, vedi Parcel, potrebbe essere la strada
        cancelNotification(sbn.getKey());
    }
}
