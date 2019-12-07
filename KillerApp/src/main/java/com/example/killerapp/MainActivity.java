package com.example.killerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.dezen.riccardo.smshandler.ReceivedMessageListener;


import android.Manifest;
import android.widget.EditText;

import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

/***
 * @author Turcato, Kumar, Habib
 */

public class MainActivity extends AppCompatActivity implements ReceivedMessageListener<SMSMessage> {

    private static final String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
    };
    private final int APP_PERMISSION_REQUEST_CODE = 0;


    private EditText txtPhoneNumber;
    private Button sendButton;
    private Button sendAlarmRequestButton;
    private Button sendLocationRequestButton;

    private Manager manager;
    private SMSPeer smsPeer;

    /***
     * @author Turcato
     * @param savedInstanceState system parameter
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtPhoneNumber =findViewById(R.id.phoneNumber);
        sendButton=findViewById(R.id.sendButton);
        sendAlarmRequestButton = findViewById(R.id.sendAlarmRequestButton);
        sendLocationRequestButton = findViewById(R.id.sendLocationRequestButton);

        manager=new Manager(getApplicationContext());
        manager.setReceiveListener(this);
        requestPermissions();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsPeer=new SMSPeer(txtPhoneNumber.getText().toString());
                manager.SendAlarmAndLocationRequest(smsPeer);
            }
        });

        sendLocationRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsPeer=new SMSPeer(txtPhoneNumber.getText().toString());
                manager.SendLocationRequest(smsPeer);
            }
        });

        sendAlarmRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsPeer=new SMSPeer(txtPhoneNumber.getText().toString());
                manager.SendAlarmRequest(smsPeer);
            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();

    }

    /***
     * @author Turcato
     * Requests Android permissions if not granted
     */
    public void requestPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)+
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)+
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)+
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)+
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)+
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)+
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)+
                ContextCompat.checkSelfPermission(this, Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)
                != PackageManager.PERMISSION_GRANTED)

            ActivityCompat.requestPermissions(this, permissions, APP_PERMISSION_REQUEST_CODE);
    }

    /***
     * @author Turcato
     * This method is executed both when the app is running or not.
     * Based on the message's content, opens AlarmAndLocateResponseActivity if it's a request message,
     * otherwise if it contains the location response (the only one expected) it opens the default maps application
     * to the received location
     *
     * @param message Received SMSMessage class of SmsHandler library
     */
    public  void onMessageReceived(SMSMessage message)
    {
        manager.getRequest(message);

    }

    /**
     * @author Turcato
     * Safely deletes the listeners
     */
    @Override
    protected void onDestroy() {
        manager.removeReceiveListener();
        super.onDestroy();
    }



}

