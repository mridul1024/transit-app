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

    public static final boolean DEBUG = true;
    public static final String DBNAME = "stations.sqlite";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "stations";
    public static final String ID = "id";
    public static final String NAME = "name";

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
                NAME + " text "
                + ");";
        if(DEBUG) {
            Log.i("createDatabase:", CREATE_STATIONS_TABLE);
        }
        db.execSQL(CREATE_STATIONS_TABLE);
    }

    private class GetStationsTask extends AsyncTask<Void, Void, Boolean> {
        private List stations = new ArrayList<Station>();

        @Override
        protected Boolean doInBackground(Void... voids) {
            StationXMLParser xmlParser = new StationXMLParser();
            Boolean result = null;
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
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                // save stations to sqlitedb
                for(int i = 0; i < stations.size(); i++) {

                }
            } else {
                // handle error gracefully
            }
        }
    }
}
