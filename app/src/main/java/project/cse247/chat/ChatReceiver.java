package project.cse247.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

public class ChatReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private MainActivity mainActivity;

    public ChatReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                        MainActivity mainActivity){
        super();
        this.manager = manager;
        this.channel = channel;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("Recieve: ", action);
        ListView peerListView = (ListView) mainActivity.findViewById(R.id.peer_list);

        switch (action){
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                break;
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                manager.requestPeers(channel, mainActivity.peerListListener);
                ArrayList<String> peerList = new ArrayList<>();

                for (WifiP2pDevice wifiP2pDevice : mainActivity.peers){
                    peerList.add(wifiP2pDevice.deviceName);
                }

                ArrayAdapter<String> peerListAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_list_item_1, peerList);

                peerListView.setAdapter(peerListAdapter);

                peerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //TODO
                    }
                });

               /* String t = "Detected Peers: \n";

                for (WifiP2pDevice p : mainActivity.peers){
                    t += "Name: " + p.deviceName + "\tAddress: " + p.deviceAddress+"\n";
                }

                mainActivity.message.setText(t);*/

                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                break;
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                break;
            default:
                break;
        }
    }
}
