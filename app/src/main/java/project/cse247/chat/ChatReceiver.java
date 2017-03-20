package project.cse247.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Noah on 3/6/2017.
 */

//Handles intents to/from the system, such as the detection of a new peer
//Or the changing of the connection
public class ChatReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private MainActivity mainActivity;

    public ChatReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                        MainActivity mainActivity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("Recieve: ", action);

        switch (action){
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                break;
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:

                //A new peer has been discovered!
                //Ask the WifiP2pManager to update the list of peers
                manager.requestPeers(channel, mainActivity.peerListListener);

                //Print the detected devices to the main screen
                mainActivity.deviceList.setText("Devices: \n");

                for (WifiP2pDevice p : mainActivity.peers){
                    mainActivity.deviceList.append(p.deviceName + "\t" + p.deviceAddress + "\n");
                }

                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:

                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                if (networkInfo.isConnected()){
                    manager.requestConnectionInfo(channel, mainActivity.connectionInfoListener);
                }

                break;
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                break;
            default:
                break;
        }
    }
}
