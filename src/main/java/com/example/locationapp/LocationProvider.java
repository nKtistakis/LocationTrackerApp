package com.example.locationapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class LocationProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.locationapp.LocationProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/locations";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String ID = "_id";
    static final String UNIX_TIMESTAMP = "unix_timestamp";
    static final String LAT = "lat";
    static final String LON = "lon";

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "LocationsDB";
    static final String LOCATIONS_TABLE_NAME = "Locations";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE IF NOT EXISTS " + LOCATIONS_TABLE_NAME +
                    "(" + ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     UNIX_TIMESTAMP + " TEXT NOT NULL, " +
                     LAT + "TEXT NOT NULL, " +
                     LON + "TEXT NOT NULL);";


    private static HashMap<String, String> LOCATIONS_PROJECTION_MAP;

    static final int LOCATIONS = 1;
    static final int LOCATIONS_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "locations", LOCATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "locations/#", LOCATIONS_ID);
    }


    @Override
    public boolean onCreate() {

        Context context = getContext();
        LocationHelper dbHelper = new LocationHelper(context);
        db = dbHelper.getWritableDatabase();

        return db == null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(LOCATIONS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case LOCATIONS:
                qb.setProjectionMap(LOCATIONS_PROJECTION_MAP);
                break;

            case LOCATIONS_ID:
                qb.appendWhere( ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }


        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, null);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long rowID = db.insert(LOCATIONS_TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }



    private static class LocationHelper extends SQLiteOpenHelper {
        LocationHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  LOCATIONS_TABLE_NAME);
            onCreate(db);
        }
    }
}
