package project.cse247.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_act_title);
        setContentView(R.layout.activity_main);
    }

    public void startPeerDiscoveryActivity(View view) {
        Intent intent = new Intent(this, DiscoveredPeersListActivity.class);
        startActivity(intent);
    }

//    public void startPreviousChatsActivity(View view) {
//        Intent intent = new Intent(this, PreviousConversationsActivity.class);
//        startActivity(intent);
//    }
}
