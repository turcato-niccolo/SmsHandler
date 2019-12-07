package com.example.killerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AlarmAndLocateResponseActivity extends AppCompatActivity {
    private final String AlarmAndLocateActivityTAG = "Alarm&LocateActivityTAG";
    private String receivedTextMessage;
    private String receivedMessageAddress;
    private Constants constants;
    private Manager manager;
    private  SendResponseSms sendResponseSms;
    private MediaPlayer mediaPlayer;


    /**
     * This activity is created in all situations, for each request, so it needs to be executed also when screen is shut
     *
     * @param savedInstanceState system parameter
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Needed to open Activity if screen is shut
        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_alarm_and_locate);

        constants = new Constants();


        //Params passed by methods tha called this activity
        receivedTextMessage = getIntent().getStringExtra(constants.receivedStringMessage);
        receivedMessageAddress = getIntent().getStringExtra(constants.receivedStringAddress);
        manager=Manager.getInstance(getApplicationContext());


        if (manager.containsLocationRequest(receivedTextMessage)) {
            //Action to execute when device receives a Location request
            sendResponseSms = new SendResponseSms(receivedMessageAddress, getApplicationContext());
            manager.getLastLocation(getApplicationContext(), sendResponseSms);
        }
        //User has to close app manually to stop
        if (manager.containsAlarmRequest(receivedTextMessage))
            manager.startAlarm(getApplicationContext());


    }

    @Override
    protected void onDestroy() {
        manager.removeReceiveListener();
        if(mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
        super.onDestroy();
    }
}
