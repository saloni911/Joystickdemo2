package com.example.joystickdemo2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MainActivity extends AppCompatActivity {

    private static final OkHttpClient client = new OkHttpClient();

    private Map<String, WebSocket> webSocketMap = new HashMap<>();
    private TextView urlTextView;
    private Button P, A, X, C, G, D, S, H, h;
    private JoystickView_saloni saloni1, saloni2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        P = findViewById(R.id.P);
        A = findViewById(R.id.A);
        X = findViewById(R.id.X);
        C = findViewById(R.id.C);
        G = findViewById(R.id.G);
        D = findViewById(R.id.D);
        S = findViewById(R.id.S);
        H = findViewById(R.id.H);
        h = findViewById(R.id.h);
        urlTextView = findViewById(R.id.url);

        saloni1 = findViewById(R.id.joystick);
        saloni2 = findViewById(R.id.joystick2);

        Intent intent = getIntent();
        if (intent != null) {
            String baseUrl = intent.getStringExtra("webSocketUrl");
            Log.d("WebSocket", "WebSocket URL: " + baseUrl);
            initializeWebSockets(baseUrl);
        }
        P.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ((motionEvent.getAction())) {
                    case MotionEvent.ACTION_DOWN:{
                        sendCommand("COM","P");
                        sendCommand("COM","S");
                        break;

                    }
                    case MotionEvent.ACTION_UP:
                    {
                        sendCommand("COM","S");
                        break;
                    }
                }
                return true;
            }

        });
        A.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ((motionEvent.getAction())) {
                    case MotionEvent.ACTION_DOWN:{
                        sendCommand("COM","A");
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        sendCommand("COM","S");
                        break;
                    }
                }
                return true;
            }

        });
        X.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ((motionEvent.getAction())) {
                    case MotionEvent.ACTION_DOWN:{
                        sendCommand("COM","X");
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        sendCommand("COM","S");
                        break;
                    }
                }
                return true;
            }

        });
       C.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {
               switch ((motionEvent.getAction())) {
                   case MotionEvent.ACTION_DOWN:{
                        sendCommand("COM","C");
                        break;
                   }
                   case MotionEvent.ACTION_UP:
                   {
                      sendCommand("COM","S");
                      break;
                   }
               }
               return true;
           }

       });
        G.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ((motionEvent.getAction())) {
                    case MotionEvent.ACTION_DOWN:{
                        sendCommand("COM","G");
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        sendCommand("COM","S");
                        break;
                    }
                }
                return true;
            }

        });
        D.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ((motionEvent.getAction())) {
                    case MotionEvent.ACTION_DOWN:{
                        sendCommand("COM","D");
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        sendCommand("COM","S");
                        break;
                    }
                }
                return true;
            }

        });
        S.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ((motionEvent.getAction())) {
                    case MotionEvent.ACTION_DOWN:{
                        sendCommand("COM","S");
                        sendCommand("LOC","S");
                        sendCommand("MPU","S");
                        sendCommand("ABC","S");
                        break;

                    }
                    case MotionEvent.ACTION_UP:
                    {
                        sendCommand("COM","S");
                        break;
                    }
                }
                return true;
            }

        });
        H.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ((motionEvent.getAction())) {
                    case MotionEvent.ACTION_DOWN:{
                        sendCommand("COM","H");
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        sendCommand("COM","S");
                        break;
                    }
                }
                return true;
            }

        });
        h.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ((motionEvent.getAction())) {
                    case MotionEvent.ACTION_DOWN:{
                        sendCommand("COM","h");
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        sendCommand("COM","S");
                        break;
                    }
                }
                return true;
            }

        });

        saloni1.setOnMoveListener(new JoystickView_saloni.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                sendJoystickCommand("LOC", angle);
            }
        }, 17);

        saloni2.setOnMoveListener(new JoystickView_saloni.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                sendJoystickCommand("LOC", angle);
            }
        });
    }

    private void initializeWebSockets(String baseUrl) {
        connectWebSocket(baseUrl, "LOC");
        connectWebSocket(baseUrl, "COM");
        connectWebSocket(baseUrl, "ABC");
        connectWebSocket(baseUrl, "MPU");
    }


    private void connectWebSocket(String baseUrl, String channel) {
        try {
            if (baseUrl != null && !baseUrl.isEmpty()) {
                String url = baseUrl + "/" + channel;
                Log.d("WebSocket", "Connecting to WebSocket for channel " + channel + ": " + url);
                Request request = new Request.Builder().url(url).build();
                WebSocketListener webSocketListener = createWebSocketListener(channel);
                WebSocket webSocket = client.newWebSocket(request, webSocketListener);
                webSocketMap.put(channel, webSocket);
            } else {
                Log.e("WebSocket", "Invalid base URL: " + baseUrl);
            }
        } catch (Exception e) {
            Log.e("WebSocket", "Error connecting to WebSocket: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private WebSocketListener createWebSocketListener(final String channel) {
        return new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                handleWebSocketOpen(webSocket, channel);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                handleConnectionError(t);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                handleWebSocketMessage(webSocket, text);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                handleSocketClosure(webSocket, code, reason);
            }
        };
    }

    private void handleWebSocketOpen(WebSocket webSocket, String channel) {
        final String urlHost = webSocket.request().url().host();
        if (urlHost != null) {
            urlTextView.setText("Connected to: " + urlHost + "/" + channel + "/");
        } else {
            Log.e("WebSocket", "WebSocket URL host is null");
        }
    }

    private void handleWebSocketMessage(WebSocket webSocket, String text) {
        Log.d("WebSocket", "Received message: " + text);
        // Handle the received message as needed
    }

    private void handleConnectionError(Throwable t) {
        Log.e("WebSocket", "Connection error: " + t.getMessage());
        t.printStackTrace();
    }

    private void handleSocketClosure(WebSocket webSocket, int code, String reason) {
        Log.d("WebSocket", "Socket closed: " + code + ", " + reason);
    }

    private void sendJoystickCommand(String channel, int angle)
    {
        WebSocket webSocket = webSocketMap.get(channel);
        if (webSocket != null) {
            String direction = getDirection(angle);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put( channel,direction);
               // jsonObject.put("direction", );
                urlTextView.setText(direction);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String jsonString = jsonObject.toString();
            Log.d("WebSocket", "Sending JSON: " + jsonString);
            sendMessageToWebSocket(channel, jsonString);
        } else {
            Log.e("WebSocket", "WebSocket is null for channel: " + channel);
        }

    }

    private void sendCommand(String channel, String command) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(channel,command);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String jsonString =jsonObject.toString(); // "{\"" + channel +"\":\"" + command +"\"}";
        sendMessageToWebSocket(channel, jsonString);
        urlTextView.setText(command);
    }

    private void sendMessageToWebSocket(String channel, String jsonString) {
        WebSocket webSocket = webSocketMap.get(channel);
        try {
            if (webSocket != null) {
                Log.d("WebSocket", "Sending message: " + jsonString);
                webSocket.send(jsonString);
            } else {
                Log.e("WebSocket", "WebSocket is null for channel: " + channel);
            }
        } catch (Exception e) {
            Log.e("WebSocket", "Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getDirection(int angle) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (angle >= 45 && angle < 135) {
                jsonObject.put("direction", "F");
            } else if (angle >= 225 && angle < 315) {
                jsonObject.put("direction", "B");
            } else if (angle >= 135 && angle < 225) {
                jsonObject.put("direction", "L");
            } else {
                jsonObject.put("direction", "R");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
