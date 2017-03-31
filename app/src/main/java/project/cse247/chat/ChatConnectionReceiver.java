package project.cse247.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.io.IOException;

/**
 * This receiver is responsible for handling changes in the connection.
 * Because it deals with an application-level service, it is defined in xml under the application tag
 * Created by Noah on 3/31/2017.
 */

public class ChatConnectionReceiver extends BroadcastReceiver {

    private ChatApp chatApp;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (!action.equals(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)) {
            return;
        }

        chatApp = (ChatApp) context.getApplicationContext();
        NetworkInfo info = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

        if (info.isConnected()) {
            chatApp.manager.requestConnectionInfo(chatApp.channel, connectionInfoListener);
        } else {
            chatApp.chatManager.destroyChatClient();
            chatApp.chatManager.destroyChatServer();
            ChatManager.inSession = false;
        }
    }

    private WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            if (info.groupFormed) {
                Log.d("Connection Listener", "Group formed!");
                if (info.isGroupOwner) {

                    Log.d("Connection Listener", "Device is group owner!");

                    chatApp.chatManager.spawnChatServer();
                    chatApp.chatManager.spawnChatClient(info.groupOwnerAddress.getHostAddress(), 8888);
                } else {
                    try {
                        chatApp.chatManager.spawnChatClient(info.groupOwnerAddress.getHostAddress(), 8888);
                    } catch (Exception e) {
                        Log.d("Connection Listener", e.getMessage());
                    }
                }
            } else {
                Log.d("Connection Listener", "Something weird happened! Couldn't form a group");
            }
        }
    };
}
