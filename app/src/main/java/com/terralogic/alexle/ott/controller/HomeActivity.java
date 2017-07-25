package com.terralogic.alexle.ott.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.model.Device;
import com.terralogic.alexle.ott.model.User;

public class HomeActivity extends AppCompatActivity {
    private TextView textContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindViews();
        showUserInfo();
    }

    private void bindViews() {
        textContent = (TextView) findViewById(R.id.content);
    }

    private void showUserInfo() {
        User user = (User) getIntent().getSerializableExtra(SplashActivity.EXTRA_USER);
        String info = "Token user: " + user.getTokenUser() + "\n"
                + "Token: " + user.getToken() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "Password: " + user.getPassword() + "\n"
                + "Phone: " + user.getPhoneNumber() + "\n"
                + "First name: " + user.getName().getFirstName() + "\n"
                + "Last name: " + user.getName().getLastName() + "\n"
                + "Sex: " + user.getSex() + "\n"
                + "Birthday: " + user.getBirthday() + "\n"
                + "City: " + user.getCity()  + "\n"
                + "Country: " + user.getCountry() + "\n"
                + "Devices: " + "\n";
        for (Device device : user.getDevices()) {
            info += device.getTokenUser() + "\n"
                    + device.getType() + "\n"
                    + device.getPort() + "\n"
                    + device.getChipID() + "\n\n";
        }
        textContent.setText(info);
    }
}
