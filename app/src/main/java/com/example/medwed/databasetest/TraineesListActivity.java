package com.example.medwed.databasetest;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.medwed.databasetest.MyContentProvider;
import com.example.medwed.databasetest.TraineeTable;



public class TraineesListActivity extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    // private Cursor cursor;
    private SimpleCursorAdapter adapter;
    public int maxId;


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        maxId = (extras == null) ? 0 : (int) extras.getInt("MAX_ID");

        setContentView(R.layout.trainees_list);
        getListView(). setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setItemsCanFocus(false);
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
                Intent i = new Intent(this, TraineeDetailActivity.class);
                i.putExtra(MyContentProvider.TRAINEE_ID, GetMenuItemUri(item));
                startActivity(i);
                return true;

            case DELETE_ID:
                getContentResolver().delete(GetMenuItemUri(item), null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

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

    public void showTrainings(View v) { //todo add  on click listener tohle je cunarna :)?
        Intent i = new Intent(this, TrainingListActivity.class);
        startActivity(i);
    }

    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox_select);
        if(cb.isChecked()){
            cb.setChecked(false);
        }
        else{
            cb.setChecked(true);
        }
        /*Intent i = new Intent(this, TraineeDetailActivity.class);
        Uri todoUri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(MyContentProvider.TRAINEE_ID, todoUri);

        startActivity(i);*/ //todo select
    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { TraineeTable.COLUMN_NAME, TraineeTable.COLUMN_SURNAME };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.list_name, R.id.list_surname };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.trainee_row, null, from,
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
        String[] projection = { TraineeTable.COLUMN_ID, TraineeTable.COLUMN_NAME,
                TraineeTable.COLUMN_SURNAME };
        return new CursorLoader(this,
                MyContentProvider.TRAINEES_URI, projection, null, null, null);
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
    public double acceptSelect(View V) { //todo add  on click listener tohle je cunarna :)?
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
        return 500;
    }
    // todo update...
    public View getViewByPosition(int pos) {
        final int firstListItemPosition = getListView().getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + getListView().getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return getListView().getAdapter().getView(pos, null, getListView());
        } else {
            final int childIndex = pos - firstListItemPosition;
            return getListView().getChildAt(childIndex);
        }
    }
    public void writeAttendant(int currentId) {
        ContentValues values = new ContentValues();
        values.put(AttendeesTable.COLUMN_TRAINING_ID, maxId);
        values.put(AttendeesTable.COLUMN_ATTENDEE_ID, currentId);

        getContentResolver().insert(MyContentProvider.ATTENDED_URI, values);

    }

} 