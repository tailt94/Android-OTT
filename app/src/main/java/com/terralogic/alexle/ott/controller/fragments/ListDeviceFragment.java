package com.terralogic.alexle.ott.controller.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.controller.adapters.ListDeviceAdapter;
import com.terralogic.alexle.ott.model.Device;
import com.terralogic.alexle.ott.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDeviceFragment extends Fragment {
    private ViewGroup btnAddDevice;
    private RecyclerView recyclerView;
    private ListDeviceAdapter adapter;

    private AddDeviceListener listener;

    private String chipID;

    public ListDeviceFragment() {
        // Required empty public constructor
    }

    public static ListDeviceFragment newInstance(String chipID) {
        ListDeviceFragment fragment = new ListDeviceFragment();
        Bundle args = new Bundle();
        args.putString(Utils.ARG_CHIPID, chipID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddDeviceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AddDeviceListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chipID = getArguments().getString(Utils.ARG_CHIPID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_device, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupListeners();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.list_device);
        btnAddDevice = view.findViewById(R.id.btn_add);

        Device device = new Device();
        List<Device> devices = new ArrayList<>();
        devices.add(device);
        adapter = new ListDeviceAdapter(devices);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDeviceAdd(chipID);
                }
            }
        });
    }

    public interface AddDeviceListener {
        void onDeviceAdd(String chipID);
    }
}
