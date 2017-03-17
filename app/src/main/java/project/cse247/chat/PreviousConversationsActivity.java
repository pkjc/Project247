package project.cse247.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PreviousConversationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.prev_conversations_act_title);
        setContentView(R.layout.activity_previous_conversations);
    }
}
