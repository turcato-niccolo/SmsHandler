package com.dezen.riccardo.smshandler;

import android.content.Context;

public class SMSManager extends CommunicationHandler<SMSMessage>{
    private static SMSManager instance;

    private Context owner;

    private SmsHandler smsHandler;
    private ReceivedMessageListener<SMSMessage> rListener;
    private SentMessageListener<SMSMessage> sListener;
    private DeliveredMessageListener<SMSMessage> dListener;
    private SmsHandler.OnSmsEventListener fullListener;

    public SMSManager getInstance(Context context){
        if(instance == null){
            instance = new SMSManager();
            owner = context;
        }
        return instance;
    }

    public void drop(Context context){
        if(context.equals(owner)){
            smsHandler.unregisterReceiver(owner);
            smsHandler.clearListener();
            owner = null;
        }
    }

    private SMSManager(){
        //prevent use of reflection to change constructor to public at runtime
        if (instance != null)
            throw new RuntimeException("This class uses the singleton building pattern. Use getInstance() to get a reference to the single instance of this class");
        smsHandler = new SmsHandler();
        fullListener = new SmsHandler.OnSmsEventListener() {
            @Override
            public void onReceive(String from, String message) {
                if(rListener != null){
                    SMSPeer p = new SMSPeer(from);
                    SMSMessage m = new SMSMessage(p, message);
                    rListener.onMessageReceived(m);
                }
            }

            @Override
            public void onSent(int resultCode) {
                if(sListener != null){

                }
            }

            @Override
            public void onDelivered(int resultCode) {
                if(dListener != null){

                }
            }
        };
    }

    @Override
    public void sendMessage(SMSMessage message) {
        smsHandler.sendSMS(message.getPeer().getAddress(),message.getData());
    }

    @Override
    public void addReceiveListener(ReceivedMessageListener<SMSMessage> listener) {
        smsHandler.registerReceiver(owner);
        smsHandler.setListener(fullListener);
        rListener = listener;
    }

    @Override
    public void removeReceiveListener() {
        smsHandler.unregisterReceiver(owner);
    }

    //TODO implementation of sending and delivery confirm
}
