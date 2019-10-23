package com.dezen.riccardo.smshandler;

import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SmsHandler.OnSmsReceivedListener {

    private SmsHandler smsHandler;
    private Button button_send;
    private EditText editText_number;
    private EditText editText_message;
    private TextView textView_last_message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsHandler = new SmsHandler();
        smsHandler.registerReceiver(getApplicationContext());
        smsHandler.setListener(this);
        button_send = findViewById(R.id.button_send);
        editText_number = findViewById(R.id.editText_number);
        editText_message = findViewById(R.id.editText_message);
        textView_last_message = findViewById(R.id.textView_last_message);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(editText_number.getText().toString(), editText_message.getText().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smsHandler.clearListener();
        smsHandler.unregisterReceiver(getApplicationContext());
    }

    /**
     * Sends a message through this activity's instance of SmsHandler
     * @param destination the destination address for the message, in phone number format
     * @param message the body of the message to be sent, can't be neither null nor empty
     */
    private void sendMessage(String destination, @NonNull String message){
        smsHandler.sendSMS(destination, message);
    }

    /**
     * Method from the OnSmsReceivedListener interface, reads the body of the message and
     * updates a TextView's content
     * @param messages non-empty array of SmsMessages retrieved by SmsHandler
     */
    @Override
    public void onReceive(SmsMessage[] messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("Last message from: ")
            .append(messages[0].getOriginatingAddress())
            .append("\n");
        for(SmsMessage sms : messages) sb.append(sms.getMessageBody());
        textView_last_message.setText(sb.toString());
    }
}