package project.cse247.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public TextView deviceList;

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver;
    private IntentFilter filter;

    public List<WifiP2pDevice> peers = new ArrayList<>();

    /*
    Create a PeerListener which simply puts detected devices into a list
     */
    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            Collection<WifiP2pDevice> refreshedPeers = peerList.getDeviceList();
            if (!refreshedPeers.equals(peers)) {
                peers.clear();
                peers.addAll(refreshedPeers);
            }
        }
    };

    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(final WifiP2pInfo info) {

            InetAddress groupOwnerAddress = info.groupOwnerAddress;

            Log.d("Debugging", "Enter onConnectionInfoAvailable");

            if (info.groupFormed && info.isGroupOwner){
                Log.d("Group", "GROUP FORMED; CURRENTLY GROUP LEADER");
            }
            else if (info.groupFormed){
                Log.d("Group", "GROUP FORMED");
            }
            else{
                Log.d("Group", "NO GROUP");
            }

            Log.d("Group", "Group owner: " + groupOwnerAddress.toString());

        }
    };

    /*
    Do initialization stuff for the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Startup", "Startup initiated");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceList = (TextView) findViewById(R.id.device_list);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

        receiver = new ChatReceiver(manager, channel, this);

        filter = new IntentFilter();

        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    public void do_connect(View view){

        Log.d("Connection", "Attempting to connect to peers");

        //TESTING
        for (WifiP2pDevice p : peers){
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = p.deviceAddress;

            manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d("Connection", "CONNECTION ESTABLISHED!");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d("Connection", "FAILED!");
                }
            });
        }
    }

    /*
    Handle resuming the activity
     */
    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(receiver, filter);
    }

    /*
    Handle pausing the activity
     */
    @Override
    protected void onPause(){
        super.onPause();
        registerReceiver(receiver, filter);
    }
}
