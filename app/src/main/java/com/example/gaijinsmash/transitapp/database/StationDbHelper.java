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

    private Context mContext;
    StationDatabase mDatabase;

    public StationDbHelper(Context context) {
        this.mContext = context;
        mDatabase = StationDatabase.getRoomDB(context);
    }

    public StationDatabase getDb() { return mDatabase; }

    public void initStationDb(Context context) throws Exception {
        Log.i("init db", "Station DB");
        if(mDatabase.getStationDAO().countStations() == 0) {
            List<Station> stationList = null;
            StationXmlParser parser = new StationXmlParser(context);
            stationList = parser.getList(ApiStringBuilder.getAllStations());
            for(Station x : stationList) {
                mDatabase.getStationDAO().addStation(x);
            }
        } else {
            Log.i("initStationDB", "SKIPPING");
        }
    }

    public String getNameFromAbbr(String abbr) throws XmlPullParserException, IOException {
        Station station = mDatabase.getStationDAO().getStationByAbbr(abbr);
        Log.i("getNameFromAbbr()", station.getName());
        return station.getName();
    }

    public String getAbbrFromName(String stationName) throws XmlPullParserException, IOException {
        Log.i("STATION NAME: ", stationName);
        Station station = mDatabase.getStationDAO().getStationByName(stationName);
        return station.getAbbreviation();
    }

    public String getAbbrFromDb(String stationName) throws XmlPullParserException, IOException {
        Log.i("STATION NAME: ", stationName);
        Station station = mDatabase.getStationDAO().getStationByName(stationName);
        return station.getAbbreviation();
    }
}
