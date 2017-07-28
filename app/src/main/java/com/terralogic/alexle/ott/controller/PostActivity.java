package com.terralogic.alexle.ott.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.terralogic.alexle.ott.model.DatabaseHandler;
import com.terralogic.alexle.ott.model.User;
import com.terralogic.alexle.ott.service.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class PostActivity extends AppCompatActivity {
    protected HashMap<String, String> postParams = new HashMap<>();
    protected User user;
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
    protected abstract void onPostFailed();

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
        protected void onPostExecute(String s) {
            if (taskSuccess(s)) {
                try {
                    JSONObject json = new JSONObject(s);
                    user = new User(json.optJSONObject("data"));
                    onPostDone();
                } catch (JSONException ex) {
                    Log.e(this.getClass().getSimpleName(), "JSON mapping error!");
                }
            } else {
                onPostFailed();
            }
        }

        /**
         * Check task state
         */
        private boolean taskSuccess(String response) {
            if (response == null) {
                return false;
            }
            try {
                JSONObject json = new JSONObject(response);
                String message = json.optString("message");
                if (message.equals("Password is wrong, please try again")
                        || message.equals("Account doesn't exist")
                        || message.equals("Account already exist !!!")
                        || message.equals("Unauthorized")) {
                    return false;
                }
            } catch (JSONException ex) {
                Log.e(this.getClass().getSimpleName(), "JSON mapping error!");
            }
            return true;
        }
    }

    protected class SaveUserTask extends AsyncTask<User, Void, User> {
        @Override
        protected User doInBackground(User... users) {
            DatabaseHandler db = DatabaseHandler.getInstance(PostActivity.this);
            db.addUser(users[0]);
            return users[0];
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            Intent intent = new Intent(PostActivity.this, HomeActivity.class);
            intent.putExtra(SplashActivity.EXTRA_USER, user);
            startActivity(intent);
            finish();
        }
    }
}
