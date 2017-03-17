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

public class PreviousConversationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.prev_conversations_act_title);
        setContentView(R.layout.activity_previous_conversations);
        getPreviousAllPreviousChats();
    }

    void getPreviousAllPreviousChats(){
        //get previous chats from file/DB and pass to populatePreviousChatsList method
        List<String> previousChatsList = new ArrayList<>();
        for(int i = 0;i<30;i++){
            previousChatsList.add("List Item : " + i);
        }

        populatePreviousChatsList(previousChatsList);
    }
    void populatePreviousChatsList(List previousChatsList){
        ListAdapter previousChatsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,previousChatsList );
        ListView previousChatsListView = (ListView) findViewById(R.id.previous_chats_list);
        previousChatsListView.setAdapter(previousChatsListAdapter);

        onPeerSelected(previousChatsListView);
    }

    void onPeerSelected(ListView previousChatsListView){
        previousChatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(view.getContext(), SingleConversationActivity.class);
                intent.putExtra("selectedChat", String.valueOf(adapterView.getItemAtPosition(position)));
                intent.putExtra("from", "prevChats");
                startActivity(intent);
            }
        });
    }
}