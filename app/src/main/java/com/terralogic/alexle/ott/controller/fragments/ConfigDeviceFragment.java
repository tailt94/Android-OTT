package com.terralogic.alexle.ott.controller.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigDeviceFragment extends Fragment {
    private Spinner inputWifiName;
    private EditText inputWifiPassword;
    private TextView btnSwitchWifi;
    private ViewGroup btnSubmit;

    private OnWifiInfoSubmitListener listener;

    public ConfigDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnWifiInfoSubmitListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString()
                    + " must implement OnWifiInfoSubmitListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config_device, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindViews(view);
        setupListeners();
        prepareWifiNameAdapter();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void bindViews(View view) {
        inputWifiName = view.findViewById(R.id.input_wifi_name);
        inputWifiPassword = view.findViewById(R.id.input_wifi_password);
        btnSwitchWifi = view.findViewById(R.id.btn_switch_wifi);
        btnSubmit = view.findViewById(R.id.btn_submit);
    }

    private void setupListeners() {
        btnSwitchWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputWifiName.getSelectedItem() != null
                        && !TextUtils.isEmpty(inputWifiPassword.getText())) {
                    listener.onWifiInfoSubmit(inputWifiName.getSelectedItem().toString(), inputWifiPassword.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Wi-Fi name and password must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void prepareWifiNameAdapter() {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        List<ScanResult> results = wifiManager.getScanResults();
        List<String> ssids = new ArrayList<>();
        for (ScanResult result : results) {
            ssids.add(result.SSID);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, ssids);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputWifiName.setAdapter(adapter);
    }

    public interface OnWifiInfoSubmitListener {
        void onWifiInfoSubmit(String wifiName, String wifiPassword);
    }
}
