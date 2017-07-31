package com.terralogic.alexle.ott.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.model.DatabaseHandler;
import com.terralogic.alexle.ott.model.User;

public class SplashActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "EXTRA_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //TEST
        //deleteDatabase(DatabaseHandler.DATABASE_NAME);
        new LoadUserTask().execute();
    }

    private class LoadUserTask extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseHandler db = DatabaseHandler.getInstance(SplashActivity.this);
            User user = db.getUser();
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                intent.putExtra(EXTRA_USER, user);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
