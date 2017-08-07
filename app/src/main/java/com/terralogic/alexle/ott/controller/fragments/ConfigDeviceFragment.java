package com.terralogic.alexle.ott.controller.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.controller.activities.AddDeviceActivity;
import com.terralogic.alexle.ott.service.HttpHandler;
import com.terralogic.alexle.ott.service.Service;
import com.terralogic.alexle.ott.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigDeviceFragment extends Fragment {
    private EditText inputWifiName;
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
        btnSwitchWifi = view.findViewById(R.id.switch_wifi);
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
                if (Utils.isRequiredFieldsFilled(inputWifiName, inputWifiPassword)) {
                    listener.onWifiInfoSubmit(inputWifiName.getText().toString(), inputWifiPassword.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Wi-Fi name and password must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface OnWifiInfoSubmitListener {
        void onWifiInfoSubmit(String wifiName, String wifiPassword);
    }
}
