package project.cse247.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

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
        //mainActivity.message.setText(action);

        switch (action){
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                break;
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                manager.requestPeers(channel, mainActivity.peerListListener);

                String t = "Detected Peers: \n";

                for (WifiP2pDevice p : mainActivity.peers){
                    t += "Name: " + p.deviceName + "\tAddress: " + p.deviceAddress+"\n";
                }

                mainActivity.message.setText(t);

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
