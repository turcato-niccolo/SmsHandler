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

    private static final String MAIN_ACTIVITY_TAG = "MainActivity";
    private Constants constants;

    private EditText txtPhoneNumber;
    private Button sendButton;
    private Button sendAlarmRequestButton;
    private Button sendLocationRequestButton;

    private Manager manager;
    private boolean MESSAGE_IS_URGENT = true;
    private final String MAPS_START_URL = "https://www.google.com/maps/search/?api=1&query=";
    //NOTE: concat latitude,longitude


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

        manager=Manager.getInstance(getApplicationContext());
        manager.setReceiveListener(this);
        constants = new Constants();
        requestPermissions();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAlarmAndLocationRequest(txtPhoneNumber.getText().toString());
            }
        });

        sendLocationRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendLocationRequest(txtPhoneNumber.getText().toString());
            }
        });

        sendAlarmRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAlarmRequest(txtPhoneNumber.getText().toString());
            }
        });
    }

    /***
     * @author Turcato
     *Send an urgent message to the @phoneNumber with a location request
     *
     * @param phoneNumber phone number to which send sms Location request
     */
    private void SendLocationRequest(String phoneNumber)
    {
        String requestStringMessage = manager.getLocationStringMessage();
        SMSMessage smsMessage = new SMSMessage(new SMSPeer(phoneNumber), requestStringMessage);
        manager.sendUrgentMessage(smsMessage);
    }

    /***
     * @author Turcato
     * Send an urgent message to the @phoneNumber for an Alarm request
     *
     * @param phoneNumber phone number to which send sms Location & alarm request
     */
    private void SendAlarmRequest(String phoneNumber)
    {
        String requestStringMessage = manager.getAlarmStringMessage();
        SMSMessage smsMessage = new SMSMessage(new SMSPeer(phoneNumber), requestStringMessage);
        manager.sendUrgentMessage(smsMessage);
    }

    /***
     * @author Turcato
     *Send an urgent message to the @phoneNumber for an Alarm and Location request
     *
     * @param phoneNumber phone number to which send sms alarm request
     */
    private void SendAlarmAndLocationRequest(String phoneNumber)
    {
        //Wake key to indicate urgency to the device
        String requestStringMessage = manager.getAlarmStringMessage()
                +manager.getLocationStringMessage();
        SMSMessage smsMessage = new SMSMessage(new SMSPeer(phoneNumber), requestStringMessage);
        manager.sendUrgentMessage(smsMessage);
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
        String receivedStringMessage = message.getData();

        //Both Requests are handled by the other activity
        if (manager.containsLocationRequest(receivedStringMessage)
                || manager.containsAlarmRequest(receivedStringMessage)) {
            OpenRequestsActivity(receivedStringMessage, message.getPeer().getAddress());
        }

        //The only expected response
        if(manager.containsLocationResponse(receivedStringMessage)){
            Double longitude;
            Double latitude;
            try {
                longitude = Double.parseDouble(manager.getLongitude(receivedStringMessage));
                latitude = Double.parseDouble(manager.getLatitude(receivedStringMessage));
                OpenMapsUrl(latitude, longitude);
            }
            catch (Exception e){
                //Written in log for future users to report
                Log.e(MAIN_ACTIVITY_TAG,manager.response + ": " + e.getMessage());
            }

        }
    }

    /***
     * @author Turcato
     * Opens the AlarmAndLocateResponseActivity, forwarding the receivedMessageText and the receivedMessageReturnAddress
     * The opened activity's task is to respond to the given requests, that can't be handled on this
     * activity because the app might be closed, so the response activity has to be forcedly opened.
     *
     * When app is closed the messages are received by KillerAppClosedReceiver,
     * secondary BroadcastReceiver that responds to the forced WAKE and has the same job as this method
     *
     * @param receivedMessageText the text of the request message
     * @param receivedMessageReturnAddress the return address of the request message
     */
    private void OpenRequestsActivity(String receivedMessageText, String receivedMessageReturnAddress)
    {
        Log.d(MAIN_ACTIVITY_TAG, "OpenRequestsActivity");
        Intent openAlarmAndLocateActivityIntent = new Intent(getApplicationContext(), AlarmAndLocateResponseActivity.class);
        openAlarmAndLocateActivityIntent.putExtra(constants.receivedStringMessage, receivedMessageText);
        openAlarmAndLocateActivityIntent.putExtra(constants.receivedStringAddress, receivedMessageReturnAddress);
        openAlarmAndLocateActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(openAlarmAndLocateActivityIntent);
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

    /***
     * @author Turcato
     * Opens the default maps application at the given Location(latitude, longitude)
     *
     * @param mapsLatitude latitude extracted by response sms
     * @param mapsLongitude longitude extracted by response sms
     */
    public void OpenMapsUrl(Double mapsLatitude, Double mapsLongitude)
    {
        String url = MAPS_START_URL + mapsLatitude + "," + mapsLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}

