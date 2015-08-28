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
import android.widget.Spinner;
import android.widget.Toast;
import com.example.medwed.databasetest.MyContentProvider;
import com.example.medwed.databasetest.TraineeTable;


public class TraineeDetailActivity extends Activity {

    //private EditText mTitleText;
    private EditText editName;
    private EditText editSurName;

    private Uri traineeUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.trainee_edit);

        editName = (EditText) findViewById(R.id.trainee_edit_name);
        editSurName = (EditText) findViewById(R.id.trainee_edit_surname);
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
                    saveState();
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }

    private void fillData(Uri uri) {
        String[] projection = { TraineeTable.COLUMN_NAME,
                TraineeTable.COLUMN_SURNAME };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            editName.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TraineeTable.COLUMN_NAME)));
            editSurName.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TraineeTable.COLUMN_SURNAME)));

            // always close the cursor
            cursor.close();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyContentProvider.TRAINEE_ID, traineeUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String name = editName.getText().toString();
        String surName = editSurName.getText().toString();

        // only save if either summary or description
        // is available

        if (name.length() == 0 && surName.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(TraineeTable.COLUMN_NAME, name);
        values.put(TraineeTable.COLUMN_SURNAME, surName);
        values.put(TraineeTable.COLUMN_BIRTHDAY, "1.1.1111");//TODO

        if (traineeUri == null) {
            // New trainee
            traineeUri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
        } else {
            // Update trainee
            getContentResolver().update(traineeUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(TraineeDetailActivity.this, "Please maintain a summary",
                Toast.LENGTH_LONG).show();
    }
}
