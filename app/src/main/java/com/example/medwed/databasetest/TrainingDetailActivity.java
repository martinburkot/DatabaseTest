package com.example.medwed.databasetest;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class TrainingDetailActivity extends FragmentActivity implements
        DatePickerFragment.MyOnDateSetListener {

    //private EditText mTitleText;

    private EditText editIncome;
    private EditText editExpenses;
    private EditText editTotal;
    private Button editDateButton;

    static final String TRAINING_ID = "CURRENT_ID";
    static final String DATE_SEPARATOR = "/";

    private Uri trainingUri;
    public int currentId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.trainings_edit);

        editIncome = (EditText) findViewById(R.id.trainings_edit_income);
        editExpenses = (EditText) findViewById(R.id.trainings_edit_expenses);
        editTotal = (EditText) findViewById(R.id.trainings_edit_total);
        editDateButton = (Button) findViewById(R.id.trainings_edit_date_button);

        Button confirmButton = (Button) findViewById(R.id.trainings_confirm_button);

        // check from the saved Instance
        currentId = (bundle == null) ? 0 : (int) bundle.getInt(TRAINING_ID);
        trainingUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyContentProvider.TRAINING_ID);

        Bundle extras = getIntent().getExtras();
        // Or passed from the other activity
        if (extras != null) {
            currentId = extras.getInt(TRAINING_ID);
            //showTrainingId.setText(String.valueOf(currentId));
            trainingUri = extras
                    .getParcelable(MyContentProvider.TRAINING_ID);

            fillData(trainingUri);
        }
        if (currentId < 1) {
            final Calendar c = Calendar.getInstance();
            int year  = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day   = c.get(Calendar.DAY_OF_MONTH);

            editDateButton.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(day).append(DATE_SEPARATOR)
                    .append(month + 1).append(DATE_SEPARATOR)
                    .append(year));
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                checkInputData();

            }

        });



    }

    private void checkInputData() {
        if (TextUtils.isEmpty(editDateButton.getText().toString()) ||
                TextUtils.isEmpty(editTotal.getText().toString())  ||
                editTotal.getText().toString() == "0") {
            makeToast();
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    public void showAttendees(View v) {
        Intent i = new Intent(this, AttendedListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TRAINING_ID, currentId);
        i.putExtras(bundle);
        startActivity(i);
    }

    private void fillData(Uri uri) {
        String[] projection = { TrainingsTable.DATE,
                TrainingsTable.EXPENSES, TrainingsTable.INCOME,
                TrainingsTable.TOTAL};
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            editDateButton.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TrainingsTable.DATE)));
            editIncome.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TrainingsTable.INCOME)));
            editExpenses.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TrainingsTable.EXPENSES)));
            editTotal.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TrainingsTable.TOTAL)));

            // always close the cursor
            cursor.close();
        }
    }

    public void pickDate(View v) { //todo add  on click listener tohle je cunarna :)?
        DialogFragment datePickerFragment = new DatePickerFragment();
        Bundle arguments = parseDate();
        datePickerFragment.setArguments(arguments);
        datePickerFragment.show(getFragmentManager(), getString(R.string.date_picker_title));
    }
    public Bundle parseDate(){
        Bundle arguments = new Bundle();
        String currentDate = editDateButton.getText().toString();
        String dateParts[] = currentDate.split("/");
        int year;
        int month;
        int day;
        try {
            day = Integer.valueOf(dateParts[0]);
        } catch (NumberFormatException E){
            return null;
        }
        try {
            month = Integer.valueOf(dateParts[1]);
        } catch (NumberFormatException E){
            return null;
        }
        try {
            year = Integer.valueOf(dateParts[2]);
        } catch (NumberFormatException E){
            return null;
        }

        arguments.putInt(DatePickerFragment.
                MyOnDateSetListener.UPDATE_YEAR, year);
        arguments.putInt(DatePickerFragment.
                MyOnDateSetListener.UPDATE_MONTH, month - 1);
        arguments.putInt(DatePickerFragment.
                MyOnDateSetListener.UPDATE_DAY, day);

        return arguments;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyContentProvider.TRAINING_ID, trainingUri);
        outState.putInt(TRAINING_ID, currentId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String date = editDateButton.getText().toString();
        double income;
        double expenses;
        double total;
        try {
            income = Double.parseDouble(editIncome.getText().toString());
        } catch (NumberFormatException E){
            income = 0.0;
        }

        try {
            expenses = Double.parseDouble(editExpenses.getText().toString());
        } catch (NumberFormatException E){
            expenses = 0.0;
        }
        try {
            total = Double.parseDouble(editTotal.getText().toString());
        } catch (NumberFormatException E){
            total = 0.0;
        }
        // only save if either summary or description
        // is available

        if (date.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(TrainingsTable.DATE, date);
        values.put(TrainingsTable.INCOME, income);
        values.put(TrainingsTable.EXPENSES, expenses);
        values.put(TrainingsTable.TOTAL, total);

        if (trainingUri == null) {
            // New training
            trainingUri = getContentResolver().insert(MyContentProvider.TRAININGS_URI, values);
        } else {
            // Update training
            getContentResolver().update(trainingUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(TrainingDetailActivity.this, getString(R.string.fill_values_waring),
                Toast.LENGTH_LONG).show();
    }
    @Override
    public void updateDate(int year, int month, int day){
        editDateButton.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append(DATE_SEPARATOR)
                .append(month + 1).append(DATE_SEPARATOR)
                .append(year));

    }

    @Override
    public void onBackPressed(){
        checkInputData();
    }

}
