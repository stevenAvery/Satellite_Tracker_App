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

        deleteAll();
        insert(new Satellite ("25544", "ISS (ZARYA)"));
        insert(new Satellite ("37820", "TIANGONG 1"));
        insert(new Satellite ("40314", "SPINSAT"));
        insert(new Satellite ("40898", "S-CUBE"));
        insert(new Satellite ("41313", "AGGIESAT 4"));
        insert(new Satellite ("41314", "BEVO 2"));
        insert(new Satellite ("41474", "MINXSS"));
        insert(new Satellite ("41475", "CADRE"));
        insert(new Satellite ("41476", "STMSAT-1"));
        insert(new Satellite ("41477", "NODES 2"));
        insert(new Satellite ("41478", "NODES 1"));
        insert(new Satellite ("41479", "FLOCK 2E'-1"));
        insert(new Satellite ("41480", "FLOCK 2E'-3"));
        insert(new Satellite ("41481", "FLOCK 2E'-2"));
        insert(new Satellite ("41482", "FLOCK 2E'-4"));
        insert(new Satellite ("41483", "FLOCK 2E-1"));
        insert(new Satellite ("41484", "FLOCK 2E-2"));
        insert(new Satellite ("41485", "LEMUR-2-THERESACONDOR"));
        insert(new Satellite ("41486", "FLOCK 2E-3"));
        insert(new Satellite ("41487", "FLOCK 2E-4"));
        insert(new Satellite ("41488", "LEMUR-2-NICK-ALLAIN"));
        insert(new Satellite ("41489", "LEMUR-2-KANE"));
        insert(new Satellite ("41490", "LEMUR-2-JEFF"));
        insert(new Satellite ("41563", "FLOCK 2E-6"));
        insert(new Satellite ("41564", "FLOCK 2E-5"));
        insert(new Satellite ("41565", "FLOCK 2E-7"));
        insert(new Satellite ("41566", "FLOCK 2E-8"));
        insert(new Satellite ("41567", "FLOCK 2E'-5"));
        insert(new Satellite ("41568", "FLOCK 2E'-6"));
        insert(new Satellite ("41569", "FLOCK 2E'-8"));
        insert(new Satellite ("41570", "FLOCK 2E'-7"));
        insert(new Satellite ("41571", "FLOCK 2E-9"));
        insert(new Satellite ("41572", "FLOCK 2E-10"));
        insert(new Satellite ("41573", "FLOCK 2E-12"));
        insert(new Satellite ("41574", "FLOCK 2E-11"));
        insert(new Satellite ("41575", "FLOCK 2E'-9"));
        insert(new Satellite ("41576", "FLOCK 2E'-10"));
        insert(new Satellite ("41577", "FLOCK 2E'-11"));
        insert(new Satellite ("41578", "FLOCK 2E'-12"));
        insert(new Satellite ("41670", "PROGRESS-MS 03"));
        insert(new Satellite ("41761", "FLOCK 2E'-13"));
        insert(new Satellite ("41762", "FLOCK 2E'-14"));
        insert(new Satellite ("41763", "FLOCK 2E'-16"));
        insert(new Satellite ("41764", "FLOCK 2E'-15"));
        insert(new Satellite ("41765", "TIANGONG-2"));
        insert(new Satellite ("41769", "FLOCK 2E'-18"));
        insert(new Satellite ("41776", "FLOCK 2E'-17"));
        insert(new Satellite ("41777", "FLOCK 2E'-19"));
        insert(new Satellite ("41782", "FLOCK 2E'-20"));
        insert(new Satellite ("41818", "CYGNUS OA-5"));
        insert(new Satellite ("41820", "SOYUZ-MS 02"));
        insert(new Satellite ("41834", "BANXING-2"));
        insert(new Satellite ("41864", "SOYUZ-MS 03"));
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SATELLITES);
        onCreate(db);
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