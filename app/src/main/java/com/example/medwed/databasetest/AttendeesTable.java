package com.example.medwed.databasetest;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class AttendeesTable {
    // Database table
    // todo implement interface for id...
    public static final String TABLE_ATTENDEES = "attendees";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TRAINING_ID = "training_id";
    public static final String COLUMN_ATTENDEE_ID = "attendee_id";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            // tood no id?
            + TABLE_ATTENDEES
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TRAINING_ID + " integer, "
            + COLUMN_ATTENDEE_ID + " integer "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TrainingsTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDEES);
        // tood backup data?
        onCreate(database);
    }

}
