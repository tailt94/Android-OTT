package com.terralogic.alexle.ott.controller;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by alex.le on 28-Jul-17.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private OnDateChangeListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mListener.onDateChange(year, month, day);
    }

    public void setDateChangeListener(Fragment parentFragment) {
        try {
            mListener = (OnDateChangeListener) parentFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(parentFragment.toString()
                    + " must implement OnDateChangeListener");
        }
    }

    public void setDateChangeListener(Activity parentActivity) {
        try {
            mListener = (OnDateChangeListener) parentActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(parentActivity.toString()
                    + " must implement OnDateChangeListener");
        }
    }

    interface OnDateChangeListener {
        void onDateChange(int year, int month, int day);
    }
}
