package com.example.gaijinsmash.transitapp.examples;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gaijinsmash.transitapp.model.bart.Station;

import java.util.List;

/**
 * Created by ryanj on 7/31/2017.
 */

public class MySqliteHelper extends SQLiteOpenHelper {


    private static final boolean DEBUG = true;
    private Context mContext;

    //Table Info
    private static final String DBNAME = "stations.sqlite";
    private static final int VERSION = 1;
    public static final String TABLE_STATIONS = "stations";

    //Table Column Names
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ABBR = "abbr";
    public static final String CITY = "city";
    public static final String ADDRESS = "address";
    public static final String COUNTY = "county";
    public static final String STATE = "state";
    public static final String ZIPCODE = "zipcode";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private List<Station> mStations = null;

    public MySqliteHelper(Context context) {
        super(context, DBNAME, null, VERSION);
        if(this.mContext == null) {
            this.mContext = context;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(DEBUG) {
            Log.w(MySqliteHelper.class.getName(),
                    "Upgrading db version " + oldVersion + " to "
                            + newVersion + ". ALL OLD DATA WILL BE DESTROYED!");
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATIONS);
        onCreate(db);
    }

    private void createDatabase(SQLiteDatabase db) {
        String CREATE_STATIONS_TABLE = "create table " + TABLE_STATIONS + "(" +
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
}
