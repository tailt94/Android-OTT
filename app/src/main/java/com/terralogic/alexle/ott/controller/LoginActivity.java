package com.terralogic.alexle.ott.controller;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;

public class LoginActivity extends PostActivity {
    private ViewGroup rootView;
    private EditText inputEmail;
    private EditText inputPassword;
    private LinearLayout buttonLogin;
    private LinearLayout buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setupListeners();
        setupLayoutTransition();
    }

    @Override
    protected void addPostParams() {
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
    protected void onPostFailed() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
    }

    private void bindViews() {
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        rootView = (ViewGroup) findViewById(R.id.root);
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
                    Toast.makeText(LoginActivity.this, "Invalid info", Toast.LENGTH_SHORT).show();
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


    }

    private void setupLayoutTransition() {
        LayoutTransition layoutTransition = rootView.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
    }

    private boolean isValidInfo() {
        return (isRequiredFieldFilled() && isValidEmail());
    }

    private boolean isValidEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText()).matches();
    }

    /**
     * Check if the Email and Password field is filled
     */
    private boolean isRequiredFieldFilled() {
        return (!TextUtils.isEmpty(inputEmail.getText()) && !TextUtils.isEmpty(inputPassword.getText()));
    }
}
