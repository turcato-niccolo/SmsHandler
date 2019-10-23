package com.example.smstest;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    //1: in the test I have the permission to send SMS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the button to send is clicked.
     *
     * @param view The home view
     *
     */
    public void smsSendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.sms_phone);
        String destinationAddress = editText.getText().toString();
        EditText smsEditText = (EditText) findViewById(R.id.sms_message);
        String smsMessage = smsEditText.getText().toString();
        //service center address
        String scAddress = null;
        // Set pending intents to broadcast when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // permessi -- checkForSmsPermission();
        //SmsManager
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationAddress, scAddress, smsMessage, sentIntent, deliveryIntent);

        //Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
    }




}
