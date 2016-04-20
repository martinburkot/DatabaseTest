package com.example.medwed.databasetest;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class TraineeTable {
    // Database table
    // todo implement interface for id...
    public static final String TABLE = "trainees";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String BIRTHDAY = "birthday";
    public static final String WEIGHT = "weight";

    // Database creation SQL statement
    private static final String CREATE = "create table "
            + TABLE
            + "("
            + _ID + " integer primary key autoincrement, "
            + NAME + " text not null, "
            + SURNAME + " text not null, "
            + BIRTHDAY + " text not null, "
            + WEIGHT + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TraineeTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("create table traineesbackup ( "
                + _ID + " integer , "
                + NAME + " text , "
                + SURNAME + " text , "
                + BIRTHDAY + " text , "
                + WEIGHT + " text "
                + ");");
        database.execSQL(" insert into traineesbackup "
                + "select * from trainees ");
        database.execSQL("DROP TABLE IF EXISTS trainees");
        // tood backup data?
        onCreate(database);
        database.execSQL(" insert into trainees "
                + "select * from traineesbackup ");
        database.execSQL("DROP TABLE IF EXISTS traineesbackup");
    }

}
