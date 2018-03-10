package com.example.gaijinsmash.transitapp.database;

import android.content.Context;
import android.util.Log;

import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.xmlparser.StationXmlParser;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class StationDbHelper {

    private StationDatabase mDatabase;
    private Context mContext;

    public StationDbHelper(Context context) {
        this.mContext = context;
        if(mDatabase == null) {
            mDatabase = StationDatabase.getRoomDB(mContext);
        }
    }

    public static void initStationDb(Context context) throws Exception {
        Log.i("init db", "Station DB");
        StationDatabase db = StationDatabase.getRoomDB(context);
        if(db.getStationDAO().countStations() == 0) {
            List<Station> stationList = null;
            StationXmlParser parser = new StationXmlParser(context);
            stationList = parser.getList(ApiStringBuilder.getAllStations());
            for(Station x : stationList) {
                db.getStationDAO().addStation(x);
            }
        } else {
            Log.i("initStationDB", "SKIPPING");
        }
    }

    public String getNameFromAbbr(String abbr) throws XmlPullParserException, IOException {
        StationDatabase db = StationDatabase.getRoomDB(mContext);
        Station station = db.getStationDAO().getStationByAbbr(abbr);
        Log.i("getNameFromAbbr()", station.getName());
        return station.getName();
    }

    public String getAbbrFromName(String stationName) throws XmlPullParserException, IOException {
        Log.i("STATION NAME: ", stationName);
        StationDatabase db = StationDatabase.getRoomDB(mContext);
        Station station = db.getStationDAO().getStationByName(stationName);
        Log.i("Station", station.getName());
        Log.i("ABBR: ", station.getAbbreviation());
        return station.getAbbreviation();
    }


}
