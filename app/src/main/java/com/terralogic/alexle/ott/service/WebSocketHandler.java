package com.terralogic.alexle.ott.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by alex.le on 14-Aug-17.
 */

public class WebSocketHandler {
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    private static OkHttpClient client;
    private Request request;
    private WebSocket webSocket;
    private WebSocketListener wsListener;

    public WebSocketHandler(String url) {
        if (client == null) {
            client = new OkHttpClient();
        }
        request = new Request.Builder()
                .url(url)
                .build();
        wsListener = null;
        webSocket = null;
    }

    public WebSocketHandler(String url, WebSocketListener listener) {
        if (client == null) {
            client = new OkHttpClient();
        }
        request = new Request.Builder()
                .url(url)
                .build();
        wsListener = listener;
        webSocket = client.newWebSocket(request, wsListener);
    }

    public void setWebSocketListener(WebSocketListener listener) {
        wsListener = listener;
        webSocket = client.newWebSocket(request, wsListener);
    }

    public boolean send(String text) {
        return webSocket.send(text);
    }

    public void close() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }
}
