package com.terralogic.alexle.ott.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.model.DatabaseHandler;
import com.terralogic.alexle.ott.model.User;
import com.terralogic.alexle.ott.service.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment implements EditAccountDialogFragment.EditDialogListener,
        ChangePasswordDialogFragment.ChangePasswordDialogListener{
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
    public void onDialogUpdate(User user) {
        clearPostParams();
        addEditAccountParams();
        new PostNewUserInfoTask().execute(user);
    }

    @Override
    public void onPasswordChange(String oldPassword, String newPassword) {
        clearPostParams();
        addChangePasswordParams(oldPassword, newPassword);
        new ChangePasswordTask().execute(postParams);
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
        postParams.put("method", "changePassword");
        postParams.put("email", user.getEmail());
        postParams.put("oldpassword", oldPassword);
        postParams.put("newpassword", newPassword);
    }

    private void clearPostParams() {
        postParams.clear();
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
                logout();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();
            }
        });
    }

    private void logout() {
        new LogoutTask().execute(user);
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

    private class LogoutTask extends AsyncTask<User, Void, String> {
        private String requestUrl = "http://10.20.19.73/api";
        @Override
        protected String doInBackground(User... users) {
            HttpHandler httpHandler = new HttpHandler(requestUrl);
            httpHandler.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpHandler.addHeader("Authorization", "Bearer " + users[0].getToken());
            httpHandler.addParam("method", "logout");
            httpHandler.addParam("email", users[0].getEmail());
            return httpHandler.makeServiceCall();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (taskSuccess(response)) {
                getActivity().deleteDatabase(DatabaseHandler.DATABASE_NAME);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Logout failed", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Check task state
         */
        private boolean taskSuccess(String response) {
            if (response == null) {
                return false;
            }
            try {
                JSONObject json = new JSONObject(response);
                String message = json.optString("message");
                if (message.equals("Unauthorized")) {
                    return false;
                }
            } catch (JSONException ex) {
                Log.e(this.getClass().getSimpleName(), "JSON mapping error!");
            }
            return true;
        }
    }

    private class ChangePasswordTask extends AsyncTask<HashMap<String, String>, Void, String> {
        private String requestUrl = "http://10.20.19.73/api";
        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            HttpHandler httpHandler = new HttpHandler(requestUrl);
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
            if (taskSuccess(response)) {
                Toast.makeText(getActivity(), "Change password success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Change password failed", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Check task state
         */
        private boolean taskSuccess(String response) {
            if (response == null) {
                return false;
            }
            try {
                JSONObject json = new JSONObject(response);
                String message = json.optString("message");
                if (message.equals("Unauthorized")) {
                    return false;
                }
            } catch (JSONException ex) {
                Log.e(this.getClass().getSimpleName(), "JSON mapping error!");
            }
            return true;
        }
    }

    private class PostNewUserInfoTask extends AsyncTask<User, Void, String> {
        private String requestUrl = "http://10.20.19.73/api";
        private User requestUser;

        @Override
        protected String doInBackground(User... users) {
            requestUser = users[0];
            HttpHandler httpHandler = new HttpHandler(requestUrl);
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
            if (taskSuccess(response)) {
                SettingsFragment.this.user = requestUser;
                showUserInfo();
                new UpdateUserTask().execute(requestUser);
            } else {
                Toast.makeText(getActivity(), "Send request failed", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Check task state
         */
        private boolean taskSuccess(String response) {
            if (response == null) {
                return false;
            }
            try {
                JSONObject json = new JSONObject(response);
                String message = json.optString("message");
                if (message.equals("Update successful !!!")) {
                    return true;
                }
            } catch (JSONException ex) {
                Log.e(this.getClass().getSimpleName(), "JSON mapping error!");
            }
            return false;
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
