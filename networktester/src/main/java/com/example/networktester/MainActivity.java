package com.example.networktester;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dezen.riccardo.networkmanager.NetworkManager;
import com.dezen.riccardo.networkmanager.OnNetworkEventListener;
import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnNetworkEventListener {

    private static final String[] neededPermissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
    };
    private static final int REQUEST_CODE_PHONE_STATE = 2;
    private static final int REQUEST_CODE_SMS = 1;
    private static final int ALL_PERMISSIONS_REQUEST_CODE = 0;
    private static final String NETWORK_TESTER_TAG = "NETWORK_TESTER";

    private NetworkManager manager;
    private TextView sampleText;
    private ImageView imageView;
    private EditText txtNumber;
    private Button inviteButton;
    private ListView peersListView;
    private Button updatePeersButton;
    private ArrayList<String> peers;
    private ArrayAdapter<String> peersListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(arePermissionsMissing()) requestPermissions();
        else init();
    }

    private void init(){
        sampleText = findViewById(R.id.sampleText);
        txtNumber = findViewById(R.id.txtnumber);
        inviteButton = findViewById(R.id.inviteButton);
        peersListView = findViewById(R.id.peersListView);
        updatePeersButton = findViewById(R.id.updatePeersButton);
        imageView = findViewById(R.id.imageView);

        peers = new ArrayList<>();
        peersListViewAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView,peers);
        peersListView.setAdapter(peersListViewAdapter);

        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        String mPhoneNumber = tMgr.getLine1Number();
        sampleText.setText(mPhoneNumber);
        SMSPeer myselfPeer = new SMSPeer(mPhoneNumber);

        //Creates manager with a Dictionary initialized with itself as first Peer
        manager = new NetworkManager(getApplicationContext(), myselfPeer);
        manager.setListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.smile();
                imageView.setImageDrawable(getDrawable(R.drawable.vector_smile_green));
            }
        });

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
                String[] updatedPeers = GetSMSPeersInArray(manager.getAvailablePeers());
                peers.clear();
                for(String newPeer : updatedPeers) peers.add(newPeer);
                peersListViewAdapter.notifyDataSetChanged();
                Log.d(NETWORK_TESTER_TAG, ""+peers.size());
                for(String peer : peers) Log.d(NETWORK_TESTER_TAG, peer);
            }
        });
    }

    private String[] GetSMSPeersInArray(SMSPeer[] arraySMSPeers)
    {
        String[] arrayPeers = new String[arraySMSPeers.length];
        for (int i = 0; i < arraySMSPeers.length; i++) {
            arrayPeers[i] = arraySMSPeers[i].getAddress();
        }
        return arrayPeers;
    }

    /**
     * Method to check if one or more permissions are missing
     * @return true if at least one permission is missing, false otherwise
     */
    private boolean arePermissionsMissing(){
        boolean missing = false;
        for(String permission : neededPermissions){
            if(ContextCompat.checkSelfPermission(this,permission)
                    != PackageManager.PERMISSION_GRANTED) missing = true;
        }
        return missing;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this,neededPermissions,ALL_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result : grantResults){
            //If a permission is missing application closes.
            if(result != PackageManager.PERMISSION_GRANTED) finish();
        }
        //If all permissions are granted activity continues setting itself up
        init();
    }

    @Override
    public void onResourceObtained(Resource obtRes) {

    }

    @Override
    public void onMessageReceived(SMSMessage message) {
        if(message.getData().contains("SMILE")) imageView.setImageDrawable(getDrawable(R.drawable.vector_smile_green));
    }

    /*public void requestPhoneStatePermission()
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
    }*/


}


