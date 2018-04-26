package com.zuk0.gaijinsmash.riderz.database;

import android.content.Context;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.model.bart.Station;
import com.zuk0.gaijinsmash.riderz.network.xmlparser.StationXmlParser;
import com.zuk0.gaijinsmash.riderz.utils.BartApiStringBuilder;

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
        int numOfStations = 48; // Change this value as new stations are constructed
        int count = mDatabase.getStationDAO().countStations();
        Log.i("count", String.valueOf(count));
        if(count == 0 || count < numOfStations) {
            List<Station> stationList;
            StationXmlParser parser = new StationXmlParser(mContext);
            stationList = parser.getList(BartApiStringBuilder.getAllStations());
            for(Station x : stationList) {
                mDatabase.getStationDAO().addStation(x);
            }
        }
    }

    public String getAbbrFromDb(String stationName) throws XmlPullParserException, IOException {
        Station station = mDatabase.getStationDAO().getStationByName(stationName);
        return station.getAbbreviation();
    }
}
