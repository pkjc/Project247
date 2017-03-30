package project.cse247.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.design.widget.Snackbar;
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
    String thisDeviceName;

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
                Snackbar.make(findViewById(R.id.discoveredPeersActivity), "Peers Discovered. Fetching...", Snackbar.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(DiscoveredPeersListActivity.this, "Peer Discovery Failed! Reason Code: " + reasonCode, Toast.LENGTH_SHORT).show();
            }
        });

    }

    void populateDiscoveredPeersList(List<WifiP2pDevice> discoveredPeersList){
        List<String> peerDeviceNameList = new ArrayList<>();

        for(WifiP2pDevice peerDevice:discoveredPeersList){
            peerDeviceNameList.add(peerDevice.deviceName);
        }

        ListAdapter discoveredPeersListAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,peerDeviceNameList);

        ListView discoveredPeersListView = (ListView) findViewById(R.id.discovered_peers_list);
        discoveredPeersListView.setAdapter(discoveredPeersListAdapter);

        //onPeerSelected(discoveredPeersListView, discoveredPeersList);
    }

    public void onCreateChatRoomBtnClick(View view) {

        //Trigger logic to connect to peers, form group and spawn clients and servers

        Intent intent = new Intent(this, SingleConversationActivity.class);
        intent.putExtra("thisDeviceName", getThisDeviceName());
        intent.putExtra("from", "discoveredPeers");
        startActivity(intent);
    }

    void onPeerSelected(ListView discoveredPeersListView, final List<WifiP2pDevice> discoveredPeersList){
        discoveredPeersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                final Intent intent = new Intent(view.getContext(), SingleConversationActivity.class);
                String peerDeviceAddress = "";
                for(WifiP2pDevice peerDevice:discoveredPeersList){
                    if(peerDevice.deviceName.equals(adapterView.getItemAtPosition(position).toString())){
                        peerDeviceAddress = peerDevice.deviceAddress;
                    }
                }

                final WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = peerDeviceAddress;

                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        //success logic
                        intent.putExtra("msgReceiver", config.deviceAddress);
                    }

                    @Override
                    public void onFailure(int reason) {
                        //failure logic
                    }
                });
                intent.putExtra("thisDeviceName", getThisDeviceName());
                intent.putExtra("selectedPeer", adapterView.getItemAtPosition(position).toString());
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

    public String getThisDeviceName() {
        return thisDeviceName;
    }

    public void setThisDeviceName(String thisDeviceName) {
        this.thisDeviceName = thisDeviceName;
    }

}