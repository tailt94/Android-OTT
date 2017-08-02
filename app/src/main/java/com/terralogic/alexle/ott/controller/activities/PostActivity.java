package com.terralogic.alexle.ott.controller.activities;

import android.os.AsyncTask;
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
        @Override
        protected String doInBackground(HashMap<String, String>... paramsMap) {
            HttpHandler httpHandler = new HttpHandler(requestUrl);
            httpHandler.addHeader("Content-Type", "application/x-www-form-urlencoded");
            for (Map.Entry<String, String> entry : paramsMap[0].entrySet()) {
                httpHandler.addParam(entry.getKey(), entry.getValue());
            }
            return httpHandler.makeServiceCall();
        }

        @Override
        protected void onPostExecute(String response) {
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
