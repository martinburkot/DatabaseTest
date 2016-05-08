package com.example.medwed.databasetest;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleCursorAdapter;



public class TrainingListActivity extends ParentListActivity {

    @Override
    public void setGUI() {
        setContentView(R.layout.trainings_list);
    }

    // create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    public Uri GetMenuItemUri(MenuItem item){
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        Uri uri = Uri.parse(MyContentProvider.TRAININGS_URI + "/"
                + info.id);
        return uri;
    }

    public void createTraining(View V) {
        Intent i = new Intent(this, TrainingDetailActivity.class);
        startActivity(i);
    }

    @Override
    protected void listItemClicked(View v, long id) {

        Intent i = new Intent(this, TrainingDetailActivity.class);
        Uri todoUri = Uri.parse(MyContentProvider.TRAININGS_URI + "/" + id);
        i.putExtra(MyContentProvider.TRAINING_ID, todoUri);
        i.putExtra(TRAINING_ID, (int) id);

        startActivity(i);
    }

    public void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { TrainingsTable.DATE, TrainingsTable.TOTAL, TrainingsTable._ID};
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.trainings_list_date, R.id.trainings_list_total, R.id.trainings_list_idNum};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.trainings_row, null, from,
                to, 0);

        setListAdapter(adapter);
    }


    @Override
    public void menuEdit(MenuItem item) {

        Intent i = new Intent(this, TrainingDetailActivity.class);
        i.putExtra(MyContentProvider.TRAINING_ID, GetMenuItemUri(item));

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        i.putExtra(TRAINING_ID, (int) info.id);
        startActivity(i);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { TrainingsTable._ID, TrainingsTable.DATE,
                TrainingsTable.TOTAL};
        return new CursorLoader(this,
                MyContentProvider.TRAININGS_URI, projection, null, null,
                TrainingsTable._ID + " DESC");
    }

} 