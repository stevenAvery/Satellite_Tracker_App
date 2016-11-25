package com.example.savery.satellite_tracker_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SatelliteDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME =   "Satellites.db";

    public static final String TABLE_SATELLITES = "Satellites";
    public static final String KEY_NORAD_ID     = "NoradId";
    public static final String KEY_NAME         = "Name";
    public static final String KEY_TYPE         = "Type";

    private static final String DATABASE_CREATE =
        "CREATE TABLE " + TABLE_SATELLITES + " (\n" +
            KEY_NORAD_ID + " TEXT PRIMARY KEY,\n" +
            KEY_NAME +     " TEXT,\n" +
            KEY_TYPE +     " TEXT\n" +
        ");";


    public SatelliteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SATELLITES);
        db.execSQL(DATABASE_CREATE);
    }

    // query the database, finding all products
    public List<Satellite> findAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Satellite> results = new ArrayList<>();

        String[] columns = new String[] {
                KEY_NORAD_ID,
                KEY_NAME,
                KEY_TYPE,
        };
        String where = "";
        String[] whereArgs = new String[] { };
        String groupBy = "";
        String groupArgs = "";
        String orderBy = KEY_NORAD_ID;

        Cursor cursor = db.query(TABLE_SATELLITES, columns, where, whereArgs,
                groupBy, groupArgs, orderBy);

        // if we didn't find anything, don't return anything
        if(cursor == null)
            return null;

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            results.add(new Satellite(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2)
            ));
            cursor.moveToNext();
        }

        return results;
    }

    // return the one satellite with the same Id
    public Satellite findById(String noradId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[] {
                KEY_NORAD_ID,
                KEY_NAME,
                KEY_TYPE,
        };
        String where = KEY_NORAD_ID + "=?";
        String[] whereArgs = new String[] { noradId };
        String groupBy = "";
        String groupArgs = "";
        String orderBy = KEY_NORAD_ID;

        Cursor cursor = db.query(TABLE_SATELLITES, columns, where, whereArgs,
                groupBy, groupArgs, orderBy);

        // if we didn't find anything, don't return anything
        if(cursor == null)
            return null;

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            return new Satellite(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
        }

        return null;
    }

    // insert a new satellite into the database
    void insert(Satellite satellite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NORAD_ID, satellite.getNoradId());
        values.put(KEY_NAME, satellite.getName());
        values.put(KEY_TYPE, satellite.getType());

        db.insert(TABLE_SATELLITES, null, values);
    }

    // delete all satellites from database
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_SATELLITES, "", new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // delete a satellite from the database
    void delete(String noradId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SATELLITES, KEY_NORAD_ID+ "=?",
                new String[] { noradId });
    }
}