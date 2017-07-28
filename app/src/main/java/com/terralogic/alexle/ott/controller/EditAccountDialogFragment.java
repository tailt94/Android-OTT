package com.terralogic.alexle.ott.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.model.Name;
import com.terralogic.alexle.ott.model.User;

public class EditAccountDialogFragment extends DialogFragment implements DatePickerFragment.OnDateChangeListener{
    private static final String ARG_USER = "user";

    private EditText inputFirstName;
    private EditText inputLastName;
    private RadioGroup groupGender;
    private TextView inputBirthday;
    private EditText inputCountry;
    private EditText inputCity;
    private ViewGroup btnUpdate;
    private ViewGroup btnCancel;

    private User user;
    private EditDialogListener mListener;

    public EditAccountDialogFragment() {
    }

    public static EditAccountDialogFragment newInstance(User user) {
        EditAccountDialogFragment dialog = new EditAccountDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_account, null);
        bindViews(view);
        showUserInfo();
        setupListeners();
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onDateChange(int year, int month, int day) {
        inputBirthday.setText(new StringBuilder().append(month + 1).append("/")
                .append(day).append("/").append(year));
    }

    private void bindViews(View view) {
        inputFirstName = (EditText) view.findViewById(R.id.input_first_name);
        inputLastName = (EditText) view.findViewById(R.id.input_last_name);
        groupGender = (RadioGroup) view.findViewById(R.id.group_sex);
        inputBirthday = (TextView) view.findViewById(R.id.input_birthday);
        inputCountry = (EditText) view.findViewById(R.id.input_country);
        inputCity = (EditText) view.findViewById(R.id.input_city);
        btnUpdate = (ViewGroup) view.findViewById(R.id.btn_update);
        btnCancel = (ViewGroup) view.findViewById(R.id.btn_cancel);
    }

    private void showUserInfo() {
        inputFirstName.setText(user.getName().getFirstName());
        inputLastName.setText(user.getName().getLastName());
        inputBirthday.setText(user.getBirthday());
        inputCountry.setText(user.getCountry());
        inputCity.setText(user.getCity());
        String sex = user.getSex().toLowerCase();
        switch (sex) {
            case "male":
                groupGender.check(R.id.sex_male);
                break;
            case "female":
                groupGender.check(R.id.sex_female);
                break;
            case "other":
                groupGender.check(R.id.sex_other);
                break;
        }
    }

    private void setupListeners() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
                dismiss();
                mListener.onDialogUpdate(user);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mListener.onDialogCancel();
            }
        });
        inputBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
    }

    private void updateUser() {
        user.setName(new Name(inputFirstName.getText().toString(), inputLastName.getText().toString()));
        int selectedGenderId = groupGender.getCheckedRadioButtonId();
        switch (selectedGenderId) {
            case R.id.sex_male:
                user.setSex("male");
                break;
            case R.id.sex_female:
                user.setSex("female");
                break;
            case R.id.sex_other:
                user.setSex("other");
                break;
        }
        user.setBirthday(inputBirthday.getText().toString());
        user.setCountry(inputCountry.getText().toString());
        user.setCity(inputCity.getText().toString());
    }

    private void showDatePicker() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setDateChangeListener(this);
        datePicker.show(getFragmentManager(), "DatePickerFragment");
    }

    public void setEditDialogListener(Fragment parentFragment) {
        try {
            mListener = (EditDialogListener) parentFragment;
        } catch (ClassCastException ex) {
            throw new ClassCastException(parentFragment.toString()
                    + " must implement EditDialogListener");
        }
    }

    interface EditDialogListener {
        void onDialogUpdate(User user);
        void onDialogCancel();
    }
}
