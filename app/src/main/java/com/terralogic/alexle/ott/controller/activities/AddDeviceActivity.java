package com.terralogic.alexle.ott.controller.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.controller.fragments.AvailableDevicesFragment;
import com.terralogic.alexle.ott.controller.fragments.ConfigDeviceFragment;
import com.terralogic.alexle.ott.model.DatabaseHandler;
import com.terralogic.alexle.ott.model.Device;
import com.terralogic.alexle.ott.model.User;
import com.terralogic.alexle.ott.service.HttpHandler;
import com.terralogic.alexle.ott.service.Service;
import com.terralogic.alexle.ott.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDeviceActivity extends AppCompatActivity implements ConfigDeviceFragment.OnWifiInfoSubmitListener,
        AvailableDevicesFragment.AddDeviceListener{
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        user = (User) getIntent().getSerializableExtra(Utils.EXTRA_USER);
        setupActionBar();
        addConfigDeviceFragment();
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

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(Utils.EXTRA_USER, user);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public void onWifiInfoSubmit(String wifiName, String wifiPassword) {
        Utils.hideKeyboard(this);
        new ConfigDeviceTask().execute(wifiName, wifiPassword, user.getTokenUser());
    }

    @Override
    public void onDeviceAdd(String chipID) {
        new AddDeviceTask().execute(chipID);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addConfigDeviceFragment() {
        ConfigDeviceFragment fragment = new ConfigDeviceFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_holder, fragment);
        transaction.commit();
    }

    private class ConfigDeviceTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(AddDeviceActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Loading...");
            dialog.setMessage("Please wait for a moment");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //TODO code tam. do api config device bi loi

            HttpHandler httpHandler = new HttpHandler(Service.URL_ESP);
            httpHandler.addParam("wifi", params[0]);
            httpHandler.addParam("pass", params[1]);
            httpHandler.addParam("userid", params[2]);
            String response = httpHandler.get();
            return response;

            /*try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "{chipID:\"10725987\"}";*/
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            dialog.dismiss();

            if (response != null) {
                try {
                    JSONObject json = new JSONObject(response);
                    String chipID = json.getString("chipID");

                    AvailableDevicesFragment fragment = AvailableDevicesFragment.newInstance(chipID);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_holder, fragment);
                    transaction.addToBackStack(null);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AddDeviceActivity.this, "Config device failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AddDeviceTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(AddDeviceActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            dialog.setTitle("Loading...");
            dialog.setMessage("Please wait for a moment");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... chipID) {
            HttpHandler httpHandler = new HttpHandler(Service.URL_API);
            httpHandler.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpHandler.addHeader("Authorization", "Bearer " + user.getToken());

            httpHandler.addParam("method", "addDevice");
            httpHandler.addParam("type", "DARK");
            httpHandler.addParam("port", "0");
            httpHandler.addParam("tokenUser", user.getTokenUser());
            httpHandler.addParam("chipID", chipID[0]);
            httpHandler.addParam("name", "Real device");
            return httpHandler.post();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            dialog.dismiss();
            Toast.makeText(AddDeviceActivity.this, HttpHandler.getMessage(response), Toast.LENGTH_SHORT).show();

            if (HttpHandler.isSuccessful(response)) {
                try {
                    JSONObject json = new JSONObject(response);
                    Device device = new Device(json.optJSONObject("data"));
                    user.getDevices().add(device);
                    DatabaseHandler db = DatabaseHandler.getInstance(AddDeviceActivity.this);
                    db.addDevice(device);
                    finish();
                } catch (JSONException e) {
                    Log.e(this.getClass().getSimpleName(), "JSON mapping error!");
                }
            }
        }
    }
}
