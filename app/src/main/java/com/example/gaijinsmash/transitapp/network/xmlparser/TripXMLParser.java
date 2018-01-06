package com.example.gaijinsmash.transitapp.network.xmlparser;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.example.gaijinsmash.transitapp.database.StationDAO;
import com.example.gaijinsmash.transitapp.database.StationDatabase;
import com.example.gaijinsmash.transitapp.model.bart.Fare;
import com.example.gaijinsmash.transitapp.model.bart.Leg;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.model.bart.Trip;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.utils.PermissionUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TripXMLParser implements XmlParserInterface {

    private Context mContext = null;
    private static final boolean DEBUG = true;

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
    private static final String ns = null;

    public TripXMLParser(Context mContext) {
        if(this.mContext == null)
            this.mContext = mContext;
    }

    public List<Trip> getList(String url) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("makeCall()", "with " + url);
        InputStream is = new FetchInputStream(mContext).connectToApi(url);
        List<Trip> results = parse(is);
        return results;
    }

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        if(DEBUG)
            Log.i("parse()", "***BEGINNING***");
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    public List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG)
            Log.i("readFeed():", "***BEGINNING***");

        List<Trip> tripList = new ArrayList<Trip>();
        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first tag
            if (name.equals("schedule")) {
                if(DEBUG)
                    Log.i("SCHEDULE tag: ", "MATCHED");
                tripList = readSchedule(parser);
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return tripList;
    }

    private List readSchedule(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG)
            Log.i("readSchedule()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "schedule");
        List<Trip> tripList = new ArrayList<Trip>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("request")) {
                Log.i("REQUESTS tag: ", "MATCHED");
                tripList = readRequest(parser);
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return tripList;
    }

    private List readRequest(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG)
            Log.i("readRequest()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "request");
        List<Trip> tripList = new ArrayList<Trip>();
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("trip")) {
                tripList.add(readTripObject(parser));
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return tripList;
    }

    private Trip readTripObject(XmlPullParser parser) {
        String origin = "";
        String destination = "";
        String fare = "";
        String origTimeMin = "";
        String origTimeDate= "";
        String destTimeMin = "";
        String destTimeDate = "";
        String clipper = "";
        String tripTime = "";
        String co2 = "";

        try {
            parser.require(XmlPullParser.START_TAG, ns, "trip");
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_TAG) {
                String name = parser.getName();
                Log.i("name", name);
                if(name.equals("trip")) {
                    origin = parser.getAttributeValue(null, "origin"); // abbr
                    if (DEBUG)
                        Log.i("origin", origin);
                    destination = parser.getAttributeValue(null, "destination"); // abbr
                    if (DEBUG)
                        Log.i("destination", destination);
                    fare = parser.getAttributeValue(null, "fare"); // BigDecimal or Currency
                    if (DEBUG)
                        Log.i("fare: ", fare);
                    origTimeMin = parser.getAttributeValue(null, "origTimeMin");
                    if (DEBUG)
                        Log.i("origTimeMin", origTimeMin);
                    origTimeDate = parser.getAttributeValue(null, "origTimeDate");
                    if (DEBUG)
                        Log.i("origTimeDate", origTimeDate);
                    destTimeMin = parser.getAttributeValue(null, "destTimeMin");
                    if (DEBUG)
                        Log.i("destTimeMin", destTimeMin);
                    destTimeDate = parser.getAttributeValue(null, "destTimeDate");
                    if (DEBUG)
                        Log.i("destTimeDate", destTimeDate);
                    clipper = parser.getAttributeValue(null, "clipper");
                    if (DEBUG)
                        Log.i("clipper", clipper);
                    tripTime = parser.getAttributeValue(null, "tripTime");
                    if (DEBUG)
                        Log.i("tripTime", tripTime);
                    co2 = parser.getAttributeValue(null, "co2");
                    if (DEBUG)
                        Log.i("co2", co2);
                } else {
                    XmlParserAbstract.skip(parser);
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        Trip trip = new Trip();

        // TODO: need to convert abbr to full names
        trip.setOrigin(origin);
        trip.setDestination(destination);
        trip.setFare(fare);
        trip.setOrigTimeMin(origTimeMin);
        trip.setOrigTimeDate(origTimeDate);
        trip.setDestTimeMin(destTimeMin);
        trip.setDestTimeDate(destTimeDate);
        trip.setClipper(clipper);
        trip.setTripTime(tripTime);
        trip.setCo2(co2);
        return trip;
    }

    private class GetStationTask extends AsyncTask<Void, Void, String[]> {
        private Context mContext;
        private Trip mTrip;
        private String mOriginAbbr;
        private String mDestAbbr;

        public GetStationTask(Context context, Trip trip, String originAbbr, String destAbbr) {
            mContext = context;
            mTrip = trip;
            mOriginAbbr = originAbbr;
            mDestAbbr = destAbbr;
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            String[] list = new String[2];
            StationDatabase db = StationDatabase.getRoomDB(mContext);
            Station originStation = db.getStationDAO().getStationByAbbr(mOriginAbbr);
            Station destStation = db.getStationDAO().getStationByAbbr(mDestAbbr);
            list[0] = (originStation.getName());
            list[1] = (destStation.getName());
            return list;
        }

        @Override
        protected void onPostExecute(String[] stationList) {
            mTrip.setOrigin(stationList[0]);
            mTrip.setDestination(stationList[1]);
        }
    }

}
