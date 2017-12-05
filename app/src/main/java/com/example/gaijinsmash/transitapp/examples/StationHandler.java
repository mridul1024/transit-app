package com.example.gaijinsmash.transitapp.examples;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.gaijinsmash.transitapp.model.bart.Station;

import java.util.ArrayList;
import java.util.List;

public class StationHandler {

    private static final boolean DEBUG = true;

    private SQLiteDatabase mDatabase;
    private MySqliteHelper mDatabaseHelper;
    private String[] allColumns = { MySqliteHelper.ID, MySqliteHelper.ABBR, MySqliteHelper.NAME, MySqliteHelper.ADDRESS,
                                    MySqliteHelper.CITY, MySqliteHelper.COUNTY, MySqliteHelper.STATE, MySqliteHelper.ZIPCODE, MySqliteHelper.LATITUDE, MySqliteHelper.LONGITUDE};

    public StationHandler(Context context) {
        mDatabaseHelper = new MySqliteHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        mDatabaseHelper.close();
    }

    public Station createStation(String abbr) {
        ContentValues values = new ContentValues();
        values.put(MySqliteHelper.ABBR, abbr);
        long insertId = mDatabase.insert(MySqliteHelper.TABLE_STATIONS, null, values);
        Cursor cursor = mDatabase.query(MySqliteHelper.TABLE_STATIONS, allColumns, MySqliteHelper.ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Station newStation = cursorToStation(cursor);
        cursor.close();
        return newStation;
    }

    public void deleteStation(Station station) {
        long id = station.getId();
        if(DEBUG)
            Log.i("deleteStation()", "Comment deleted with id: " + id);
        mDatabase.delete(MySqliteHelper.TABLE_STATIONS, MySqliteHelper.ID + " = " + id, null);
    }

    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<Station>();
        Cursor cursor = mDatabase.query(MySqliteHelper.TABLE_STATIONS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Station station = cursorToStation(cursor);
            stations.add(station);
            cursor.moveToNext();
        }
        cursor.close();
        return stations;
    }

    public Station getStation(String abbr) {
        Station station = new Station("name");
        if(mDatabase != null) {
            String sql = "select * from stations where abbr='" + abbr + "';";
            mDatabase.execSQL(sql);
        }
        return station;
    }

    // TODO: check logic
    private Station cursorToStation(Cursor cursor) {
        Station station = new Station("name");
        //station.setName(getString(0));
        // add more
        return station;
    }
/*
    public void saveStationsList(SQLiteDatabase db, List<Station> list) {
        for(int i = 0; i < list.size(); i++) {
            Station temp = list.get(i);
            createStationObject(db, temp);
        }
    }

    public void createStationObject(SQLiteDatabase db, Station station) {
        String abbr = station.getAbbreviation();
        addStationValue(db, ABBR, abbr);
        String name = station.getName();
        addStationValue(db, NAME, name);
        String address = station.getAddress();
        addStationValue(db, ADDRESS, address);
        String city = station.getCity();
        addStationValue(db, CITY, city);
        String county = station.getCounty();
        addStationValue(db, COUNTY, county);
        String state = station.getState();
        addStationValue(db, STATE, state);
        String zipcode = station.getZipcode();
        addStationValue(db, ZIPCODE, zipcode);
        String longitude = station.getLongitude();
        addStationValue(db, LONGITUDE, longitude);
        String latitude = station.getLatitude();
        addStationValue(db, LATITUDE, latitude);
    }

    public void addStationValue(SQLiteDatabase db, String column, String value) {
        String sql = "INSERT into " + TABLE_STATIONS + " (" + column + ") values (" + value + ");";
        db.execSQL(sql);
    }

    */
}
