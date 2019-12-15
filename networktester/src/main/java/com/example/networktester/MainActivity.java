package com.example.networktester;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dezen.riccardo.networkmanager.OnNetworkEventListener;
import com.dezen.riccardo.networkmanager.ReplicatedNetworkManager;
import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.Message;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.ArrayList;

/**
 * @author Niccolò Turcato. [...]
 * @author Riccardo De Zen. Fixed user list. Added interface callbacks. Fixed Permission-related
 * initialization.
 */
public class MainActivity extends AppCompatActivity implements OnNetworkEventListener {

    private static final String[] neededPermissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
    };
    private static final int ALL_PERMISSIONS_REQUEST_CODE = 0;
    private static final String NETWORK_TESTER_TAG = "NETWORK_TESTER";

    private ReplicatedNetworkManager manager;
    private TextView sampleText;
    private EditText txtNumber;
    private EditText editTextName;
    private EditText editTextValue;
    private Button inviteButton;
    private Button updateButton;
    private Button addResourceButton;
    private ListView peersListView;
    private ListView resourcesListView;

    private ArrayList<String> peers;
    private ArrayList<String> resources;
    private ArrayAdapter<String> peersAdapter;
    private ArrayAdapter<String> resourcesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(arePermissionsMissing()) requestPermissions();
        else init();
    }

    /**
     * Method containing the initialization of UI elements typically contained in onCreate.
     * It is located here to prevent it's execution if permissions are missing.
     */
    private void init(){
        sampleText = findViewById(R.id.sampleText);
        txtNumber = findViewById(R.id.txtnumber);
        editTextName = findViewById(R.id.editTextName);
        editTextValue = findViewById(R.id.editTextValue);
        inviteButton = findViewById(R.id.inviteButton);
        addResourceButton = findViewById(R.id.addResourceButton);
        peersListView = findViewById(R.id.peersListView);
        resourcesListView = findViewById(R.id.resourcesListView);
        updateButton = findViewById(R.id.updatePeersButton);

        peers = new ArrayList<>();
        peersAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView, peers);
        peersListView.setAdapter(peersAdapter);

        resources = new ArrayList<>();
        resourcesAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView, resources);
        resourcesListView.setAdapter(resourcesAdapter);

        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        final String mPhoneNumber = tMgr.getLine1Number();
        sampleText.setText(mPhoneNumber);
        SMSPeer myselfPeer = new SMSPeer(mPhoneNumber);

        //Creates manager with a Dictionary initialized with itself as first Peer
        manager = new ReplicatedNetworkManager(getApplicationContext(), myselfPeer);
        manager.setListener(this);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSPeer toInviterPeer = new SMSPeer(txtNumber.getText().toString());
                manager.invite(toInviterPeer);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] updatedPeers = getSMSPeersInArray(manager.getAvailablePeers());
                peers.clear();
                for (String p : updatedPeers) peers.add(p);
                peersAdapter.notifyDataSetChanged();

                String[] updatedResources = getStringResourcesInArray(manager.getAvailableResources());
                resources.clear();
                for (String r : updatedResources) resources.add(r);
                resourcesAdapter.notifyDataSetChanged();
            }
        });

        addResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String value = editTextValue.getText().toString();
                if(name.isEmpty() || value.isEmpty()) return;
                StringResource res = new StringResource(name, value);
                manager.createResource(res);
            }
        });
    }

    private String[] getSMSPeersInArray(SMSPeer[] arraySMSPeers)
    {
        String[] arrayPeers = new String[arraySMSPeers.length];
        for (int i = 0; i < arraySMSPeers.length; i++) {
            arrayPeers[i] = arraySMSPeers[i].getAddress();
        }
        return arrayPeers;
    }

    private String[] getStringResourcesInArray(Resource[] resources)
    {
        String[] resourcesToStrings = new String[resources.length];
        for (int i = 0; i < resources.length; i++) {
            resourcesToStrings[i] = resources[i].getName() + ": " + resources[i].getValue();
        }
        return resourcesToStrings;
    }

    /**
     * Method to check if one or more permissions are missing
     * @return true if at least one permission is missing, false otherwise
     */
    private boolean arePermissionsMissing(){
        for(String permission : neededPermissions){
            if(ContextCompat.checkSelfPermission(this,permission)
                    != PackageManager.PERMISSION_GRANTED) return true;
        }
        return false;
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

    /**
     * Method called when a Resource has been obtained through the network.
     *
     * @param resource the obtained Resource.
     */
    @Override
    public void onResourceObtained(Resource resource) {

    }

    @Override
    public void onMessageReceived(Message message) {

    }

    @Override
    protected void onStop(){
        super.onStop();
    }
}


