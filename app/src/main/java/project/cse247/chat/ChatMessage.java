package project.cse247.chat;

import java.util.Date;

/**
 * Created by PKJ on 3/21/2017.
 */

public class ChatMessage {
    private String messageText;
    private String messageSender;
    private String messageReceiver;
    private Long messageTime;

    public ChatMessage(String messageText, String messageSender, String messageReceiver) {
        this.messageText = messageText;
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
        messageTime = new Date().getTime();
    }

    public ChatMessage(){}

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(String messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }

    @Override
    public String toString() {
        String s = messageSender + "::-::" + messageText+"\n";
        return s;
    }
}
