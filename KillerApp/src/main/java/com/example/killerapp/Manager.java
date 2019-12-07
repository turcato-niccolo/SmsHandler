package com.example.killerapp;

import android.content.Context;

import com.dezen.riccardo.smshandler.ReceivedMessageListener;
import com.dezen.riccardo.smshandler.SMSManager;
import com.dezen.riccardo.smshandler.SMSMessage;

/***
 * @author Pardeep
 * Class
 */

public class Manager {
    private static Manager instance;
    private static Context currentContext;
    AlarmManager alarmManager = new AlarmManager();
    LocationManager locationManager=new LocationManager();
    SMSManager smsManager;

    final String[] audioAlarmMessages = {"AUDIO_ALARM_REQUEST", "AUDIO_ALARM_RESPONSE"};
    String[] locationMessages = {"LOCATION_REQUEST", "LOCATION_RESPONSE"};
    final int request = 0, response = 1;

    private Manager(){
        //prevent use of reflection to change constructor to public during runtime
        if (instance != null)
            throw new RuntimeException("This class uses the singleton design pattern. Use getInstance() to get a reference to the single instance of this class");
        smsManager=SMSManager.getInstance(Manager.currentContext);
    }

    /**
     * Method to get the only valid instance of this class. A new instance is created only if it was
     * null previously. The used context is always the parent application context of the parameter.
     *
     * @param context The calling context.
     * @return the Manager instance.
     */
    public static Manager getInstance(Context context){
        if(instance == null){
            Manager.currentContext = context.getApplicationContext();
            instance = new Manager();
        }
        return instance;
    }

     // Methods from SMSManager class

    /**
     * Method to send an SMSMessage classified as urgent (containing the corresponding code)
     *
     * @param message the valid SMSMessage to send
     * @return true if the message is valid and it has been sent, false otherwise
     */
    public boolean sendSms(SMSMessage message) {
        boolean checkMessageSent = smsManager.sendMessage(message);
        return checkMessageSent;
    }

    /**
     * Method to send an SMSMessage classified as urgent (containing the corresponding code)
     *
     * @param message the valid SMSMessage to send
     * @return true if the message is valid and it has been sent, false otherwise
     */
    public boolean sendUrgentMessage(SMSMessage message) {
        boolean checkUrgentMessageSent = smsManager.sendUrgentMessage(message);
        return checkUrgentMessageSent;
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

    /**
     * Method to save String name for the Activity that should wake up on urgent messages.
     *
     * @param activityClass the Activity Class that should wake up.
     * @return true if the value was set, false otherwise.
     * @throws IllegalArgumentException if the parameter class does not extend Activity
     */
    public boolean setActivityToWake(Class activityClass) throws IllegalArgumentException {
        boolean valueSet = smsManager.setActivityToWake(activityClass);
        return valueSet;
    }

     //Methods from alarm manager class
    /**
     *
     * @return returns a formatted String containing the Alarm Request command
     */
    public String getAlarmStringMessage() { return audioAlarmMessages[request]; }

    /**
     *
     * @param alarmStringRequest the text message received
     * @return true if the received text contains the (formatted) alarm Request
     */
    public boolean containsAlarmRequest(String alarmStringRequest)
    {
        boolean contains=alarmManager.containsAlarmRequest(alarmStringRequest);
        return contains;
    }

    /**
     * Starts an alarm with the default ringtone of the device, stops when activity is closed by the user
     */
    public void startAlarm(Context context)
    {
        alarmManager.startAlarm(context);
    }

     // Methods from LocationManager class

    /**
     *
     * @return returns a formatted String containing the location Request command
     */
    public String getLocationStringMessage() { return locationMessages[request]; }

    /**
     *
     * @param locationStringResponse string containing the received txt message
     * @return true if the received message contains a location response message
     */
    public boolean containsLocationResponse(String locationStringResponse)
    {
        boolean contains=locationManager.containsLocationResponse(locationStringResponse);
        return contains;
    }

    /***
     * @author Turcato
     * Extract the string contained between the latitude tags (if present)
     * Returns empty string if it doesn't find the tags
     *
     * @param receivedMessage string containing the text received sy sms
     * @return if present, the string contained between the latitude tags, empty string if it doesn't find the tags
     */
    public String getLatitude(String receivedMessage)
    {
        String string=locationManager.getLatitude(receivedMessage);
        return string;
    }

    /***
     * @author Turcato
     * Extract the string contained between the longitude tags (if present)
     * Returns empty string if it doesn't find the tags
     *
     * @param receivedMessage string containing the text received sy sms
     * @return if present, the string contained between the longitude tags, empty string if it doesn't find the tags
     */
    public String getLongitude(String receivedMessage)
    {
        String string=locationManager.getLongitude(receivedMessage);
        return string;
    }

    /***
     * Method that gets the last Location available of the device, and executes the imposed command
     * calling command.execute(foundLocation)
     *
     * @param command object of a class that implements interface Command
     */
    public void getLastLocation(Context applicationContext, final Command command) {
        locationManager.getLastLocation(applicationContext, command);
    }

    /**
     *
     * @param locationStringRequest the text message received
     * @return true if the received text contains the (formatted) location Request
     */
    public boolean containsLocationRequest(String locationStringRequest)
    {
        boolean contains=locationManager.containsLocationRequest(locationStringRequest);
        return contains;
    }

}