package com.example.medwed.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class TraineeDetailActivity extends Activity {

    private EditText editName;
    private EditText editSurName;
    private EditText editBirthDay;
    private EditText editWeight;

    private Uri traineeUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.trainee_edit);

        editName = (EditText) findViewById(R.id.trainee_edit_name);
        editSurName = (EditText) findViewById(R.id.trainee_edit_surname);
        editBirthDay = (EditText) findViewById(R.id.trainee_edit_birthday);
        editWeight = (EditText) findViewById(R.id.trainee_edit_weight);

        Button confirmButton = (Button) findViewById(R.id.todo_edit_button);

        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        traineeUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyContentProvider.TRAINEE_ID);

        // Or passed from the other activity
        if (extras != null) {
            traineeUri = extras
                    .getParcelable(MyContentProvider.TRAINEE_ID);

            fillData(traineeUri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(editName.getText().toString()) ||
                        TextUtils.isEmpty(editSurName.getText().toString())) {
                    makeToast();
                } else {
                    saveData();
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }

    private void fillData(Uri uri) {
        String[] projection = { TraineeTable.NAME,
                TraineeTable.SURNAME, TraineeTable.BIRTHDAY,
                TraineeTable.WEIGHT};
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            editName.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TraineeTable.NAME)));
            editSurName.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TraineeTable.SURNAME)));
            editBirthDay.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TraineeTable.BIRTHDAY)));
            editWeight.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TraineeTable.WEIGHT)));

            // always close the cursor
            cursor.close();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // todo saveState();
        outState.putParcelable(MyContentProvider.TRAINEE_ID, traineeUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void saveData() {
        String name = editName.getText().toString();
        String surName = editSurName.getText().toString();
        String birthDay = editBirthDay.getText().toString();
        String weight = editWeight.getText().toString();

        if (name.length() == 0 && surName.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(TraineeTable.NAME, name);
        values.put(TraineeTable.SURNAME, surName);
        if (birthDay.length() > 0) {
            values.put(TraineeTable.BIRTHDAY, birthDay);
        }
        else {
            values.put(TraineeTable.BIRTHDAY, " ");
        }
        if (weight.length() > 0) {
            values.put(TraineeTable.WEIGHT, weight);
        }
        else {
            values.put(TraineeTable.WEIGHT, 0);
        }
        if (traineeUri == null) {
            // New trainee
            AsyncDBfunctions.asyncInsert(getContentResolver(), getUriInsert(), values);
            //new AsyncInsert().execute(values);
        } else {
            // Update trainee
            AsyncDBfunctions.asyncUpdate(getContentResolver(), getUriUpdate(), values);
        }
    }

    private void makeToast() {
        Toast.makeText(TraineeDetailActivity.this, "Please maintain a summary",
                Toast.LENGTH_LONG).show();
    }

    private Uri getUriUpdate() {
        return this.traineeUri;
    }

    private Uri getUriInsert() {
        return MyContentProvider.TRAINEES_URI;
    }

}
