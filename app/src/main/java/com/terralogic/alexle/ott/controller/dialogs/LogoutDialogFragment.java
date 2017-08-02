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

import com.terralogic.alexle.ott.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutDialogFragment extends DialogFragment {
    private ViewGroup btnNo;
    private ViewGroup btnYes;
    private LogoutDialogListener mListener;

    public LogoutDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_logout, null);
        bindViews(view);
        setupListeners();
        builder.setView(view);
        return builder.create();
    }

    private void bindViews(View view) {
        btnNo = view.findViewById(R.id.btn_no);
        btnYes = view.findViewById(R.id.btn_yes);
    }

    private void setupListeners() {
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogLogoutConfirm();
                dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setLogoutDialogListener(Fragment parentFragment) {
        try {
            mListener = (LogoutDialogListener) parentFragment;
        } catch (ClassCastException ex) {
            throw new ClassCastException(parentFragment.toString()
                    + " must implement LogoutDialogListener");
        }
    }

    public interface LogoutDialogListener {
        void onDialogLogoutConfirm();
    }
}
