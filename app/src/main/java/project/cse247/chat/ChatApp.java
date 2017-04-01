package project.cse247.chat;

import android.app.Application;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Contains and initializes application-level resources on startup
 * Created by Noah on 3/31/2017.
 */
public class ChatApp extends Application {

    public WifiP2pManager manager;
    public WifiP2pManager.Channel channel;

    /**
     * This object is responsible for handling all the messaging
     * on behalf of the app
     */
    public ChatManager chatManager;

    public ChatApp() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        chatManager = new ChatManager();
    }
}
