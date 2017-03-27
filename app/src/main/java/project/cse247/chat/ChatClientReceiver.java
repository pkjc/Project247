package project.cse247.chat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The second part of the client implementation. This class is responsible for handling incoming
 * messages from the server.
 * Created by Noah on 3/24/2017.
 */
public class ChatClientReceiver implements Runnable {

    private Socket socket;
    private ChatClient chatClient;

    private BufferedReader inStream;

    /**
     * Create a new ChatClientReceiver, listening to the given socket and associated with the
     * given ChatClient
     *
     * @param socket     the socket to listen on
     * @param chatClient the associated ChatClient
     */
    public ChatClientReceiver(Socket socket, ChatClient chatClient) {
        this.socket = socket;
        this.chatClient = chatClient;

        try {
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            Log.d("Chat Client Receiver", e.toString());
        }
    }

    /**
     * Closes the input stream
     */
    public void close() {
        try {
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * While the socket is not closed, wait for input from the server, and do something with it
     */
    @Override
    public void run() {

        try {
            while (!socket.isClosed()) {
                String input = inStream.readLine();

                //the server has alerted us that it is going down!
                if (input.equals(":disconnect")) {
                    chatClient.close();
                    break;
                }

                //TODO: This method needs to be implemented!
                ChatManager.handleServerInput(input);
            }
        } catch (IOException e) {
            Log.d("Chat Client Receiver", e.toString());
        }

    }
}
