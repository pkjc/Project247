package project.cse247.chat;

import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * One of two parts of the client version. The ChatClient object should be created for any device
 * that wishes to participate in the chat network. In particular, this class is responsible for
 * broadcasting user messages into the network
 * <p>
 * Created by Noah on 3/24/2017.
 */

public class ChatClient {

    private Socket socket;
    private ChatClientReceiver receiver;

    private PrintWriter outStream;

    /**
     * Create a new ChatClient, which connects to the server at the specified address and port
     *
     * @param address the device to connect to
     * @param port    the port
     * @throws IOException represents a failure in the attempt to establish a connection
     */
    public ChatClient(String address, int port) throws IOException {
        socket = new Socket(address, port);
        receiver = new ChatClientReceiver(socket, this);
        outStream = new PrintWriter(socket.getOutputStream());
    }


    /**
     * Broadcast a message to the server
     *
     * @param message the desired message to broadcast
     */
    public void sendMessage(String message) {
        outStream.print(message);
        outStream.flush();
    }

    /**
     * Close down the socket and associated ChatClientReceiver
     */
    public void close() {
        try {
            outStream.close();
            if (receiver != null) receiver.close();
            socket.close();
        } catch (IOException e) {
            Log.d("Chat Client", e.toString());
        }
    }

    /**
     * Spawns a new thread to listen for incoming messages from the server
     */
    public void spawnChatClientReceiver() {
        new Thread(receiver).start();
    }
}
