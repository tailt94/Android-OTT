package com.terralogic.alexle.ott.controller;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordDialogFragment extends DialogFragment {
    public static final String BUNDLE_OLD_PASSWORD = "old_password";
    public static final String BUNDLE_NEW_PASSWORD = "new_password";

    private EditText inputOldPassword;
    private EditText inputNewPassword;
    private EditText inputConfirmPassword;
    private ViewGroup buttonChangePassword;
    private ViewGroup buttonCancel;

    private String messageError = "";
    private ChangePasswordDialogListener mListener;

    public ChangePasswordDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_change_password_dialog, null);
        bindViews(view);
        setupListeners();
        dialogBuilder.setView(view);
        return dialogBuilder.create();
    }

    private void bindViews(View view) {
        inputOldPassword = (EditText) view.findViewById(R.id.input_old_password);
        inputNewPassword = (EditText) view.findViewById(R.id.input_new_password);
        inputConfirmPassword = (EditText) view.findViewById(R.id.input_confirm_password);
        buttonChangePassword = (ViewGroup) view.findViewById(R.id.btn_change_password);
        buttonCancel = (ViewGroup) view.findViewById(R.id.btn_cancel);
    }

    private void setupListeners() {
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidPasswordChange()) {
                    Bundle passwords = new Bundle();
                    passwords.putString(BUNDLE_OLD_PASSWORD, inputOldPassword.getText().toString());
                    passwords.putString(BUNDLE_NEW_PASSWORD, inputNewPassword.getText().toString());
                    mListener.onPasswordChange(passwords);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), messageError, Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private boolean isValidPasswordChange() {
        if (!isRequiredFieldsFilled()) {
            messageError = "Passwords must not be empty";
        }
        if (!isValidNewPassword()) {
            messageError = "Please confirm your new password again";
        }
        return (isValidNewPassword() && isRequiredFieldsFilled());
    }

    private boolean isValidNewPassword() {
        return (inputNewPassword.getText().toString().equals(inputConfirmPassword.getText().toString()));
    }

    private boolean isRequiredFieldsFilled() {
        return (!TextUtils.isEmpty(inputOldPassword.getText()) && !TextUtils.isEmpty(inputNewPassword.getText())
                && !TextUtils.isEmpty(inputConfirmPassword.getText()));
    }

    public void setChangePasswordDialogListener(Fragment parentFragment) {
        try {
            mListener = (ChangePasswordDialogListener) parentFragment;
        } catch (ClassCastException ex) {
            throw new ClassCastException(parentFragment.toString()
                    + " must implement ChangePasswordDialogListener");
        }
    }

    interface ChangePasswordDialogListener {
        void onPasswordChange(Bundle passwords);
        void onDialogCancel();
    }
}
