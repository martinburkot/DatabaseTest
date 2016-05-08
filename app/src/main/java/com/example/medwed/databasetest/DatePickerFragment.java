package com.example.medwed.databasetest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by medwed on 9/13/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    MyOnDateSetListener listener;

    public interface MyOnDateSetListener {

        public void updateDate(int year, int month, int day);
        public static final String UPDATE_YEAR = "UPDATE_YEAR";
        public static final String UPDATE_MONTH = "UPDATE_MONTH";
        public static final String UPDATE_DAY = "UPDATE_DAY";
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year;
        int month;
        int day;

        Bundle arguments = getArguments();
        if (arguments == null) {

            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        else{
                year = arguments.getInt(MyOnDateSetListener.UPDATE_YEAR, 0);
                month = arguments.getInt(MyOnDateSetListener.UPDATE_MONTH, 0);
                day = arguments.getInt(MyOnDateSetListener.UPDATE_DAY, 0);
        }


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (MyOnDateSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + activity.getString(R.string.onDateListener_warning));
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        listener.updateDate(year, month, day);
    }
}
