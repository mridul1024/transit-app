package com.example.gaijinsmash.transitapp.network.xmlparser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.example.gaijinsmash.transitapp.model.bart.Fare;
import com.example.gaijinsmash.transitapp.model.bart.Leg;
import com.example.gaijinsmash.transitapp.model.bart.Trip;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;

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
            }
            else if (name.equals("message")) {
                if(DEBUG)
                    Log.i("MESSAGE tag: ", "MATCHED");
                // TODO: create message object and display to screen
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
                if(DEBUG)
                    Log.i("trip", "matched!");
                tripList.add(readTripObject(parser));
            } else if(name.equals("fares")) {
                if(DEBUG)
                    Log.i("fares", "matched");
            } else if (name.equals("leg")) {
                if(DEBUG)
                    Log.i("leg","matched");
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return tripList;
    }

    private Trip readTripObject(XmlPullParser parser)  throws XmlPullParserException, IOException {
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
        String name = parser.getName();
        if(name.equals("trip")) {
            origin = parser.getAttributeValue(null, "origin"); // abbr
            if(DEBUG)
                Log.i("origin", origin);
            destination = parser.getAttributeValue(null, "destination"); // abbr
            if(DEBUG)
                Log.i("destination", destination);
            fare = parser.getAttributeValue(null, "fare"); // BigDecimal or Currency
            if(DEBUG)
                Log.i("fare: ", fare);
            origTimeMin = parser.getAttributeValue(null, "origTimeMin");
            if(DEBUG)
                Log.i("origTimeMin", origTimeMin);
            origTimeDate = parser.getAttributeValue(null, "origTimeDate");
            if(DEBUG)
                Log.i("origTimeDate", origTimeDate);
            destTimeMin = parser.getAttributeValue(null, "destTimeMin");
            if(DEBUG)
                Log.i("destTimeMin", destTimeMin);
            destTimeDate = parser.getAttributeValue(null, "destTimeDate");
            if(DEBUG)
                Log.i("destTimeDate", destTimeDate);
            clipper = parser.getAttributeValue(null, "clipper");
            if(DEBUG)
                Log.i("clipper", clipper);
            tripTime = parser.getAttributeValue(null, "tripTime");
            if(DEBUG)
                Log.i("tripTime", tripTime);
            co2 = parser.getAttributeValue(null, "co2");
            if(DEBUG)
                Log.i("co2", co2);
        }
        Trip trip = new Trip();
        trip.setOrigin(origin);
        trip.setDestination(destination);
        trip.setFare(fare);

        //todo: add more values
        return trip;
    }

    private List<Fare> readFares(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<Fare> faresList = new ArrayList<Fare>();
        parser.require(XmlPullParser.START_TAG, ns, "fares");
        String fareAmount, fareClass, fareName = null;

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if(name.equals("fare")) {
                fareAmount = parser.getAttributeValue(null, "amount");
                fareClass = parser.getAttributeValue(null, "class");
                fareName = parser.getAttributeValue(null, "name");
            }
        }

        return faresList;
    }

    private List<Leg> readLegs(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<Leg> legsList = new ArrayList<Leg> ();

        String name = parser.getName();
        if(name.equals("")) {

        }
        return legsList;
    }

    //----------------------------------------------------------------------------------------------
    // Processes name tags of Trip Objects
    private String readTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readTag() : ", tag);
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return result;
    }
}
