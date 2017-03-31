package project.cse247.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SingleConversationActivity extends AppCompatActivity {

    ListAdapter chatMsgAdapter;
    List<ChatMessage> chatMsgArrayList = new ArrayList<>();
    String thisDeviceName = "";
    String msgReceiver = "";

    private ChatApp chatApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("from").equals("prevChats")) {
            setTitle(getIntent().getStringExtra("selectedChat"));
        } else if (getIntent().getStringExtra("from").equals("discoveredPeers")) {
            setTitle(getIntent().getStringExtra("selectedPeer"));
        }
        setContentView(R.layout.activity_single_conversation);
        thisDeviceName = getIntent().getStringExtra("thisDeviceName");
        msgReceiver = getIntent().getStringExtra("msgReceiver");

        chatApp = (ChatApp) this.getApplication();
        chatApp.chatManager.singleConversationActivity = this;
        chatApp.chatManager.updateConversationActivity(); //write any messages currently saved by the ChatManager
    }

    void displayChatMsgs(ChatMessage chatMessage) {
        ListView chatMsgs = (ListView) findViewById(R.id.msgList);
        chatMsgArrayList.add(chatMessage);

        chatMsgAdapter = new ChatMsgAdapter(this, R.layout.chat_msg_row,
                chatMsgArrayList.toArray(new ChatMessage[chatMsgArrayList.size()]));
        chatMsgs.setAdapter(chatMsgAdapter);
    }

    public void onSendMsgBtnClick(View view) {
        EditText msgEditTextView = (EditText) findViewById(R.id.msgEditText);
        if (!msgEditTextView.getText().toString().isEmpty()) {
            ChatMessage chatMessage = new ChatMessage(msgEditTextView.getText().toString(), thisDeviceName, msgReceiver);
            Log.d("Single Conversation", "Message Created: " + chatMessage.toString());

            //Ask the chatManager to try sending this message to the server
            chatApp.chatManager.sendMessage(chatMessage);

            msgEditTextView.setText("");
            displayChatMsgs(chatMessage);
        } else
            return;
    }
}
