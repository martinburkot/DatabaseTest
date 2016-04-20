package com.example.medwed.databasetest;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class AttendedSQLView {
    // Database table
    // todo implement interface for id...
    public static final String VIEV_ATTENDED = "attended";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create view "
            + VIEV_ATTENDED
            + " as select "
            + TraineeTable.TABLE + "." + TraineeTable._ID + ", "
            + TraineeTable.TABLE + "." + TraineeTable.NAME + ", "
            + TraineeTable.TABLE + "." + TraineeTable.SURNAME + ", "
            + AttendeesTable.TABLE + "." + AttendeesTable.TRAINING_ID +" "
            + "from " + TraineeTable.TABLE + " left join "
            + AttendeesTable.TABLE + " on "
            + TraineeTable.TABLE + "." + TraineeTable._ID + " = "
            + AttendeesTable.TABLE + "." + AttendeesTable.ATTENDEE_ID;

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TrainingsTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP VIEW IF EXISTS " + VIEV_ATTENDED);
        // tood backup data?
        onCreate(database);
    }

}
/*
create view attended as
SELECT trainees.name, trainees.surname, attendees.training_id
FROM   trainees
       LEFT JOIN attendees
          ON trainees._id = attendees.attendee_id
 */
