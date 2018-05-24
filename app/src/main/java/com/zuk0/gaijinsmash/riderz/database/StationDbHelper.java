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

    // Update this number when new Stations are built
    private static final int mNumOfBartStations = 48;

    public static void initStationDb(Context context) throws IOException, XmlPullParserException {
        int count = getStationCount(context);
        if(count == 0 || count < mNumOfBartStations) {
            List<Station> stationList;
            StationXmlParser parser = new StationXmlParser(context);
            stationList = parser.getList(BartApiStringBuilder.getAllStations());

            StationDatabase db = StationDatabase.getRoomDB(context);
            for(Station x : stationList) {
                db.getStationDAO().addStation(x);
            }
            db.close();
        }
    }

    private static int getStationCount(Context context) {
        StationDatabase db = StationDatabase.getRoomDB(context);
        int count = db.getStationDAO().countStations();
        if(DebugController.DEBUG)
            Log.i("count", String.valueOf(count));
        db.close();
        return count;
    }

    public static List<Station> getAllStations(Context context) {
        StationDatabase db = StationDatabase.getRoomDB(context);
        List<Station> stations = db.getStationDAO().getAllStations();
        db.close();
        return stations;
    }

    public static String getAbbrFromDb(Context context, String stationName) {
        StationDatabase db = StationDatabase.getRoomDB(context);
        Station station = db.getStationDAO().getStationByName(stationName);
        db.close();
        return station.getAbbreviation();
    }
}
