package com.example.medwed.databasetest;

    import android.content.Context;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

public class TheBookDatabase extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "thebook.db";
    private static final int DATABASE_VERSION = 2;

    public TheBookDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        TraineeTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        TraineeTable.onUpgrade(database, oldVersion, newVersion);
    }


}
