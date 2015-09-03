package com.example.medwed.databasetest;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class TraineeTable {
    // Database table
    // todo implement interface for id...
    public static final String TABLE_TRAINEE = "trainees";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_WEIGHT = "weight";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRAINEE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_SURNAME + " text not null, "
            + COLUMN_BIRTHDAY + " text not null, "
            + COLUMN_WEIGHT + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TraineeTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("create table traineesbackup ( "
                + COLUMN_ID + " integer , "
                + COLUMN_NAME + " text , "
                + COLUMN_SURNAME + " text , "
                + COLUMN_BIRTHDAY + " text , "
                + COLUMN_WEIGHT + " text "
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
