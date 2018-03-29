package com.example.gaijinsmash.transitapp.helper;

import android.content.Context;
import android.util.Log;
import com.example.gaijinsmash.transitapp.database.StationDatabase;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

public class TripHelper {

    public static String getAbbrFromDb(String stationName, Context context) throws XmlPullParserException, IOException {
        Log.i("STATION NAME: ", stationName);
        StationDatabase db = StationDatabase.getRoomDB(context);
        Station station = db.getStationDAO().getStationByName(stationName);
        Log.i("Station", station.getName());
        Log.i("ABBR: ", station.getAbbreviation());
        return station.getAbbreviation();
    }

}
