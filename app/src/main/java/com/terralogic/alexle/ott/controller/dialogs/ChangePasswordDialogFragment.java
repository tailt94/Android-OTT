package com.terralogic.alexle.ott.controller.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordDialogFragment extends DialogFragment {
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
        View view = inflater.inflate(R.layout.dialog_change_password, null);
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
                    mListener.onDialogPasswordChange(inputOldPassword.getText().toString(),
                            inputNewPassword.getText().toString());
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
        if (!Utils.isRequiredFieldsFilled(inputOldPassword, inputNewPassword, inputConfirmPassword)) {
            messageError = "Passwords must not be empty";
            return false;
        }
        if (!Utils.isValidPassword(inputNewPassword, inputConfirmPassword)) {
            messageError = "Please confirm your new password again";
            return false;
        }
        return true;
    }

    public void setChangePasswordDialogListener(Fragment parentFragment) {
        try {
            mListener = (ChangePasswordDialogListener) parentFragment;
        } catch (ClassCastException ex) {
            throw new ClassCastException(parentFragment.toString()
                    + " must implement ChangePasswordDialogListener");
        }
    }

    public interface ChangePasswordDialogListener {
        void onDialogPasswordChange(String oldPassword, String newPassword);
        void onDialogCancel();
    }
}
