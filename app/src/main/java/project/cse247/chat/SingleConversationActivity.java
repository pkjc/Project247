package project.cse247.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SingleConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getStringExtra("from").equals("prevChats")){
            setTitle(getIntent().getStringExtra("selectedChat"));
        }else if(getIntent().getStringExtra("from").equals("discoveredPeers")){
            setTitle(getIntent().getStringExtra("selectedPeer"));
        }
        setContentView(R.layout.activity_single_conversation);
    }
}
