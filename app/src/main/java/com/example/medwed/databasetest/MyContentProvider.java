package com.example.medwed.databasetest;
import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.example.medwed.databasetest.TheBookDatabase;
import com.example.medwed.databasetest.TraineeTable;

public class MyContentProvider extends ContentProvider {
    // database
    private TheBookDatabase database;

    // used for the UriMacher
    public static final int ALL_TRAINEES = 1;
    public static final int ONE_TRAINEE = 2;

    private static final String AUTHORITY = "com.example.medwed.databasetest.contentprovider";

    private static final String BASE_PATH = "thebook";
    public static final String TRAINEE_ID = "TRAINEE_ID";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, ALL_TRAINEES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ONE_TRAINEE);
    }

    @Override
    public boolean onCreate() {
        database = new TheBookDatabase(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(TraineeTable.TABLE_TRAINEE);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case ALL_TRAINEES:
                break;
            case ONE_TRAINEE:
                // adding the ID to the original query
                queryBuilder.appendWhere(TraineeTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted;
        long id;
        switch (uriType) {
            case ALL_TRAINEES:
                id = sqlDB.insert(TraineeTable.TABLE_TRAINEE, null, values);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
        getContext().getContentResolver().notifyChange(uri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case ALL_TRAINEES:
                rowsDeleted = sqlDB.delete(TraineeTable.TABLE_TRAINEE, selection,
                        selectionArgs);
                break;
            case ONE_TRAINEE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(TraineeTable.TABLE_TRAINEE,
                            TraineeTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(TraineeTable.TABLE_TRAINEE,
                            TraineeTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated;
        switch (uriType) {
            case ALL_TRAINEES:
                rowsUpdated = sqlDB.update(TraineeTable.TABLE_TRAINEE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ONE_TRAINEE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TraineeTable.TABLE_TRAINEE,
                            values,
                            TraineeTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TraineeTable.TABLE_TRAINEE,
                            values,
                            TraineeTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { TraineeTable.COLUMN_NAME,
                TraineeTable.COLUMN_SURNAME, TraineeTable.COLUMN_BIRTHDAY,
                TraineeTable.COLUMN_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}

