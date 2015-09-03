package com.example.medwed.databasetest;
import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


public class MyContentProvider extends ContentProvider {
    // database
    private TheBookDatabase database;

    // used for the UriMacher
    public static final int ALL_TRAINEES = 1;
    public static final int ONE_TRAINEE = 2;
    public static final int ALL_TRAININGS = 3;
    public static final int ONE_TRAINING = 4;
    public static final int ALL_ATTENDED = 5;
    public static final int INSERT_ATTENDANT = 6;

    private static final String AUTHORITY = "com.example.medwed.databasetest.contentprovider";

    private static final String TRAINEE_PATH = "trainees";
    public static final String TRAINEE_ID = "TRAINEE_ID";
    public static final Uri TRAINEES_URI = Uri.parse("content://" + AUTHORITY
            + "/" + TRAINEE_PATH);

    private static final String TRAININGS_PATH = "trainings";
    public static final String TRAINING_ID = "TRAINING_ID";
    public static final Uri TRAININGS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + TRAININGS_PATH);

    private static final String ATTENDEE_PATH = "attendees";
    public static final String ATTENDEE_ID = "ATTENDEE_ID";
    public static final Uri ATTENDEE_URI = Uri.parse("content://" + AUTHORITY
            + "/" + ATTENDEE_PATH);

    private static final String ATTENDED_PATH = "attended";
    public static final Uri ATTENDED_URI = Uri.parse("content://" + AUTHORITY
            + "/" + ATTENDED_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, TRAINEE_PATH, ALL_TRAINEES);
        sURIMatcher.addURI(AUTHORITY, TRAINEE_PATH + "/#", ONE_TRAINEE);
        sURIMatcher.addURI(AUTHORITY, TRAININGS_PATH, ALL_TRAININGS);
        sURIMatcher.addURI(AUTHORITY, TRAININGS_PATH + "/#", ONE_TRAINING);
        sURIMatcher.addURI(AUTHORITY, ATTENDEE_PATH, INSERT_ATTENDANT);
        sURIMatcher.addURI(AUTHORITY, ATTENDED_PATH, ALL_ATTENDED);
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

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case ALL_TRAINEES:
                queryBuilder.setTables(TraineeTable.TABLE_TRAINEE);
                break;
            case ONE_TRAINEE:
                // set table
                queryBuilder.setTables(TraineeTable.TABLE_TRAINEE);
                // adding the ID to the original query
                queryBuilder.appendWhere(TraineeTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case ALL_TRAININGS:
                queryBuilder.setTables(TrainingsTable.TABLE_TRAININGS);
                break;
            case ONE_TRAINING:
                // set table
                queryBuilder.setTables(TrainingsTable.TABLE_TRAININGS);
                // adding the ID to the original query
                queryBuilder.appendWhere(TrainingsTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case ALL_ATTENDED:
                // set table
                queryBuilder.setTables(AttendedSQLView.VIEV_ATTENDED);
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
        Uri newUri;
        long id;
        switch (uriType) {
            case ALL_TRAINEES:
                id = sqlDB.insert(TraineeTable.TABLE_TRAINEE, null, values);
                newUri = ContentUris.withAppendedId(TRAINEES_URI, id);
                break;
            case ALL_TRAININGS:
                id = sqlDB.insert(TrainingsTable.TABLE_TRAININGS, null, values);
                newUri = ContentUris.withAppendedId(TRAININGS_URI, id);
                break;
            case ALL_ATTENDED:
                id = sqlDB.insert(AttendeesTable.TABLE_ATTENDEES, null, values);
                newUri = ContentUris.withAppendedId(ATTENDEE_URI, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
            //INSERT_ATTENDANT
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
        String id;
        switch (uriType) {
            case ALL_TRAINEES:
                rowsUpdated = sqlDB.update(TraineeTable.TABLE_TRAINEE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ONE_TRAINEE:
                id = uri.getLastPathSegment();
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
            case ALL_TRAININGS:
                rowsUpdated = sqlDB.update(TrainingsTable.TABLE_TRAININGS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ONE_TRAINING:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TrainingsTable.TABLE_TRAININGS,
                            values,
                            TraineeTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TrainingsTable.TABLE_TRAININGS,
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
}

