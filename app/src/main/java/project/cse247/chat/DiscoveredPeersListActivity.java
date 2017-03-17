package project.cse247.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DiscoveredPeersListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.discovered_peers_act_title);
        setContentView(R.layout.activity_discovered_peers_list);

        discoverPeers();
        populateDiscoveredPeersList();
    }

    void discoverPeers(){
    }

    void populateDiscoveredPeersList(){
    }

    void onPeerSelected(){
    }

}
