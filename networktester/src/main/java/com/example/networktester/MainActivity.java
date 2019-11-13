package com.example.networktester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dezen.riccardo.networkmanager.NetworkManager;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    NetworkManager manager;
    private final int REQUEST_CODE_PHONE_STATE = 1;
    private final int REQUEST_CODE_SMS = 0;
    private final String NETWORK_TESTER_TAG = "NETWORK_TESTER";
    TextView sampleText;
    EditText txtNumber;
    Button inviteButton;
    ListView peersListView;
    Button updatePeersButton;
    ArrayList<String> peers;
    ArrayAdapter<String> peersListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!SmsPermissionGranted())
            requestSmsPermission();

        sampleText = findViewById(R.id.sampleText);
        txtNumber = findViewById(R.id.txtnumber);
        inviteButton = findViewById(R.id.inviteButton);
        peersListView = findViewById(R.id.peersListView);
        updatePeersButton = findViewById(R.id.updatePeersButton);

        if(!PhoneStatePermissionGranted())
            requestPhoneStatePermission();

        peers = new ArrayList<>();
        peersListViewAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, peers);
        peersListView.setAdapter(peersListViewAdapter);


        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        checkPermission(Manifest.permission.READ_PHONE_STATE, android.os.Process.myPid(), android.os.Process.myUid());
        String mPhoneNumber = tMgr.getLine1Number();
        sampleText.setText(mPhoneNumber);
        SMSPeer myselfPeer = new SMSPeer(mPhoneNumber);

        //Creates manager with a Dictionary initialized with itself
        manager = new NetworkManager(getApplicationContext(), myselfPeer);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSPeer toInviterPeer = new SMSPeer(txtNumber.getText().toString());
                manager.invite(toInviterPeer);
            }
        });

        updatePeersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSPeer[] updatedPeers = manager.getAvailablePeers();
                peers = new ArrayList<String>(Arrays.asList(GetSMSPeersInArray(updatedPeers)));
                peersListView.setAdapter(peersListViewAdapter);
                peersListViewAdapter.notifyDataSetChanged();
                Log.d(NETWORK_TESTER_TAG, peers.get(0));
            }
        });



    }
    public String[] GetSMSPeersInArray(SMSPeer[] arraySMSPeers)
    {
        String[] arrayPeers = new String[arraySMSPeers.length];
        for (int i = 0; i < arraySMSPeers.length; i++) {
            arrayPeers[i] = arraySMSPeers[i].getAddress();
        }
        return arrayPeers;
    }

    public void requestPhoneStatePermission()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                REQUEST_CODE_PHONE_STATE);
    }

    public boolean PhoneStatePermissionGranted()
    {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
    }

    public boolean CheckPhoneStatePermission()
    {
        return checkPermission(Manifest.permission.READ_PHONE_STATE, android.os.Process.myPid(), android.os.Process.myUid())  == PackageManager.PERMISSION_GRANTED;
    }

    public boolean SmsPermissionGranted()
    {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED);
    }

    public void requestSmsPermission()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS},
                REQUEST_CODE_SMS);

    }


}


