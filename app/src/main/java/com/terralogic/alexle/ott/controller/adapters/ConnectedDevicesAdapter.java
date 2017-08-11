package com.terralogic.alexle.ott.controller.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.terralogic.alexle.ott.R;
import com.terralogic.alexle.ott.model.Device;

import java.util.List;

/**
 * Created by alex.le on 11-Aug-17.
 */

public class ConnectedDevicesAdapter extends RecyclerView.Adapter<ConnectedDevicesAdapter.ConnectedDeviceViewHolder> {
    Context context;
    List<Device> devices;

    public ConnectedDevicesAdapter(Context context, List<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    @Override
    public ConnectedDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_connected_device, parent, false);
        return new ConnectedDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConnectedDeviceViewHolder holder, int position) {
        Device device = devices.get(position);
        int status = device.getStatus();

        if (status == 0) {
            holder.deviceImage.setImageResource(R.drawable.light_off);
            holder.deviceStatus.setText("Off");
        } else if (status == 1) {
            holder.deviceImage.setImageResource(R.drawable.light_on);
            holder.deviceStatus.setText("On");
        }
        holder.deviceName.setText(device.getName());

        holder.deviceImage.setOnClickListener(new ConnectedDeviceClickListener(holder, position));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    private class ConnectedDeviceClickListener implements View.OnClickListener {
        private ConnectedDeviceViewHolder holder;
        private int position;

        public ConnectedDeviceClickListener(ConnectedDeviceViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            Log.i("SDDDD", "DDDD");
            Device device = devices.get(position);
            int status = device.getStatus();
            if (status == 0) {
                device.setStatus(1);
                holder.deviceImage.setImageResource(R.drawable.light_on);
                holder.deviceStatus.setText("On");
            } else if (status == 1) {
                device.setStatus(0);
                holder.deviceImage.setImageResource(R.drawable.light_off);
                holder.deviceStatus.setText("Off");
            }
        }
    }

    public class ConnectedDeviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView deviceImage;
        private TextView deviceName;
        private TextView deviceStatus;

        public ConnectedDeviceViewHolder(View itemView) {
            super(itemView);

            deviceImage = itemView.findViewById(R.id.device_image);
            deviceName = itemView.findViewById(R.id.device_name);
            deviceStatus = itemView.findViewById(R.id.device_status);
        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
