package com.terralogic.alexle.ott.controller.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.terralogic.alexle.ott.model.User;
import com.terralogic.alexle.ott.service.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public abstract class PostActivity extends AppCompatActivity {
    protected HashMap<String, String> postParams = new HashMap<>();
    protected User user;
    protected String messageError = "";
    protected String requestUrl = "http://10.20.19.73/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Add params to HashMap for Post request
     */
    protected abstract void addPostParams();
    protected abstract void onPostDone();

    protected class PostRequestTask extends AsyncTask<HashMap<String, String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(PostActivity.this);
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
        protected String doInBackground(HashMap<String, String>... paramsMap) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            HttpHandler httpHandler = new HttpHandler(requestUrl);
            httpHandler.addHeader("Content-Type", "application/x-www-form-urlencoded");
            for (Map.Entry<String, String> entry : paramsMap[0].entrySet()) {
                httpHandler.addParam(entry.getKey(), entry.getValue());
            }
            return httpHandler.makeServiceCall();
        }

        @Override
        protected void onPostExecute(String response) {
            dialog.dismiss();
            if (HttpHandler.isSuccessful(response)) {
                try {
                    JSONObject json = new JSONObject(response);
                    user = new User(json.optJSONObject("data"));
                    onPostDone();
                } catch (JSONException ex) {
                    Log.e(this.getClass().getSimpleName(), "JSON mapping error!");
                }
            } else {
                Toast.makeText(PostActivity.this, HttpHandler.getMessage(response), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
