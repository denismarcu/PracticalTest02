package com.example.student.practicaltest02;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText portEditText = null;
    private Button startButton = null;

    // Client widgets
    private String clientAddressEditText = "localhost";
    private EditText urlEditText = null;
    private Button goButton = null;
    private TextView resultTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private StartButtonClickListener startButtonClickListener = new StartButtonClickListener();
    private class StartButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = portEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }


    private GoButtonClickListener goButtonClickListener = new GoButtonClickListener();
    private class GoButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientPort = portEditText.getText().toString();
            if (clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = urlEditText.getText().toString();
            if (url == null || url.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            resultTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(Integer.parseInt(clientPort), url, resultTextView);
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        portEditText = (EditText)findViewById(R.id.port_edit_text);
        startButton = (Button)findViewById(R.id.start_button);
        startButton.setOnClickListener(startButtonClickListener);

        urlEditText = (EditText)findViewById(R.id.url_edit_text);
        goButton = (Button)findViewById(R.id.go_button);
        goButton.setOnClickListener(goButtonClickListener);
        resultTextView = (TextView)findViewById(R.id.result_text_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
