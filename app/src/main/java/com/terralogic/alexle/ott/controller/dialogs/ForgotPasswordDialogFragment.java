package com.terralogic.alexle.ott.controller.dialogs;


import android.app.Activity;
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

import com.terralogic.alexle.ott.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordDialogFragment extends DialogFragment {
    private EditText inputEmail;
    private ViewGroup btnSend;

    private ForgotPasswordDialogListener mListener;

    public ForgotPasswordDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forgot_password, null);
        bindViews(view);
        setupListeners();
        builder.setView(view);
        return builder.create();
    }

    private void bindViews(View view) {
        inputEmail = view.findViewById(R.id.input_email);
        btnSend = view.findViewById(R.id.btn_send);
    }

    private void setupListeners() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogSendEmail(inputEmail.getText().toString());
                dismiss();
            }
        });
    }

    public void setForgotPasswordDialogListener(Activity parentActivity) {
        try {
            mListener = (ForgotPasswordDialogListener) parentActivity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(parentActivity.toString()
                    + " must implement ForgotPasswordDialogListener");
        }
    }

    public interface ForgotPasswordDialogListener {
        void onDialogSendEmail(String email);
    }
}
