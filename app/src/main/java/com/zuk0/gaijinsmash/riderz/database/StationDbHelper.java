package com.zuk0.gaijinsmash.riderz.database;

import android.content.Context;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.debug.DebugController;
import com.zuk0.gaijinsmash.riderz.model.bart.Station;
import com.zuk0.gaijinsmash.riderz.utils.BartApiStringBuilder;
import com.zuk0.gaijinsmash.riderz.xml_adapter.station.StationXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class StationDbHelper {

    private StationDatabase mDatabase;
    // Update this number when new Stations are built
    private static final int mNumOfBartStations = 48;

    public StationDbHelper(Context context) {
        mDatabase = StationDatabase.getRoomDB(context);
    }

    public void closeDb() {
        mDatabase.close();
    }

    public void initStationDb(Context context) throws IOException, XmlPullParserException {
        int count = getStationCount();
        if(count == 0 || count < mNumOfBartStations) {
            List<Station> stationList;
            StationXmlParser parser = new StationXmlParser(context);
            stationList = parser.getList(BartApiStringBuilder.getAllStations());
            for(Station x : stationList) {
                mDatabase.getStationDAO().addStation(x);
            }
        }
    }

    private int getStationCount() {
        int count = mDatabase.getStationDAO().countStations();
        if(DebugController.DEBUG)
            Log.i("count", String.valueOf(count));
        return count;
    }

    public List<Station> getAllStations() {
        return mDatabase.getStationDAO().getAllStations();
    }

    public String getAbbrFromDb(String stationName) {
        Station station = mDatabase.getStationDAO().getStationByName(stationName);
        return station.getAbbreviation();
    }
}
