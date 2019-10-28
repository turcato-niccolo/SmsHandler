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

    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        // you can get notification info here

        Toast.makeText(getApplicationContext(), sbn.getPackageName(), Toast.LENGTH_LONG).show();

        Toast.makeText(getApplicationContext(), "Categoria: " +notification.CATEGORY_MESSAGE, Toast.LENGTH_LONG).show();
        String defaultSmsApplication = "";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            defaultSmsApplication = Telephony.Sms.getDefaultSmsPackage(getApplicationContext());
        else
            defaultSmsApplication = "vnd.android-dir/mms-sms";

        Toast.makeText(getApplicationContext(), "Def: " +defaultSmsApplication + " " + sbn.getPackageName(), Toast.LENGTH_LONG).show();

        if(defaultSmsApplication == sbn.getPackageName())
            cancelNotification(sbn.getKey());
    }
}