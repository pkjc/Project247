package project.cse247.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DiscoveredPeersListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.discovered_peers_act_title);
        setContentView(R.layout.activity_discovered_peers_list);
        discoverPeers();
    }

    void discoverPeers(){
        //Discover peers with Wifip2P API and pass to populateDiscoveredPeersList method
        List<String> discoveredPeersList = new ArrayList<>();
        for(int i = 0;i<30;i++){
            discoveredPeersList.add("List Item : " + i);
        }

        populateDiscoveredPeersList(discoveredPeersList);
    }

    void populateDiscoveredPeersList(List discoveredPeersList){
        ListAdapter discoveredPeersListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,discoveredPeersList );
        ListView discoveredPeersListView = (ListView) findViewById(R.id.discovered_peers_list);
        discoveredPeersListView.setAdapter(discoveredPeersListAdapter);

        onPeerSelected(discoveredPeersListView);
    }

    void onPeerSelected(ListView discoveredPeersListView){
        discoveredPeersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(view.getContext(), SingleConversationActivity.class);
                intent.putExtra("selectedPeer", String.valueOf(adapterView.getItemAtPosition(position)));
                intent.putExtra("from", "discoveredPeers");
                startActivity(intent);
            }
        });
    }
}