package com.example.medwed.databasetest;


import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class AttendedListActivity extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DELETE_ID = Menu.FIRST + 1;
    // private Cursor cursor;
    private SimpleCursorAdapter adapter;
    public int maxId;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Bundle extras = getIntent().getExtras();
        maxId = (extras == null) ? 0 : (int) extras.getInt("MAX_ID");


        setContentView(R.layout.attended_list);
        fillData();
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(MyContentProvider.TRAININGS_URI + "/"
                        + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void createTraining(View V) { //todo add  on click listener tohle je cunarna :)!
        Intent i = new Intent(this, TrainingDetailActivity.class);
        startActivity(i);
    }

    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{TraineeTable.COLUMN_NAME, TraineeTable.COLUMN_SURNAME};
        // Fields on the UI to which we map
        int[] to = new int[]{R.id.list_name, R.id.list_surname};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.trainee_row, null, from,
                to, 0);

        setListAdapter(adapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.trainee_delete);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { TraineeTable.COLUMN_ID, TraineeTable.COLUMN_NAME,
                TraineeTable.COLUMN_SURNAME };
        String selection = AttendeesTable.COLUMN_TRAINING_ID + " = " + String.valueOf(maxId);
        return new CursorLoader(this,
                MyContentProvider.ATTENDED_URI, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

    public void showToAdd(View v) {
        Intent i = new Intent(this, TraineesListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("MAX_ID", maxId);
        i.putExtras(bundle);
        startActivity(i);
    }

}