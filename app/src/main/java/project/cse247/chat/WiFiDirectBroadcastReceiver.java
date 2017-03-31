package project.cse247.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by PKJ on 3/20/2017.
 */

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private DiscoveredPeersListActivity discoveredPeersListActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       DiscoveredPeersListActivity discoveredPeersListActivity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.discoveredPeersListActivity = discoveredPeersListActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi Direct is enabled
            } else {
                // Wi-Fi Direct is not enabled
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
                mManager.requestPeers(mChannel, myPeerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                mManager.requestConnectionInfo(mChannel, connectionInfoListener);
            } else {
                ChatManager.destroyChatClient();
                ChatManager.destroyChatServer();
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            String thisDeviceName = "";
            if (!(device == null)) {
                thisDeviceName = device.deviceName;
            }
            discoveredPeersListActivity.setThisDeviceName(thisDeviceName);
        }
    }

    WifiP2pManager.PeerListListener myPeerListListener = new WifiP2pManager.PeerListListener() {
        List<WifiP2pDevice> discoveredPeersList = new ArrayList<>();

        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceListProvider) {

            Collection<WifiP2pDevice> refreshedPeers = wifiP2pDeviceListProvider.getDeviceList();

            if (!refreshedPeers.equals(discoveredPeersList)) {
                discoveredPeersList.clear();
                discoveredPeersList.addAll(refreshedPeers);
            }

            discoveredPeersListActivity.populateDiscoveredPeersList(discoveredPeersList);

        }
    };

    /**
     * When a connection has been established, initiate chat services
     */
    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {

            // InetAddress from WifiP2pInfo struct.
            //InetAddress groupOwnerAddress = info.groupOwnerAddress.getHostAddress());

            if (info.groupFormed) {

                if (info.isGroupOwner) {
                    ChatManager.spawnChatServer();
                    ChatManager.spawnChatClient("127.0.0.1", 8888);
                } else {
                    try {
                        ChatManager.spawnChatClient(info.groupOwnerAddress.getHostAddress(), 8888);
                    } catch (Exception e) {
                        Log.d("Connection Listener", "Exception creating chat client! Does network owner have the app?");
                    }
                }

            } else {
                Log.d("Connection Listener", "Something weird happened! No group formed!");
            }
        }
    };
}
