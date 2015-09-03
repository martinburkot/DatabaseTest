package com.example.medwed.databasetest;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.medwed.databasetest.MyContentProvider;
import com.example.medwed.databasetest.TraineeTable;
import com.example.medwed.databasetest.AttendedListActivity;


public class TrainingDetailActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    //private EditText mTitleText;
    private EditText editDate;
    private EditText editIncome;
    private EditText editExpenses;
    private EditText editTotal;
    private EditText editMaxId;

    private Uri trainingUri;
    public int maxId;
    public int currentId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.trainings_edit);

        editDate = (EditText) findViewById(R.id.trainings_edit_date);
        editIncome = (EditText) findViewById(R.id.trainings_edit_income);
        editExpenses = (EditText) findViewById(R.id.trainings_edit_expenses);
        editTotal = (EditText) findViewById(R.id.trainings_edit_total);
        editMaxId = (EditText) findViewById(R.id.trainings_max_id);

        Button confirmButton = (Button) findViewById(R.id.trainings_edit_button);
        Bundle extras = getIntent().getExtras();


        // check from the saved Instance
        trainingUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyContentProvider.TRAINING_ID);

        maxId = (bundle == null) ? 0 : (int) bundle.getInt("MAX_ID");

        // Or passed from the other activity
        if (extras != null) {
            trainingUri = extras
                    .getParcelable(MyContentProvider.TRAINING_ID);

            fillData(trainingUri);
            currentId = extras.getInt("CURRENT_ID", -1);
        }
        if (maxId < 1) {
            getLoaderManager().initLoader(0, null, this);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(editDate.getText().toString()) ||
                        TextUtils.isEmpty(editTotal.getText().toString())) {
                    makeToast();
                } else {
                    //saveState();
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });

    }

    public void showAttendees(View v) { //todo add  on click listener tohle je cunarna :)?
        Intent i = new Intent(this, AttendedListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("MAX_ID", currentId);
        i.putExtras(bundle);
        startActivity(i);
    }

    private void fillData(Uri uri) {
        String[] projection = { TrainingsTable.COLUMN_DATE,
                TrainingsTable.COLUMN_EXPENSES, TrainingsTable.COLUMN_INCOME,
                TrainingsTable.COLUMN_TOTAL  };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            editDate.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TrainingsTable.COLUMN_DATE)));
            editIncome.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TrainingsTable.COLUMN_INCOME)));
            editExpenses.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TrainingsTable.COLUMN_EXPENSES)));
            editTotal.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TrainingsTable.COLUMN_TOTAL)));

            // always close the cursor
            cursor.close();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyContentProvider.TRAINING_ID, trainingUri);
        outState.putInt("MAX_ID", maxId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String date = editDate.getText().toString();
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
        values.put(TrainingsTable.COLUMN_DATE, date);
        values.put(TrainingsTable.COLUMN_INCOME, income);
        values.put(TrainingsTable.COLUMN_EXPENSES, expenses);
        values.put(TrainingsTable.COLUMN_TOTAL, total);

        if (trainingUri == null) {
            // New training
            trainingUri = getContentResolver().insert(MyContentProvider.TRAININGS_URI, values);
        } else {
            // Update training
            getContentResolver().update(trainingUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(TrainingDetailActivity.this, getString(R.string.fill_date_waring),
                Toast.LENGTH_LONG).show();
    }
    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { TrainingsTable.COLUMN_MAX_ID};

        return new CursorLoader(this,
                MyContentProvider.TRAININGS_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            maxId = data.getInt(0) + 1;
            editMaxId.setText(String.valueOf(maxId));
            data.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
