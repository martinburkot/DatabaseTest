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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;



public class TrainingListActivity extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    // private Cursor cursor;
    private SimpleCursorAdapter adapter;


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainings_list);
        fillData();
        registerForContextMenu(getListView());
    }

    // create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EDIT_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(MyContentProvider.TRAININGS_URI + "/"
                        + info.id);
                Intent i = new Intent(this, TrainingDetailActivity.class);
                i.putExtra(MyContentProvider.TRAINING_ID, uri);
                i.putExtra("CURRENT_ID", info.id);
                startActivity(i);
                return true;

            case DELETE_ID:
                AdapterContextMenuInfo info2 = (AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri2 = Uri.parse(MyContentProvider.TRAININGS_URI + "/"
                        + info2.id);
                getContentResolver().delete(uri2, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public Uri GetMenuItemUri(MenuItem item){
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        Uri uri = Uri.parse(MyContentProvider.TRAININGS_URI + "/"
                + info.id);
        return uri;
    }

    public void createTraining(View V) { //todo add  on click listener tohle je cunarna :)!
        Intent i = new Intent(this, TrainingDetailActivity.class);
        startActivity(i);
    }

    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, TrainingDetailActivity.class);
        Uri todoUri = Uri.parse(MyContentProvider.TRAININGS_URI + "/" + id);
        i.putExtra(MyContentProvider.TRAINING_ID, todoUri);

        startActivity(i);
    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { TrainingsTable.COLUMN_DATE, TrainingsTable.COLUMN_TOTAL };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.trainings_list_date, R.id.trainings_list_total};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.trainings_row, null, from,
                to, 0);

        setListAdapter(adapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, getString(R.string.menu_edit));
        menu.add(0, DELETE_ID, 1, R.string.trainee_delete);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { TrainingsTable.COLUMN_ID, TrainingsTable.COLUMN_DATE,
                TrainingsTable.COLUMN_TOTAL };
        return new CursorLoader(this,
                MyContentProvider.TRAININGS_URI, projection, null, null, null);
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

} 