package project.cse247.chat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import static android.content.ContentValues.TAG;

/**
 * Created by pkj on 4/1/17.
 */

public class TxtServerAsyncTask extends AsyncTask {

    private Context context;
    private TextView statusText;

    public TxtServerAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] params)  {
        try {

            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a JPEG file
             */

            InputStream inputstream = client.getInputStream();

            BufferedReader buffIn = new BufferedReader(new InputStreamReader(inputstream));
            Log.d(TAG, "doInBackground: " + buffIn.readLine());

            serverSocket.close();
            return null;
        } catch (IOException e) {
            //Log.e(WiFiDirectActivity.TAG, e.getMessage());
            return null;
        }
    }

    /**
     * Start activity that can handle the JPEG image
     *//*
    @Override
    protected void onPostExecute(MediaBrowserService.Result result){
        if (result != null) {
            statusText.setText("File copied - " + result);
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + result), "image*//*");
            context.startActivity(intent);
        }
    }*/


}