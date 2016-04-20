package com.example.medwed.databasetest;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class AttendeesTable {
    // Database table
    // todo implement interface for id...
    public static final String TABLE = "attendees";
    public static final String _ID = "_id";
    public static final String TRAINING_ID = "training_id";
    public static final String ATTENDEE_ID = "attendee_id";

    // Database creation SQL statement
    private static final String CREATE = "create table "
            // tood no id?
            + TABLE
            + "("
            + _ID + " integer primary key autoincrement, "
            + TRAINING_ID + " integer, "
            + ATTENDEE_ID + " integer "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TrainingsTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE);
        // tood backup data?
        onCreate(database);
    }

}
