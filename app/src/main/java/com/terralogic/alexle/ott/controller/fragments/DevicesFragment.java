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

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment {
    private FloatingActionButton fab;
    public DevicesFragment() {
        // Required empty public constructor
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
                startActivity(intent);
            }
        });
    }
}
