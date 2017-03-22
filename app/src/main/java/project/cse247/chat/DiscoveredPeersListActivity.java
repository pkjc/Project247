package project.cse247.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiscoveredPeersListActivity extends AppCompatActivity {

    private static final String TAG = "Disc Peer" ;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.discovered_peers_act_title);
        setContentView(R.layout.activity_discovered_peers_list);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        onPeerDiscovery();
    }

    void onPeerDiscovery(){

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //Toast.makeText(DiscoveredPeersListActivity.this, "Peer Discovery Successful!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(DiscoveredPeersListActivity.this, "Peer Discovery Failed! Reason Code: " + reasonCode, Toast.LENGTH_SHORT).show();
            }
        });

    }

    void populateDiscoveredPeersList(List<WifiP2pDevice> discoveredPeersList){
        List<String> peerDeviceNameList = new ArrayList<>();

        for(WifiP2pDevice peerDeviceName:discoveredPeersList){
            peerDeviceNameList.add(peerDeviceName.deviceName);
        }

        ListAdapter discoveredPeersListAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,peerDeviceNameList);

        ListView discoveredPeersListView = (ListView) findViewById(R.id.discovered_peers_list);
        discoveredPeersListView.setAdapter(discoveredPeersListAdapter);

        onPeerSelected(discoveredPeersListView);
    }

    void onPeerSelected(ListView discoveredPeersListView){
        discoveredPeersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(view.getContext(), SingleConversationActivity.class);
                intent.putExtra("selectedPeer", String.valueOf(adapterView.getItemAtPosition(position)));
                intent.putExtra("from", "discoveredPeers");
                startActivity(intent);
            }
        });
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}