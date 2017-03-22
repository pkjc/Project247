package project.cse247.chat;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SingleConversationActivity extends AppCompatActivity {

    ListAdapter chatMsgAdapter;
    List<ChatMessage> chatMsgArrayList = new ArrayList<>();
    String thisDeviceName = "";
    String msgReceiver = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getStringExtra("from").equals("prevChats")){
            setTitle(getIntent().getStringExtra("selectedChat"));
        }else if(getIntent().getStringExtra("from").equals("discoveredPeers")){
            setTitle(getIntent().getStringExtra("selectedPeer"));
        }
        setContentView(R.layout.activity_single_conversation);
        thisDeviceName = getIntent().getStringExtra("thisDeviceName");
        msgReceiver = getIntent().getStringExtra("msgReceiver");
    }

    void displayChatMsgs(ChatMessage chatMessage){
        ListView chatMsgs = (ListView) findViewById(R.id.msgList);
        chatMsgArrayList.add(chatMessage);

        chatMsgAdapter = new ChatMsgAdapter(this, R.layout.chat_msg_row,
                chatMsgArrayList.toArray(new ChatMessage[chatMsgArrayList.size()]));
        chatMsgs.setAdapter(chatMsgAdapter);
    }

    public void onSendMsgBtnClick(View view) {
        EditText msgEditTextView = (EditText) findViewById(R.id.msgEditText);
        if(!msgEditTextView.getText().toString().isEmpty()){
            ChatMessage chatMessage = new ChatMessage(msgEditTextView.getText().toString(), thisDeviceName, msgReceiver);
            msgEditTextView.setText("");
            displayChatMsgs(chatMessage);
        }else
            return;
    }
}
