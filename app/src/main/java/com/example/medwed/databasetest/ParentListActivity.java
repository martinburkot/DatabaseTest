package com.example.medwed.databasetest;

import android.app.ListActivity;
import android.app.LoaderManager;
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




public abstract class ParentListActivity extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    protected static final int EDIT_ID = Menu.FIRST + 1;
    protected static final int DELETE_ID = Menu.FIRST + 2;
    protected static final String TRAINING_ID = "CURRENT_ID";
    protected SimpleCursorAdapter adapter;


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGUI();
        // todo data from extras/bundle
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
                menuEdit(item);
                return true;

            case DELETE_ID:
                menuDelete(item);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void createTrainee(View v) { //todo add  on click listener tohle je cunarna :)?
        Intent i = new Intent(this, TraineeDetailActivity.class);
        startActivity(i);
    }

    public void showTrainings(View v) { //todo add  on click listener tohle je cunarna :)?
        Intent i = new Intent(this, TrainingListActivity.class);
        startActivity(i);
    }

    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        listItemClicked(v, id);
        //CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox_select);
        //if(cb.isChecked()){
        //    cb.setChecked(false);
        //}
        //else{
        //    cb.setChecked(true);
        //}
        /*Intent i = new Intent(this, TraineeDetailActivity.class);
        Uri todoUri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(MyContentProvider.TRAINEE_ID, todoUri);

        startActivity(i);*/ //todo select
    }

    public abstract void setGUI();

    protected abstract void listItemClicked(View v, long id);

    protected void menuDelete(MenuItem item){

        getContentResolver().delete(GetMenuItemUri(item), null, null);
        fillData();

    }

    public abstract void fillData();

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, getString(R.string.menu_edit));
        menu.add(0, DELETE_ID, 1, R.string.menu_delete);
    }
    public abstract void menuEdit(MenuItem item);

    public abstract Uri GetMenuItemUri(MenuItem item);

    // creates a new loader after the initLoader () call
    @Override
    public abstract Loader<Cursor> onCreateLoader(int id, Bundle args);

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