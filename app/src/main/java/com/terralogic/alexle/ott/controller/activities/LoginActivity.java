package com.terralogic.alexle.ott.controller.activities;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.controller.dialogs.ForgotPasswordDialogFragment;
import com.terralogic.alexle.ott.model.DatabaseHandler;
import com.terralogic.alexle.ott.model.User;
import com.terralogic.alexle.ott.service.HttpHandler;
import com.terralogic.alexle.ott.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends PostActivity implements ForgotPasswordDialogFragment.ForgotPasswordDialogListener {
    private ViewGroup rootView;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView buttonForgotPassword;
    private LinearLayout buttonLogin;
    private LinearLayout buttonRegister;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setupListeners();
        setupLayoutTransition();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void addPostParams() {
        postParams.clear();
        postParams.put("method", "login");
        if (!TextUtils.isEmpty(inputEmail.getText())) {
            postParams.put("email", inputEmail.getText().toString());
        }
        if (!TextUtils.isEmpty(inputPassword.getText())) {
            postParams.put("password", inputPassword.getText().toString());
        }
    }

    @Override
    protected void onPostDone() {
        new SaveUserTask().execute(user);
    }

    @Override
    public void onDialogSendEmail(String email) {
        if (!Utils.isValidEmail(email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
        } else {
            new ForgotPasswordTask().execute(email);
        }
    }

    private void bindViews() {
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        rootView = (ViewGroup) findViewById(R.id.root);
        buttonForgotPassword = (TextView) findViewById(R.id.btn_forgot_password);
        buttonLogin = (LinearLayout) findViewById(R.id.btn_login);
        buttonRegister = (LinearLayout) findViewById(R.id.btn_register);
    }

    private void setupListeners() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInfo()) {
                    addPostParams();
                    PostRequestTask loginTask = new PostRequestTask();
                    loginTask.execute(postParams);
                } else {
                    Toast.makeText(LoginActivity.this, messageError, Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasswordDialogFragment dialog = new ForgotPasswordDialogFragment();
                dialog.show(getSupportFragmentManager(), "ForgotPasswordDialogFragment");
            }
        });
    }

    private void setupLayoutTransition() {
        LayoutTransition layoutTransition = rootView.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
    }

    private boolean isValidInfo() {
        if (!Utils.isRequiredFieldsFilled(inputEmail, inputPassword)) {
            messageError = "Email and password must be filled";
            return false;
        }
        if (!Utils.isValidEmail(inputEmail.getText())) {
            messageError = "Invalid email";
            return false;
        }
        return true;
    }

    private class ForgotPasswordTask extends AsyncTask<String, Void, String> {
        private String requestUrl = "http://10.20.19.73/user";
        @Override
        protected String doInBackground(String... emails) {
            HttpHandler service = new HttpHandler(requestUrl);
            service.addHeader("Content-Type", "application/x-www-form-urlencoded");
            service.addParam("method", "forgotpassword");
            service.addParam("email", emails[0]);
            return service.makeServiceCall();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (HttpHandler.isSuccessful(response)) {
                Toast.makeText(LoginActivity.this, "Your new password was sent to your email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, HttpHandler.getMessage(response), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SaveUserTask extends AsyncTask<User, Void, User> {
        @Override
        protected User doInBackground(User... users) {
            DatabaseHandler db = DatabaseHandler.getInstance(LoginActivity.this);
            db.addUser(users[0]);
            return users[0];
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra(SplashActivity.EXTRA_USER, user);
            startActivity(intent);
            finish();
        }
    }
}
