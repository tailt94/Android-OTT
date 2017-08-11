package com.terralogic.alexle.ott.controller.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.model.Device;

import java.util.List;

/**
 * Created by alex.le on 08-Aug-17.
 */

public class AvailableDevicesAdapter extends RecyclerView.Adapter<AvailableDevicesAdapter.AvailableDeviceViewHolder>{
    private List<Device> devices;

    public AvailableDevicesAdapter(List<Device> devices) {
        this.devices = devices;
    }
    @Override
    public AvailableDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_available_device, parent, false);
        return new AvailableDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AvailableDeviceViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class AvailableDeviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView deviceImage;
        private TextView deviceName;
        private CheckBox deviceCheckbox;

        public AvailableDeviceViewHolder(View itemView) {
            super(itemView);
            deviceImage = itemView.findViewById(R.id.device_image);
            deviceName = itemView.findViewById(R.id.device_name);
            deviceCheckbox = itemView.findViewById(R.id.device_checkbox);
        }
    }
}
