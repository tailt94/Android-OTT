package com.terralogic.alexle.ott.controller.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.controller.activities.AddDeviceActivity;
import com.terralogic.alexle.ott.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment {
    private static final String ARG_USER = "user";

    private FloatingActionButton fab;

    private User user;

    public DevicesFragment() {
        // Required empty public constructor
    }

    public static DevicesFragment newInstance(User user) {
        DevicesFragment fragment = new DevicesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupListeners();
    }

    private void bindViews(View view) {
        fab = view.findViewById(R.id.fab);
    }

    private void setupListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
                intent.putExtra(ARG_USER, user);
                startActivity(intent);
            }
        });
    }
}
