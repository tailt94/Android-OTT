package com.terralogic.alexle.ott.controller;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.model.User;
import com.terralogic.alexle.ott.service.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends PostActivity {
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private EditText inputFirstName;
    private EditText inputLastName;
    private RadioGroup groupGender;
    private TextView birthday;
    private EditText inputCity;
    private EditText inputCountry;
    private ViewGroup buttonRegister;
    private boolean isBirthdaySet = false;
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    isBirthdaySet = true;
                    showDate(year, month + 1, day);
                }
            };

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindViews();
        setupListeners();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, 1994, 6, 20);
        }
        return null;
    }

    @Override
    protected void addPostParams() {
        postParams.put("method", "register");
        if (!TextUtils.isEmpty(inputEmail.getText())) {
            postParams.put("email", inputEmail.getText().toString());
        }
        if (!TextUtils.isEmpty(inputPassword.getText())) {
            postParams.put("password", inputPassword.getText().toString());
        }
        if (!TextUtils.isEmpty(inputFirstName.getText())) {
            postParams.put("firstname", inputFirstName.getText().toString());
        }
        if (!TextUtils.isEmpty(inputLastName.getText())) {
            postParams.put("lastname", inputLastName.getText().toString());
        }
        if (!TextUtils.isEmpty(inputCity.getText())) {
            postParams.put("city", inputCity.getText().toString());
        }
        if (!TextUtils.isEmpty(inputCountry.getText())) {
            postParams.put("country", inputCountry.getText().toString());
        }
        String selectedGender = ((RadioButton) findViewById(groupGender.getCheckedRadioButtonId())).getText().toString().toLowerCase();
        postParams.put("sex", selectedGender);
        if (isBirthdaySet) {
            postParams.put("birthday", birthday.getText().toString());
        }
    }

    private void bindViews() {
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        inputFirstName = (EditText) findViewById(R.id.input_first_name);
        inputLastName = (EditText) findViewById(R.id.input_last_name);
        groupGender = (RadioGroup) findViewById(R.id.group_sex);
        birthday = (TextView) findViewById(R.id.input_birthday);
        inputCity = (EditText) findViewById(R.id.input_city);
        inputCountry = (EditText) findViewById(R.id.input_country);
        buttonRegister = (ViewGroup) findViewById(R.id.btn_register);
    }

    private void setupListeners() {
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInfo()) {
                    addPostParams();
                    new PostTask().execute(postParams);
                } else {
                    Toast.makeText(RegisterActivity.this, "INVALID", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showDatePicker() {
        showDialog(999);
    }

    /**
     * Display selected birthday
     */
    private void showDate(int year, int month, int day) {
        birthday.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    /**
     * Check if the register information is valid
     */
    private boolean isValidInfo() {
        return (isRequiredFieldFilled() && isValidEmail() && isValidPassword());
    }

    private boolean isValidEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText()).matches();
    }

    private boolean isValidPassword() {
        return (inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString()));
    }

    private boolean isRequiredFieldFilled() {
        return (!TextUtils.isEmpty(inputEmail.getText()) && !TextUtils.isEmpty(inputPassword.getText())
                    && !TextUtils.isEmpty(inputConfirmPassword.getText()));
    }
}
