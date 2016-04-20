package com.example.medwed.databasetest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;


public class AsyncDBfunctions {
    private static ContentResolver resolver;
    private static Uri uri;

    public static void asyncInsert(ContentResolver resolver, Uri uri, ContentValues... values){
        AsyncDBfunctions.resolver = resolver;
        AsyncDBfunctions.uri = uri;
        new AsyncInsert().execute(values);
    }

    public static void asyncUpdate(ContentResolver resolver, Uri uri, ContentValues... values){
        AsyncDBfunctions.resolver = resolver;
        AsyncDBfunctions.uri = uri;
        new AsyncUpdate().execute(values);
    }


    private static class AsyncInsert extends AsyncTask<ContentValues, Void, Void> {

        @Override
        protected Void doInBackground(ContentValues... values) {

            resolver.insert(uri, values[0]);
            return null;
        }
    }

    private static class AsyncUpdate extends AsyncTask<ContentValues, Void, Void> {

        @Override
        protected Void doInBackground(ContentValues... values) {

            resolver.update(uri, values[0], null, null);
            return null;
        }
    }

}
