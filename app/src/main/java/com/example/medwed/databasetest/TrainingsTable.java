package com.example.medwed.databasetest;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class TrainingsTable {
    // Database table
    // todo implement interface for id...
    public static final String TABLE = "trainings";
    public static final String _ID = "_id";
    public static final String DATE = "date";
    public static final String INCOME = "income";
    public static final String EXPENSES = "expenses";
    public static final String TOTAL = "total";
    public static final String MAX_ID = "max(_id) as maxid";

    // Database creation SQL statement
    private static final String CREATE = "create table "
            + TABLE
            + "("
            + _ID + " integer primary key autoincrement, "
            + DATE + " text not null, "
            + INCOME + " real default 0, "
            + EXPENSES + " real default 0, "
            + TOTAL + " real not null"
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
