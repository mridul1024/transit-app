package com.example.gaijinsmash.transitapp.database;

import android.content.Context;

import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.network.xmlparser.StationXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Facade Pattern to simplify actions to database
 */

public class StationDbFacade {
    private Context mContext;
    private StationDatabase mDb = null;

    public StationDbFacade(Context context) {
        this.mContext = context;
        if (mContext != null) {
            try {
                mDb = StationDatabase.getRoomDB(mContext);
                populateDB(mContext, mDb);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // populate db with objects
    private void populateDB(Context context, StationDatabase db) throws XmlPullParserException, IOException {
        StationXMLParser parser = new StationXMLParser(context);
        List<Station> list = parser.getAllStations();
        for(Station station : list) {
            db.getStationDAO().addStation(station);
        }
    }

    // ADD
    public void addStation(Station station) {
        mDb.getStationDAO().addStation(station);
    }

    // Delete
    public void deleteStation(Station station) {
        mDb.getStationDAO().delete(station);
    }

    // Update
    public void updateStation(Station station) {
        mDb.getStationDAO().updateStation(station);
    }

    // Find
    public Station getStation(Station station) {
        String abbr = station.getAbbreviation();
        return mDb.getStationDAO().getStation(abbr);
    }

    // Find ALL
    public List<Station> getAllStations() {
        List<Station> list = mDb.getStationDAO().getAllStations();
        return list;
    }
}
