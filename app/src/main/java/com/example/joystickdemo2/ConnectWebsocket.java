package com.example.joystickdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ConnectWebsocket extends AppCompatActivity {

    private EditText editTextUrl;
    private Button buttonConnect;
    private TextView urlTextView;
    private OkHttpClient client;
    private

    WebSocket webSocket;

    @Override


    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_websocket);

        editTextUrl = findViewById(R.id.editTextUrl);
        buttonConnect = findViewById(R.id.buttonConnect);
        urlTextView = findViewById(R.id.url);

        client = new OkHttpClient(); // Initialize OkHttpClient

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view)

            {
                connectWebsocket();
            }
        });
    }

    private void connectWebsocket() {
        String url = editTextUrl.getText().toString().trim();
        if (!url.isEmpty()) {
            Request request = new Request.Builder().url(url).build();

            WebSocketListener webSocketListener = new WebSocketListener() {
                @Override


                public void onOpen(WebSocket webSocket, Response response) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                urlTextView.setText("Connected to: " + url);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("WebSocket", "Error in onOpen: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override


                public void onFailure(WebSocket webSocket, Throwable t, Response response)

                {
                    super.onFailure(webSocket, t, response);
                    Log.d("WebSocket", "Connection failure: " + t.getMessage());
                    t.printStackTrace();
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    super.onMessage(webSocket, text);
                    Log.d("WebSocket", "Received message: " + text);
                }
            };


            webSocket = client.newWebSocket(request, webSocketListener);


            Intent intent = new Intent(ConnectWebsocket.this, MainActivity.class);
            intent.putExtra("webSocketUrl", url);
            startActivity(intent);
        }
    }
}