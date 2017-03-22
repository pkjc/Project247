package project.cse247.chat;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by PKJ on 3/21/2017.
 */

public class ChatMsgAdapter extends ArrayAdapter<ChatMessage> {
    Context context;
    int layoutResourceId;
    ChatMessage chatMsgdata[] = null;

    public ChatMsgAdapter(Context context, int layoutResourceId, ChatMessage[] chatMsgdata) {
        super(context, layoutResourceId, chatMsgdata);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.chatMsgdata = chatMsgdata;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ChatMsgHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ChatMsgHolder();
            holder.msgText = (TextView)row.findViewById(R.id.msgText);
            holder.msgTime = (TextView)row.findViewById(R.id.msgTime);
            holder.msgSender = (TextView)row.findViewById(R.id.msgSender);

            row.setTag(holder);
        } else {
            holder = (ChatMsgHolder)row.getTag();
        }

        ChatMessage chatMsg = chatMsgdata[position];

        holder.msgText.setText(chatMsg.getMessageText());
        holder.msgTime.setText(DateFormat.format("dd-mm-yyyy (HH:mm:ss)",chatMsg.getMessageTime()));
        holder.msgSender.setText(chatMsg.getMessageSender());

        return row;
    }
    static class ChatMsgHolder {
        TextView msgText;
        TextView msgSender;
        TextView msgTime;
    }
}