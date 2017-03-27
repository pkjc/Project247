package project.cse247.chat;

import android.util.Log;

import java.io.IOException;

/**
 * Created by Noah on 3/27/2017.
 */

public class ChatManager {

    private static ChatServer chatServer;
    private static ChatClient chatClient;

    /**
     * Starts a chat server in a new thread. Destroys any chatServer that already exists
     */
    public static void spawnChatServer() {
        new Thread(new Runnable() { //Network actions cannot be run on the main thread
            @Override
            public void run() {
                destroyChatServer();

                try {
                    chatServer = new ChatServer(); //create a chat server
                    new Thread(chatServer).start(); //fork the chat server's welcome loop into a new thread
                } catch (IOException e) {
                    Log.d("Chat Manager", e.getMessage());
                    destroyChatServer();
                }
            }
        }).start();
    }

    /**
     * Create a ChatClient, and spawn a ChatReceiver in a new thread. Destroys and ChatClient
     * that already exists
     *
     * @param address the device address to connect to
     * @param port    the port number to connect on
     */
    public static void spawnChatClient(final String address, final int port) {
        new Thread(new Runnable() { //network actions cannot be run on the main thread
            @Override
            public void run() {
                Log.d("Chat Manager", "Attempting to spawn ChatClient...");
                destroyChatClient();

                try {
                    chatClient = new ChatClient(address, port);
                    chatClient.spawnChatClientReceiver();
                    Log.d("Chat Manager", "Chat client instantiated!");
                } catch (IOException e) {
                    Log.d("Chat Manager", e.getMessage());
                    destroyChatClient();
                }
            }
        }).start();
    }

    /**
     * Close and null out the chatServer if it is not null already
     */
    public static void destroyChatServer() {
        if (chatServer != null) {
            chatServer.close();
            chatServer = null;
        }
    }

    /**
     * Close and null out the ChatClient if it is not null already
     */
    public static void destroyChatClient() {
        if (chatClient != null) {
            chatClient.close();
            chatClient = null;
        }
    }

    /**
     * Attempt to send a message into the chat network
     *
     * @param message the message to send
     */
    public static void sendMessage(final ChatMessage message) {
        new Thread(new Runnable() {  //network actions cannot be run on the main thread
            @Override
            public void run() {
                if (chatClient != null) {
                    //TODO: This takes a string - format this as desired
                    chatClient.sendMessage(message.getMessageText());
                } else {
                    Log.d("Chat Manager", "Chat Client is not instantiated!");
                }
            }
        }).start();
    }

    /**
     * Should be called by ChatClientReceiver whenever it gets some input
     *
     * @param input input received from the ChatServer
     */
    //TODO: send message to appropriate place - maybe we need to reference a chat view in this class?
    //TODO: Should probably be parsed into a ChatMessage object
    public static void handleServerInput(String input) {

    }
}
