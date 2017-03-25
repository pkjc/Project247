package project.cse247.chat;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is the main managing class for a chat server.
 * If a device is 'group owner' it should instantiate this class (and destroy it if it loses network
 * ownership)
 * <p>
 * This implementation will rebroadcast messages from any one client to the rest of the clients that
 * are connected to it.
 * Created by Noah on 3/24/2017.
 */

public class ChatServer implements Runnable {

    private ServerSocket welcomeSocket;
    private ArrayList<ChatThread> chatThreads;

    public ChatServer() throws IOException {
        chatThreads = new ArrayList<ChatThread>();
        welcomeSocket = new ServerSocket(8888);
    }

    @Override
    public void run() {
        try {
            while (!welcomeSocket.isClosed()) {
                Log.d("Server", "Waiting for incoming connections...");
                Socket clientSocket = welcomeSocket.accept();
                Log.d("Server", "Client has been accepted");
                spawnClient(clientSocket);
            }
        } catch (IOException e) {
            Log.d("Server", e.toString());
        }
    }

    /**
     * Broadcast the specified message from the specified client into that chat network
     *
     * @param input  the desired message
     * @param client the corresponding client
     */
    public synchronized void handleClientInput(String input, ChatThread client) {
        if (input == null) {
            Log.d("Server", "Null input received from client!");
        } else if (input.equals(":quit")) {
            dropClient(client);
        } else {
            for (ChatThread c : chatThreads) {
                if (!c.equals(client)) {
                    c.sendMessage(input + "\n");
                }
            }
        }
    }

    /**
     * Close all resources associated with this server. The server will stop running if it is
     * currently working in another thread.
     */
    public synchronized void close() {

        try {
            welcomeSocket.close();

            for (ChatThread c : chatThreads) {
                c.sendMessage(":disconnect"); //notify clients that the is disconnecting
                c.close();
            }

            chatThreads.clear();

        } catch (IOException e) {
            Log.d("Server", e.toString());
        }

    }


    /**
     * Forks a new thread for a newly accepted client socket
     *
     * @param clientSocket the socket of a new client
     */
    public void spawnClient(Socket clientSocket) {
        ChatThread client = new ChatThread(clientSocket, this);
        chatThreads.add(client);
        new Thread(client).start();
        Log.d("Server", "New client thread has been spawned!");
    }

    /**
     * Remove the specified client from the collection, and close its resources
     *
     * @param client the client that needs to be dropped
     */
    public void dropClient(ChatThread client) {
        chatThreads.remove(client);
        client.close();
    }
}
