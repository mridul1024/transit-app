package com.example.gaijinsmash.transitapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.xmlparser.StationXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanj on 7/31/2017.
 */

public class StationDbHandler extends SQLiteOpenHelper {

    private static final boolean DEBUG = true;
    private static final String DBNAME = "stations.sqlite";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "stations";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ABBR = "abbr";
    private static final String CITY = "city";
    private static final String ADDRESS = "address";
    private static final String COUNTY = "county";
    private static final String STATE = "state";
    private static final String ZIPCODE = "zipcode";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private List<Station> mStations = null;

    public StationDbHandler(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
        throw new UnsupportedOperationException("No upgrade yet");
    }

    private void createDatabase(SQLiteDatabase db) {
        String CREATE_STATIONS_TABLE = "create table " + TABLE_NAME + "(" +
                ID + " integer primary key autoincrement not null, " +
                NAME + " text, " +
                ABBR + " text, " +
                ADDRESS + " text, " +
                CITY + " text, " +
                COUNTY + " text, " +
                STATE + " text, " +
                ZIPCODE + " text, " +
                LATITUDE + " text, " +
                LONGITUDE + " text" + ");";
        if(DEBUG) {
            Log.i("createDatabase:", CREATE_STATIONS_TABLE);
        }
        db.execSQL(CREATE_STATIONS_TABLE);
    }

    public List<Station> getAllStations() {
        new GetStationsTask().execute();
        return mStations;
    }

    public Station getStation(SQLiteDatabase db, String abbr) {
        Station station = new Station();
        if(db != null) {
            String sql = "select * from " + TABLE_NAME + "where abbr='" + abbr +"';";
            db.execSQL(sql);
        }
        return station;
    }

    public void saveStationsList(SQLiteDatabase db, List<Station> list) {
        for(int i = 0; i < list.size(); i++) {
            Station temp = list.get(i);
            prepStationObject(db, temp);
        }
    }

    public void prepStationObject(SQLiteDatabase db, Station station) {
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
        String sql = "INSERT into " + TABLE_NAME + " (" + column + ") values (" + value + ");";
        db.execSQL(sql);
    }

    //TODO: should i use a thread or handler instead?
    private class GetStationsTask extends AsyncTask<Void, Void, Boolean> {
        private List stations = null;

        @Override
        protected Boolean doInBackground(Void... voids) {
            StationXMLParser xmlParser = new StationXMLParser();
            Boolean result = null;

            if(stations == null) {
                try {
                    stations = xmlParser.getStations();
                    result = true;
                } catch (IOException e) {
                    result = false;
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    result = false;
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                // save stations to sqlitedb
                for(int i = 0; i < stations.size(); i++) {

                }
            } else {
                // TODO: handle error gracefully
                Log.e("onPostExecute()", "stations is NULL");
            }
        }
    }
}
