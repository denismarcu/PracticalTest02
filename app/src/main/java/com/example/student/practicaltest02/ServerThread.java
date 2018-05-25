package com.example.student.practicaltest02;

/**
 * Created by student on 25.05.2018.
 */
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class ServerThread extends Thread {

    private int port = 0;
    private ServerSocket serverSocket;
    private HashMap<String, String> data = null;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ServerThread(int port) {
        this.port = port;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException io) {
            Log.e("[SERVER THREAD]", "exception occured!\n");
        }
        data = new HashMap<>();
    }

    public void stopThread() {
        interrupt();
        if(serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException io) {
                Log.e("[SERVER THREAD]", "error!\n");
            }
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                new Thread(communicationThread).start();
            }
        } catch (Exception ex) {
            Log.e("[SERVER THREAD]", "error!\n");
        }
    }

    public synchronized void setData(String url, String body) {
        data.put(url, body);
    }

    public synchronized HashMap<String, String> getData() {
        return data;
    }
}
