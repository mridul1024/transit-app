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
    private StationDatabase mDatabase;

    public StationDbHelper(Context context) {
        this.mContext = context;
        mDatabase = StationDatabase.getRoomDB(context);
    }

    public StationDatabase getDb() { return mDatabase; }

    public void initStationDb() throws Exception {
        if(mDatabase.getStationDAO().countStations() == 0) {
            List<Station> stationList = null;
            StationXmlParser parser = new StationXmlParser(mContext);
            stationList = parser.getList(ApiStringBuilder.getAllStations());
            for(Station x : stationList) {
                mDatabase.getStationDAO().addStation(x);
            }
        }
    }

    public String getNameFromAbbr(String abbr) throws XmlPullParserException, IOException {
        Station station = mDatabase.getStationDAO().getStationByAbbr(abbr);
        return station.getName();
    }

    public String getAbbrFromName(String stationName) throws XmlPullParserException, IOException {
        Station station = mDatabase.getStationDAO().getStationByName(stationName);
        return station.getAbbreviation();
    }

    public String getAbbrFromDb(String stationName) throws XmlPullParserException, IOException {
        Station station = mDatabase.getStationDAO().getStationByName(stationName);
        return station.getAbbreviation();
    }
}
