package com.example.killerapp;

import android.content.Context;
import android.location.Location;

import com.dezen.riccardo.smshandler.SMSManager;
import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

/***
 * @author Turcato
 * Action to execute when receiving a Location request
 * Sends back current position
 */
public class SendResponseSms implements Command<Location> {
    String receivingAddress;
    Constants constants;
    SMSManager manager;
    Context applicationContext;
    LocationManager locationManager;

    /**
     * @author Turcato
     * Sends an sms message to the defined sms number with a text specifically formatted to contain
     * the position in foundlocation
     * @param foundLocation location to forward to given phone number
     */
    public void execute(Location foundLocation) {
        String responseMessage = locationManager.getResponseStringMessage(foundLocation);
        SMSMessage smsMessage = new SMSMessage(new SMSPeer(receivingAddress), responseMessage);
        manager.sendMessage(smsMessage);

    }

    /***
     * @author Turcato3
     * @param receiverAddress receiver's phone number

     * @param context android application Context
     */
    public SendResponseSms(String receiverAddress, Context context)
    {
        receivingAddress = receiverAddress;
        constants = new Constants();
        manager = SMSManager.getInstance(context);
        applicationContext = context;
        locationManager = new LocationManager();
    }
}
