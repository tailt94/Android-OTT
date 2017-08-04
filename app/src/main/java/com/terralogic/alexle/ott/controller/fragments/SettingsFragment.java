package com.terralogic.alexle.ott.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.controller.activities.LoginActivity;
import com.terralogic.alexle.ott.controller.dialogs.ChangePasswordDialogFragment;
import com.terralogic.alexle.ott.controller.dialogs.EditAccountDialogFragment;
import com.terralogic.alexle.ott.controller.dialogs.LogoutDialogFragment;
import com.terralogic.alexle.ott.model.DatabaseHandler;
import com.terralogic.alexle.ott.model.User;
import com.terralogic.alexle.ott.service.HttpHandler;
import com.terralogic.alexle.ott.service.Service;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment implements EditAccountDialogFragment.EditDialogListener,
        ChangePasswordDialogFragment.ChangePasswordDialogListener, LogoutDialogFragment.LogoutDialogListener {
    private static final String ARG_USER = "user";

    private TextView tvUsername;
    private TextView tvFullname;
    private TextView tvGender;
    private TextView tvBirthday;
    private ImageView btnEditAccount;
    private ImageView btnChangePassword;
    private ViewGroup btnLogout;

    private User user;
    private HashMap<String, String> postParams = new HashMap<>();

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

    @Override
    public void onDialogAccountUpdate(User user) {
        addEditAccountParams();
        new PostNewUserInfoTask().execute(user);
    }

    @Override
    public void onDialogPasswordChange(String oldPassword, String newPassword) {
        addChangePasswordParams(oldPassword, newPassword);
        new ChangePasswordTask().execute(postParams);
    }

    @Override
    public void onDialogLogoutConfirm() {
        new LogoutTask().execute(user);
    }

    @Override
    public void onDialogCancel() {

    }

    private void bindViews(View view) {
        tvUsername = (TextView) view.findViewById(R.id.text_username);
        tvFullname = (TextView) view.findViewById(R.id.text_full_name);
        tvGender = (TextView) view.findViewById(R.id.text_gender);
        tvBirthday = (TextView) view.findViewById(R.id.text_birthday);
        btnEditAccount = (ImageView) view.findViewById(R.id.btn_edit_account);
        btnChangePassword = (ImageView) view.findViewById(R.id.btn_change_password);
        btnLogout = (ViewGroup) view.findViewById(R.id.btn_logout);
    }

    private void addEditAccountParams() {
        postParams.clear();
        postParams.put("method", "editUser");
        postParams.put("firstname", user.getName().getFirstName());
        postParams.put("lastname", user.getName().getLastName());
        postParams.put("sex", user.getSex());
        postParams.put("birthday", user.getBirthday());
        postParams.put("country", user.getCountry());
        postParams.put("city", user.getCity());
        postParams.put("tokenUser", user.getTokenUser());
    }

    private void addChangePasswordParams(String oldPassword, String newPassword) {
        postParams.clear();
        postParams.put("method", "changePassword");
        postParams.put("email", user.getEmail());
        postParams.put("oldpassword", oldPassword);
        postParams.put("newpassword", newPassword);
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
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();
            }
        });
    }

    private void showEditAccountDialog() {
        EditAccountDialogFragment dialog = EditAccountDialogFragment.newInstance(user);
        dialog.setEditDialogListener(this);
        dialog.show(getFragmentManager(), "EditAccountDialogFragment");
    }

    private void showChangePasswordDialog() {
        ChangePasswordDialogFragment dialog = new ChangePasswordDialogFragment();
        dialog.setChangePasswordDialogListener(this);
        dialog.show(getFragmentManager(), "ChangePasswordDialogFragment");
    }

    private void showLogoutDialog() {
        LogoutDialogFragment dialog = new LogoutDialogFragment();
        dialog.setLogoutDialogListener(this);
        dialog.show(getFragmentManager(), "LogoutDialogFragment");
    }

    private class LogoutTask extends AsyncTask<User, Void, String> {
        @Override
        protected String doInBackground(User... users) {
            HttpHandler httpHandler = new HttpHandler(Service.URL_API);
            httpHandler.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpHandler.addHeader("Authorization", "Bearer " + users[0].getToken());
            httpHandler.addParam("method", "logout");
            httpHandler.addParam("email", users[0].getEmail());
            return httpHandler.makeServiceCall();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            DatabaseHandler db = DatabaseHandler.getInstance(getActivity());
            db.clearDatabase();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private class ChangePasswordTask extends AsyncTask<HashMap<String, String>, Void, String> {
        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            HttpHandler httpHandler = new HttpHandler(Service.URL_API);
            httpHandler.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpHandler.addHeader("Authorization", "Bearer " + user.getToken());
            for (Map.Entry<String, String> entry : params[0].entrySet()) {
                httpHandler.addParam(entry.getKey(), entry.getValue());
            }
            return httpHandler.makeServiceCall();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Toast.makeText(getActivity(), HttpHandler.getMessage(response), Toast.LENGTH_SHORT).show();
            if (HttpHandler.isSuccessful(response)) {
                new LogoutTask().execute(user);
            }
        }
    }

    private class PostNewUserInfoTask extends AsyncTask<User, Void, String> {
        private User requestUser;

        @Override
        protected String doInBackground(User... users) {
            requestUser = users[0];
            HttpHandler httpHandler = new HttpHandler(Service.URL_API);
            httpHandler.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpHandler.addHeader("Authorization", "Bearer " + requestUser.getToken());
            for (Map.Entry<String, String> entry : postParams.entrySet()) {
                httpHandler.addParam(entry.getKey(), entry.getValue());
            }
            return httpHandler.makeServiceCall();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (HttpHandler.isSuccessful(response)) {
                SettingsFragment.this.user = requestUser;
                showUserInfo();
                new UpdateUserTask().execute(requestUser);
            } else {
                Toast.makeText(getActivity(), HttpHandler.getMessage(response), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateUserTask extends AsyncTask<User, Void, Integer> {
        @Override
        protected Integer doInBackground(User... users) {
            DatabaseHandler db = DatabaseHandler.getInstance(getActivity());
            return db.updateUser(user);
        }

        @Override
        protected void onPostExecute(Integer affectedRowsCount) {
            super.onPostExecute(affectedRowsCount);
            if (affectedRowsCount == 1) {
                Toast.makeText(getActivity(), "Edit user success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Edit user failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
