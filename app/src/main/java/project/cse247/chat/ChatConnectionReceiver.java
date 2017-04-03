package project.cse247.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * This receiver is responsible for handling changes in the connection.
 * Because it deals with an application-level service, it is defined in xml under the application tag
 * Created by Noah on 3/31/2017.
 */

public class ChatConnectionReceiver extends BroadcastReceiver {

    private ChatApp chatApp;
    private DiscoveredPeersListActivity discoveredPeersListActivity;

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
            ChatManager.ChatState.inSession = false;
        }
    }

    private WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(final WifiP2pInfo info) {
            /*if (info.groupFormed) {
                Log.d("Connection Listener", "Group formed!");
                if (info.isGroupOwner) {

                    Log.d("Connection Listener", "Device is group owner!");

                    chatApp.chatManager.spawnChatServer();
                    chatApp.chatManager.spawnChatClient(info.groupOwnerAddress.getHostAddress(), 8888);
                } else {
                    try {
                        chatApp.chatManager.spawnChatClient(info.groupOwnerAddress.getHostAddress(), 8888);
                        Log.d("**** Not grp ownr ****", "Device is group owner!");
                        discoveredPeersListActivity = new DiscoveredPeersListActivity();
                        discoveredPeersListActivity.startSingleConvAct();
                    } catch (Exception e) {
                        Log.d("Connection Listener", e.getMessage());
                    }
                }
            } else {
                Log.d("Connection Listener", "Something weird happened! Couldn't form a group");
            }*/

            if (info.groupFormed) {

                Log.d("Connection Listener", "Group formed...");

                final String address = info.groupOwnerAddress.getHostAddress();
                final int port = 8888;

                final boolean inSession = ChatManager.ChatState.inSession;
                final boolean groupLeader = ChatManager.ChatState.groupLeader;

                if (info.isGroupOwner) {

                    Log.d("Connection Listener", "Device is group owner...");
                    /*
                    Handle a brand new connection as group leader!
                     */
                    if (!inSession && !groupLeader) {
                        Log.d("Connection Listener", "Create a new connection! We are Chat Server!");
                        ChatManager.ChatState.groupLeader = true;
                        ChatManager.ChatState.inSession = true;
                        chatApp.chatManager.spawnChatServer();
                        chatApp.chatManager.spawnChatClient(address, port);
                    }
                    /*
                    Connection changed, but we're still group leader!
                     */
                    else if (inSession && groupLeader) {
                        Log.d("Connection Listener", "Connection changed, still Chat Server. Do Nothing!");
                    }

                    /*
                    We were a client, but now we are group leader!
                     */
                    else if (inSession && !groupLeader) {
                        Log.d("Connection Listener", "Connection changed, we are now Group Leader!");
                        ChatManager.ChatState.groupLeader = true;
                        chatApp.chatManager.spawnChatServer();
                        chatApp.chatManager.spawnChatClient(address, port);
                    }
                    else {
                        Log.d("Connection Listener", "Device state was not handled! inSession: " + inSession +
                                " groupLeader: " + groupLeader);
                    }


                } else {
                    /*
                    Handle a brand new connection as a client
                     */
                    if (!inSession && !groupLeader) {
                        Log.d("Connection Listener", "New connection to server!");
                        ChatManager.ChatState.inSession = true;
                        chatApp.chatManager.spawnChatClient(address, port);
                    }
                    /*
                    Handle a change in connection as a client
                     */
                    else if (inSession && !groupLeader) {
                        Log.d("Connection Listener", "Connection to server changed!");
                        chatApp.chatManager.spawnChatClient(address, port);
                    }
                    /*
                    Handle a change in connection, going from group leader to client
                     */
                    else if (inSession && groupLeader) {
                        Log.d("Connection Listener", "No longer server; connection to server changed!");
                        ChatManager.ChatState.groupLeader = false;
                        chatApp.chatManager.destroyChatServer();
                        chatApp.chatManager.spawnChatClient(address, port);
                    }
                    else {
                        Log.d("Connection Listener", "Device state was not handled! inSession: " + inSession +
                                " groupLeader: " + groupLeader);
                    }

                }
            } else {
                Log.d("Connection Listener", "Something weird happened! Couldn't form a group!");
            }
        }
    };
}
