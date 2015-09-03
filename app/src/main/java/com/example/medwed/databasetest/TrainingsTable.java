package com.example.medwed.databasetest;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class TrainingsTable {
    // Database table
    // todo implement interface for id...
    public static final String TABLE_TRAININGS = "trainings";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_INCOME = "income";
    public static final String COLUMN_EXPENSES = "expenses";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_MAX_ID = "max(_id) as maxid";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRAININGS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DATE + " text not null, "
            + COLUMN_INCOME + " real default 0, "
            + COLUMN_EXPENSES + " real default 0, "
            + COLUMN_TOTAL + " real not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TrainingsTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAININGS);
        // tood backup data?
        onCreate(database);
    }

}
