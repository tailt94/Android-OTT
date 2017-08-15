package com.terralogic.alexle.ott.controller.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.controller.activities.AddDeviceActivity;
import com.terralogic.alexle.ott.controller.adapters.AvailableDevicesAdapter;
import com.terralogic.alexle.ott.controller.adapters.ConnectedDevicesAdapter;
import com.terralogic.alexle.ott.model.Device;
import com.terralogic.alexle.ott.model.User;
import com.terralogic.alexle.ott.service.Service;
import com.terralogic.alexle.ott.service.WebSocketHandler;
import com.terralogic.alexle.ott.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment implements ConnectedDevicesAdapter.DeviceStatusChangeListener {
    private static final int ADD_DEVICE_REQUEST = 1;
    private RecyclerView recyclerView;
    private ConnectedDevicesAdapter adapter;
    private FloatingActionButton fab;

    private WebSocketHandler wsHandler;
    private User user;

    public DevicesFragment() {
        // Required empty public constructor
    }

    public static DevicesFragment newInstance(User user) {
        DevicesFragment fragment = new DevicesFragment();
        Bundle args = new Bundle();
        args.putSerializable(Utils.ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(Utils.ARG_USER);
        }
        wsHandler = new WebSocketHandler(Service.URL_SOCKET, new ConnectWebSocketListener());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupRecyclerView();
        setupListeners();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_DEVICE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                user = (User) data.getSerializableExtra(Utils.EXTRA_USER);
                adapter.setDevices(user.getDevices());
            }
        }
    }

    @Override
    public void onStatusChange(int position, int status) {
        //TODO gui JSON qua websocket
        String msg = new StringBuilder()
                .append("{")
                .append("\"status\": ").append(status).append(",")
                .append("\"token\": \"").append(user.getDevices().get(position).getToken()).append("\"").append(",")
                .append("\"event\": \"changeStatus\"")
                .append("}").toString();
        wsHandler.send(msg);
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.list_connected_devices);
        fab = view.findViewById(R.id.fab);
    }

    private void setupRecyclerView() {
        adapter = new ConnectedDevicesAdapter(getActivity(), user.getDevices());
        adapter.setDeviceStatusChangeListener(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ConnectedDevicesAdapter.GridSpacingItemDecoration(2, 15, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                intent.putExtra(Utils.EXTRA_USER, user);
                startActivityForResult(intent, ADD_DEVICE_REQUEST);
            }
        });
    }

    private class ConnectWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            String msg = new StringBuilder()
                    .append("{")
                    .append("\"event\": \"register\",")
                    .append("\"tokenuser\": \"").append(user.getTokenUser()).append("\"")
                    .append("}").toString();
            webSocket.send(msg);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            if (Utils.isValidJSON(text)) {
                try {
                    List<Device> devices = new ArrayList<>();
                    JSONObject json = new JSONObject(text);
                    JSONArray deviceArray = json.optJSONArray("data");
                    if (deviceArray != null) {
                        for (int i = 0; i < deviceArray.length(); i++) {
                            devices.add(new Device(deviceArray.optJSONObject(i)));
                        }
                    }
                    user.setDevices(devices);
                    adapter.setDevices(devices);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            showMessage("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            showMessage("Error: " + t.getMessage());
        }

        private void showMessage(final String msg) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
