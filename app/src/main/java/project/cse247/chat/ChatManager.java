package project.cse247.chat;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Noah on 3/27/2017.
 */

public class ChatManager {

    /**
     * Host relevant global variables about any current Chat session
     */
    public static final class ChatState {

        private ChatState() {

        }

        /**
         * Track if we are in a chat session
         */
        public static boolean inSession = false;

        /**
         * Track if we are group leader
         */
        public static boolean groupLeader = false;

        /**
         * Track group leader address
         */
        public static String groupLeaderAddress = null;

        /**
         * Reset all state fields to their default values
         */
        public static void clearState() {
            inSession = false;
            groupLeader = false;
            groupLeaderAddress = null;
        }

    }

    private ChatServer chatServer;
    private ChatClient chatClient;

    private ArrayList<ChatMessage> bufferedMessages;

    public SingleConversationActivity singleConversationActivity;


    public ChatManager() {
        bufferedMessages = new ArrayList<ChatMessage>();
    }

    /**
     * Starts a chat server in a new thread. Destroys any chatServer that already exists
     */
    public void spawnChatServer() {
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
    public void spawnChatClient(final String address, final int port) {

        final ChatManager ref = this;

        new Thread(new Runnable() { //network actions cannot be run on the main thread
            @Override
            public void run() {
                Log.d("Chat Manager", "Attempting to spawn ChatClient...");
                destroyChatClient();

                try {
                    chatClient = new ChatClient(address, port);
                    chatClient.spawnChatClientReceiver();
                    chatClient.setChatManager(ref);
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
    public void destroyChatServer() {
        if (chatServer != null) {
            chatServer.close();
            chatServer = null;
        }
    }

    /**
     * Close and null out the ChatClient if it is not null already
     */
    public void destroyChatClient() {
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
    public void sendMessage(final ChatMessage message) {
        Log.d("Chat Manager", "Attempting to send: " + message);
        new Thread(new Runnable() {  //network actions cannot be run on the main thread
            @Override
            public void run() {
                if (chatClient != null) {
                    chatClient.sendMessage(message.toString());
                } else {
                    Log.d("Chat Manager", "Chat Client is not instantiated!");
                }
            }
        }).start();
    }

    /**
     * Should be called by ChatClientReceiver whenever it gets some input
     * <p>
     * Parses the given input into a message, and adds it to the buffer. Then calls the
     * updateConversationActivity function to write the input to the screen, if the view is
     * available
     *
     * @param input input received from the ChatServer
     */
    public void handleServerInput(String input) {

        Log.d("Chat Manager", "Input from server: " + input);

        Scanner scan = new Scanner(input);
        scan.useDelimiter("::-::");
        final String sender = scan.next();
        final String message = scan.next();

        ChatMessage chatMessage = new ChatMessage(message, sender, "");

        bufferedMessages.add(chatMessage);

        updateConversationActivity();
    }

    /**
     * Pushes all the messages in the queue to the SingleConversationActivity, as long as it isn't null
     */
    public void updateConversationActivity() {
        if (!(singleConversationActivity == null)) {
            singleConversationActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (ChatMessage c : bufferedMessages) {
                        singleConversationActivity.displayChatMsgs(c);
                    }

                    bufferedMessages.clear();
                }
            });
        }
    }
}
