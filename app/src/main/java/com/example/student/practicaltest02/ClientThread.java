package com.example.student.practicaltest02;

/**
 * Created by student on 25.05.2018.
 */
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private String url;
    private int port;
    private TextView resultTextView;

    private Socket socket;

    public ClientThread(int port, String url, TextView resultTextView) {
        this.port = port;
        this.url = url;
        this.resultTextView = resultTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(url);
            printWriter.flush();
            String result = "";
            String line = "";
            while( (line = bufferedReader.readLine()) != null ) {
                result += line;
            }
            final String finalResult = result;
            resultTextView.post(new Runnable() {
                @Override
                public void run() {
                    resultTextView.setText(finalResult);
                }
            });
            while ((result = bufferedReader.readLine()) != null) {
                final String finalizedInformation = result;
                resultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(finalizedInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
