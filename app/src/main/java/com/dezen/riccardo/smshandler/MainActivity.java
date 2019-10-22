package com.dezen.riccardo.smshandler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.os.Bundle;


import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    // ArrayList<String> appSignature; //firme per la ricezione di sms
    //SmsWarden warden;
    BroadcastSmsHandler guardian;
    final int REQUEST_CODE = 1;


    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button send_button = (Button)findViewById(R.id.send_button);
        //TextView number = (TextView)findViewById(R.id.number_txtview);
        //TextView text = (TextView)findViewById(R.id.text_txtview);

        //warden = new SmsWarden(context);
        guardian = new BroadcastSmsHandler();
        //Controllo per l'accesso in ricezione e invio di sms, se lo trova non concesso, lo richede
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS))
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.SEND_SMS", "android.permission.RECEIVE_SMS"}, REQUEST_CODE);
        }

        send_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView number = (TextView)findViewById(R.id.number_txtview);
                        TextView text = (TextView)findViewById(R.id.text_txtview);

                        TextView message = (TextView)findViewById(R.id.message_txtview);

                        message.setText(number.getText() + " " + text.getText());
                        String res = guardian.Send(number.getText().toString(), text.getText().toString());
                        //String res = warden.Send(number.getText().toString(), text.getText().toString());
                        Toast.makeText(context, res, Toast.LENGTH_LONG).show();

                    }
                }
        );


    }




}
