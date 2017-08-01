package com.terralogic.alexle.ott.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.controller.dialogs.DatePickerDialogFragment;

public class RegisterActivity extends PostActivity implements DatePickerDialogFragment.OnDateChangeListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindViews();
        setupListeners();
    }

    @Override
    public void onDateChange(int year, int month, int day) {
        isBirthdaySet = true;
        birthday.setText(new StringBuilder().append(month + 1).append("/")
                .append(day).append("/").append(year));
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

    @Override
    protected void onPostDone() {
        Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostFailed() {
        Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT).show();
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
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.setDateChangeListener(RegisterActivity.this);
                datePicker.show(getSupportFragmentManager(), "DatePickerDialogFragment");
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInfo()) {
                    addPostParams();
                    new PostRequestTask().execute(postParams);
                } else {
                    Toast.makeText(RegisterActivity.this, messageError, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Check if the register information is valid
     */
    private boolean isValidInfo() {
        if (!isValidPassword()) {
            messageError = "Passwords must match";
        }
        if (!isValidEmail()) {
            messageError = "Invalid email";
        }
        if (!isRequiredFieldFilled()) {
            messageError = "Email and password must be filled";
        }
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
