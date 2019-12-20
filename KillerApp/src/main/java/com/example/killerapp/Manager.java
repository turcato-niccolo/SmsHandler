package com.example.killerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.dezen.riccardo.smshandler.ReceivedMessageListener;
import com.dezen.riccardo.smshandler.SMSManager;
import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

/***
 * @author Pardeep
 * Class
 */

public class Manager {
    private static Context currentContext;
    AlarmManager alarmManager = new AlarmManager();
    LocationManager locationManager=new LocationManager();
    SMSManager smsManager;
    Constants constants=new Constants();

    private static final String MANAGER_TAG = "Manager";
    private  SendResponseSms sendResponseSms;

    public Manager(Context context){
        currentContext=context.getApplicationContext();
        smsManager=SMSManager.getInstance(Manager.currentContext);

    }

    /**
     * Method to remove receivedListener
     */
    public void removeReceiveListener() {smsManager.removeReceiveListener(); }

    /**
     * Setter for receivedListener in SMSHandler
     *
     * @param newReceivedListener the new listener
     */
    public void setReceiveListener(ReceivedMessageListener<SMSMessage> newReceivedListener) { smsManager.setReceiveListener(newReceivedListener); }

    /***
     * @author Turcato
     *Send an urgent message to the peer with a location request
     *
     * @param smsPeer the peer to which  send sms Location request
     */
    public void SendLocationRequest(SMSPeer smsPeer)
    {
        String requestStringMessage = LocationManager.locationMessages[LocationManager.request];
        SMSMessage smsMessage = new SMSMessage(smsPeer, requestStringMessage);
        smsManager.sendUrgentMessage(smsMessage);
    }

    /***
     * @author Turcato
     * Send an urgent message to the peer for an Alarm request
     *
     * @param smsPeer the peer to which  send sms Location & alarm request
     */
    public void SendAlarmRequest(SMSPeer smsPeer)
    {
        String requestStringMessage = AlarmManager.audioAlarmMessages[LocationManager.request];
        SMSMessage smsMessage = new SMSMessage(smsPeer, requestStringMessage);
        smsManager.sendUrgentMessage(smsMessage);
    }

    /***
     * @author Turcato
     *Send an urgent message to the peer for an Alarm and Location request
     *
     * @param smsPeer the peer to which send sms alarm request
     */
    public void SendAlarmAndLocationRequest(SMSPeer smsPeer)
    {
        //Wake key to indicate urgency to the device
        String requestStringMessage = AlarmManager.audioAlarmMessages[LocationManager.request]
                +LocationManager.locationMessages[LocationManager.request];
        SMSMessage smsMessage = new SMSMessage(smsPeer, requestStringMessage);
        smsManager.sendUrgentMessage(smsMessage);
    }

    /***
     * This method check the received string and can active alarm /send location or both based on its content
     *
     * @param requestMessage the request message that decide which action to do
     * @param phoneNumber the number to which send you'r phone's location
     */
    public void getRequest(String requestMessage,String phoneNumber)
    {
        if (locationManager.containsLocationRequest(requestMessage)) {
            //Action to execute when device receives a Location request
            sendResponseSms = new SendResponseSms(phoneNumber, currentContext.getApplicationContext());
            locationManager.getLastLocation(currentContext.getApplicationContext(), sendResponseSms);
        }
        //User has to close app manually to stop the alarm
        if (alarmManager.containsAlarmRequest(requestMessage))
            alarmManager.startAlarm(currentContext.getApplicationContext());
    }

    /***
     *Based on the response this method opens the activityClass or open the default map app
     * @param messageResponse The message received
     */
   public void getResponse(SMSMessage messageResponse,Class activityClass)
    {
        String requestMessage  = messageResponse.getData();
        if (locationManager.containsLocationRequest(requestMessage)
                || alarmManager.containsAlarmRequest(requestMessage)) {
            OpenRequestsActivity(requestMessage, messageResponse.getPeer().getAddress(),activityClass);
        }

        //The only expected response
        if(locationManager.containsLocationResponse(requestMessage)){
            Double longitude;
            Double latitude;
            try {
                longitude = Double.parseDouble(locationManager.getLongitude(requestMessage));
                latitude = Double.parseDouble(locationManager.getLatitude(requestMessage));
                locationManager.OpenMapsUrl(latitude, longitude);
            }
            catch (Exception e){
                //Written in log for future users to report
                Log.e(MANAGER_TAG,AlarmManager.response + ": " + e.getMessage());
            }

        }
    }

    /***
     * @author Turcato
     * Opens an activityClass, forwarding the receivedMessageText and the receivedMessageReturnAddress
     *
     * @param activityClass the activity to be opened
     * @param receivedMessageText the text of the request message
     * @param receivedMessageReturnAddress the return address of the request message
     */
    private void OpenRequestsActivity(String receivedMessageText, String receivedMessageReturnAddress, Class activityClass)
    {
        Log.d(MANAGER_TAG, "OpenRequestsActivity");
        Intent openAlarmAndLocateActivityIntent = new Intent(currentContext.getApplicationContext(), activityClass);
        openAlarmAndLocateActivityIntent.putExtra(constants.receivedStringMessage, receivedMessageText);
        openAlarmAndLocateActivityIntent.putExtra(constants.receivedStringAddress, receivedMessageReturnAddress);
        openAlarmAndLocateActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentContext.getApplicationContext().startActivity(openAlarmAndLocateActivityIntent);
    }


}