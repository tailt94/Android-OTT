package com.terralogic.alexle.ott.controller.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.terralogic.alexle.ott.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterSuccessDialogFragment extends DialogFragment {
    private static final String ARGS_EMAIL = "email";

    private TextView textInfo;
    private ViewGroup btnOK;

    private String email;
    private RegisterSuccessListener mListener;

    public RegisterSuccessDialogFragment() {
        // Required empty public constructor
    }

    public static RegisterSuccessDialogFragment newInstance(String email) {
        RegisterSuccessDialogFragment instance = new RegisterSuccessDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_EMAIL, email);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (RegisterSuccessListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString()
                    + " must implement RegisterSuccessListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARGS_EMAIL);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_register_success, null);
        bindViews(view);
        setupListeners();
        builder.setView(view);
        return builder.create();
    }

    private void bindViews(View view) {
        textInfo = view.findViewById(R.id.tv_info);
        String info = "Success, please check your email " + email + " to activate account";
        textInfo.setText(info);
        btnOK = view.findViewById(R.id.btn_ok);
    }

    private void setupListeners() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogRegisterSuccess();
                dismiss();
            }
        });
    }

    public interface RegisterSuccessListener {
        void onDialogRegisterSuccess();
    }
}
