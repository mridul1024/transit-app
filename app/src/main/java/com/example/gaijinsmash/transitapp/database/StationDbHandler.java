package com.example.gaijinsmash.transitapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gaijinsmash.transitapp.model.Station;

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
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDatabase(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int v1, int v2) {
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

    public void addData(SQLiteDatabase db) {
        List<Station> stationList = new ArrayList<Station>();

        db.execSQL("");
    }


}
