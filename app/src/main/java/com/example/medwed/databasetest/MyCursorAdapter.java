package com.example.medwed.databasetest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by medwed on 9/15/2015.
 */
public class MyCursorAdapter extends CursorAdapter {
    private static final int s = 0;
    private int layout;
    private String PostNumber;
    LayoutInflater inflater;

    private int nameCol;
    private int surNameCol;


    public MyCursorAdapter(Context context, int layout, Cursor c, String[] from,
                           int[] to, int flags) {
        super(context, c, flags);
        this.layout = layout;

    }

    @Override
    public View newView(Context context, final Cursor c, ViewGroup parent) {

        ViewHolder holder;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.trainee_row, null);

        holder = new ViewHolder();

        holder.listName = (TextView) row.findViewById(R.id.list_name);
        holder.listSurName = (TextView) row.findViewById(R.id.list_surname);
        holder.checkBoxSelect = (CheckBox) row.findViewById(R.id.checkbox_select);

        row.setTag(holder);

        nameCol = c.getColumnIndex(TraineeTable.NAME);
        String name = c.getString(nameCol);

        surNameCol = c.getColumnIndex(TraineeTable.SURNAME);
        String surName = c.getString(surNameCol);

        holder.listName.setText(name);
        holder.listSurName.setText(surName);
        holder.checkBoxSelect.setChecked(true);

        holder.checkBoxSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cBox = (CheckBox) v;
                if (cBox.isChecked()) {

                    // todo save data to db

                } else if (!cBox.isChecked()) {
                    // todo delete

                }

            }
        });
        return row;

    }

    ;

    @Override
    public void bindView(View row, Context context, final Cursor c) {

        ViewHolder holder;
        holder = (ViewHolder) row.getTag();


        String name = c.getString(nameCol);

        String surName = c.getString(surNameCol);

        holder.listName.setText(name);
        holder.listSurName.setText(surName);

        holder.checkBoxSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cBox = (CheckBox) v;
                if (cBox.isChecked()) {

                    //mDbHelper.UpdatePostImage(FileName, PostNumber);

                } else if (!cBox.isChecked()) {
                    //mDbHelper.UpdatePostImage(FileName, "");

                }

            }
        });

    }

    ;


    static class ViewHolder {
        CheckBox checkBoxSelect;
        TextView listName;
        TextView listSurName;
    }
}