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

public class SingleConversationActivity extends AppCompatActivity {

    ListAdapter chatMsgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getStringExtra("from").equals("prevChats")){
            setTitle(getIntent().getStringExtra("selectedChat"));
        }else if(getIntent().getStringExtra("from").equals("discoveredPeers")){
            setTitle(getIntent().getStringExtra("selectedPeer"));
        }
        setContentView(R.layout.activity_single_conversation);

        Button sendMsgBtn = (Button) findViewById(R.id.sendMsgBtn);
    }

    void displayChatMsgs(ChatMessage chatMessage){
        ListView chatMsgs = (ListView) findViewById(R.id.msgList);
        ChatMessage chatMsgArray[] = new ChatMessage[]{
                chatMessage
        };
        chatMsgAdapter = new ChatMsgAdapter(this, R.layout.chat_msg_row,chatMsgArray);
        chatMsgs.setAdapter(chatMsgAdapter);
    }

    public void onSendMsgBtnClick(View view) {
        EditText msgEditTextView = (EditText) findViewById(R.id.msgEditText);
        ChatMessage chatMessage = new ChatMessage(msgEditTextView.getText().toString(), "Sender", "Receiver");
        msgEditTextView.setText("");
        displayChatMsgs(chatMessage);
    }
}
