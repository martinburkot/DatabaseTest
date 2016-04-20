package com.example.medwed.databasetest;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class TraineesListActivity extends ParentListActivity {

    public int trainingId;

    @Override
    public void setGUI() {
        setContentView(R.layout.trainees_list);
        getListView(). setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setItemsCanFocus(false);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            trainingId = extras.getInt(TRAINING_ID);
            //showCurrentid.setText(String.valueOf(currentId));
            //trainingUri = extras
            //        .getParcelable(MyContentProvider.TRAINING_ID);

            //fillData(trainingUri);
        }

    }

    @Override
    public Uri GetMenuItemUri(MenuItem item){
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        Uri uri = Uri.parse(MyContentProvider.TRAINEES_URI + "/"
                + info.id);
        return uri;
    }

    public void createTrainee(View v) { //todo add  on click listener tohle je cunarna :)?
        Intent i = new Intent(this, TraineeDetailActivity.class);
        startActivity(i);
    }

    @Override
    protected void listItemClicked(View v, long id) {
        CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox_select);
        if(cb.isChecked()){
            cb.setChecked(false);
        }
        else{
            cb.setChecked(true);// todo error?
        }
    }
    @Override
    public void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { TraineeTable.NAME, TraineeTable.SURNAME};
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.list_name, R.id.list_surname };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.trainee_row, null, from,
                to, 0);

        setListAdapter(adapter);
    }

    @Override
    public void menuEdit(MenuItem item) {
        Intent i = new Intent(this, TraineeDetailActivity.class);
        i.putExtra(MyContentProvider.TRAINEE_ID, GetMenuItemUri(item));
        startActivity(i);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { TraineeTable._ID, TraineeTable.NAME,
                TraineeTable.SURNAME};
        String order = TraineeTable.NAME + ", " + TraineeTable.SURNAME;
        return new CursorLoader(this,
                MyContentProvider.TRAINEES_URI, projection, null, null, order);
    }


    public void acceptSelect(View V) {
        SparseBooleanArray checked = getListView().getCheckedItemPositions();
        int pos;
        int currentId;
        Cursor c = adapter.getCursor();
        for (int i =0; i < checked.size(); i++){
            if (checked.valueAt(i) == true) {
                pos = checked.keyAt(i);
                c.moveToPosition(pos);
                currentId = c.getInt(0);
                writeAttendant(currentId);
            }
        }
        finish();
        return;
    }


    public void writeAttendant(int currentId) {
        ContentValues values = new ContentValues();
        values.put(AttendeesTable.TRAINING_ID, trainingId);
        values.put(AttendeesTable.ATTENDEE_ID, currentId);

        getContentResolver().insert(MyContentProvider.ATTENDED_URI, values);

    }

} 