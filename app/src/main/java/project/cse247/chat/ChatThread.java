package project.cse247.chat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This object represents a connection to an individual client
 * <p>
 * It contains a method for broadcasting a string to the client.
 * Created by Noah on 3/24/2017.
 */
public class ChatThread implements Runnable {
    private Socket socket;
    private ChatServer chatServer;

    private BufferedReader inStream;
    private PrintWriter outStream;

    public ChatThread(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;

        try {
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outStream = new PrintWriter(socket.getOutputStream());
            Log.d("Chat Thread", "New thread instantiated!");
        } catch (IOException e) {
            Log.d("Chat Thread", e.toString());
        }
    }

    /**
     * While the socket is not closed, return all input to the ChatServer
     */
    @Override
    public void run() {

        Log.d("Chat Thread", "Thread listening...");

        try {
            while (!socket.isClosed()) {
                String input = inStream.readLine();

                Log.d("Chat Thread", "Received: " + input);

                chatServer.handleClientInput(input, this);
            }
        } catch (IOException e) {
            Log.d("Chat Thread", e.toString());
            chatServer.dropClient(this);
        }
    }

    /**
     * Close all resources associated with this connection
     * All blocking calls should abort
     */
    public void close() {
        try {
            inStream.close();
            outStream.close();
            socket.close();
        } catch (IOException e) {
            Log.d("Chat Thread", e.toString());
        }
    }

    /**
     * Broadcast a message to this client
     *
     * @param s the desired message
     */
    public void sendMessage(String s) {

        Log.d("Chat Thread", "Sending to client: " + s);

        outStream.print(s);
        outStream.flush();
    }
}