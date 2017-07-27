package com.terralogic.alexle.ott.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.model.User;

public class SettingsFragment extends Fragment {
    private static final String ARG_USER = "user";

    private TextView tvUsername;
    private TextView tvFullname;
    private TextView tvGender;
    private TextView tvBirthday;
    private ImageView btnEditAccount;

    private User user;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(User user) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        showUserInfo();
        setupListeners();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void bindViews(View view) {
        tvUsername = (TextView) view.findViewById(R.id.text_username);
        tvFullname = (TextView) view.findViewById(R.id.text_full_name);
        tvGender = (TextView) view.findViewById(R.id.text_gender);
        tvBirthday = (TextView) view.findViewById(R.id.text_birthday);
        btnEditAccount = (ImageView) view.findViewById(R.id.btn_edit);
    }

    private void showUserInfo() {
        tvUsername.setText(user.getEmail());
        tvFullname.setText(user.getName().toString());
        tvGender.setText(user.getSex());
        tvBirthday.setText(user.getBirthday());
    }

    private void setupListeners() {
        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditAccountDialog();
            }
        });
    }

    private void showEditAccountDialog() {
        EditAccountDialogFragment dialog = EditAccountDialogFragment.newInstance(user);
        dialog.show(getFragmentManager(), "EditAccountDialogFragment");
    }

}
