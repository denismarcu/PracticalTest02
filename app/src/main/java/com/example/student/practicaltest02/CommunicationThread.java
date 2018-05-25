package com.example.student.practicaltest02;

/**
 * Created by student on 25.05.2018.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        if(serverThread == null || socket == null) {
            throw new IllegalArgumentException("parameters are null!");
        }
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (null == bufferedReader || null == printWriter) {
                Log.e("[COMMUNICATION THREAD]", "error!\n");
                // error
                return;
            }

            String url = bufferedReader.readLine();
            if (null == url || url.isEmpty()) {
                Log.e("[COMMUNICATION THREAD]", "error!\n");
                return; // error
            }

            String result = null;
            if(url.contains("bad")) {
                result = "URL blocked by firewall!\n";
            }
            else {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String pageSourceCode = httpClient.execute(httpPost, responseHandler);

                if (pageSourceCode == null) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                    return;
                }

                result = pageSourceCode;
            }
            printWriter.println(result);
            printWriter.flush();

        } catch (IOException io) {
            Log.e("[COMMUNICATION THREAD]", "Caught exception!\n");
            io.printStackTrace();
        }  finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
    }
}
