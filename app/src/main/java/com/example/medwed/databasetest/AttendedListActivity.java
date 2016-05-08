package com.example.medwed.databasetest;


import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleCursorAdapter;


public class AttendedListActivity extends ParentListActivity{

    public int trainingId;

    @Override
    public void setGUI() {
        setContentView(R.layout.attended_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            trainingId = extras.getInt(TRAINING_ID);
         }
        if (trainingId < 1) {
            getLoaderManager().initLoader(1, null, this);
        }

    }

    @Override
    protected void listItemClicked(View v, long id) {

    }

    public void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{TraineeTable.NAME, TraineeTable.SURNAME};
        // Fields on the UI to which we map
        int[] to = new int[]{R.id.attendee_list_name, R.id.attendee_list_surname};
        if (trainingId > 0) {
            getLoaderManager().initLoader(0, null, this);
        }
        adapter = new SimpleCursorAdapter(this, R.layout.attendee_row, null, from,
                to, 0);

        setListAdapter(adapter);
    }

    @Override
    public void menuEdit(MenuItem item) {

    }

    @Override
    public Uri GetMenuItemUri(MenuItem item) {

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        Uri uri = Uri.parse(MyContentProvider.TRAININGS_URI + "/"
                + info.id);//todo this is for trainingsnot attendees...
        return uri;
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 1){
            String[] projection = { TrainingsTable.MAX_ID};

            return new CursorLoader(this,
                    MyContentProvider.TRAININGS_URI, projection, null, null, null);
        }
        else {
            String[] projection = {TraineeTable._ID, TraineeTable.NAME,
                    TraineeTable.SURNAME};
            String selection = AttendeesTable.TRAINING_ID + " = " + String.valueOf(trainingId);
            return new CursorLoader(this,
                    MyContentProvider.ATTENDED_URI, projection, selection, null, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == 0) {
            adapter.swapCursor(data);
            if (data.getCount() == 0) {
                showToAdd(this.getListView());  // todo is this view correct
            }
        }
        else {
            data.moveToFirst();
            trainingId = data.getInt(0);
            data.close();
            getLoaderManager().initLoader(0, null, this);
        }
    }

    public void showToAdd(View v) {
        Intent i = new Intent(this, TraineesListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TRAINING_ID, trainingId);
        i.putExtras(bundle);
        startActivity(i); //todo start for result...

    }

}