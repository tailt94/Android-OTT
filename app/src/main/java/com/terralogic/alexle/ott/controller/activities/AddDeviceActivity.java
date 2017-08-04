package com.terralogic.alexle.ott.controller.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.terralogic.alexle.ott.R;

public class AddDeviceActivity extends AppCompatActivity {
    private EditText inputWifiName;
    private EditText inputWifiPassword;
    private TextView btnSwitchWifi;
    private ViewGroup btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        setupActionBar();
        bindViews();
        setupListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void bindViews() {
        inputWifiName = (EditText) findViewById(R.id.input_wifi_name);
        inputWifiPassword = (EditText) findViewById(R.id.input_wifi_password);
        btnSwitchWifi = (TextView) findViewById(R.id.switch_wifi);
        btnSubmit = (ViewGroup) findViewById(R.id.btn_submit);
    }

    private void setupListeners() {
        btnSwitchWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
