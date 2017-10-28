package com.example.gaijinsmash.transitapp.network.xmlparser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.model.bart.Route;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RouteXMLParser {

    private Context mContext = null;
    private static final boolean DEBUG = true;

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
    private static final String ns = null;

    public RouteXMLParser(Context mContext) {
        if(this.mContext == null)
            this.mContext = mContext;
    }

    public List makeCall(String url) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("makeCall()", "with " + url);
        }
        InputStream is = new FetchInputStream(mContext).connectToApi(url);
        List results = parse(is);
        return results;
    }

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        if(DEBUG) {
            Log.i("parse()", "***BEGINNING***");
        }
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

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG) {
            Log.i("readFeed():", "***BEGINNING***");
        }

        List<Route> routeList = new ArrayList<Route>();

        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first tag
            if (name.equals("schedule")) {
                if(DEBUG) {
                    Log.i("SCHEDULE tag: ", "MATCHED");
                }
                routeList = readSchedule(parser);
            }
            else if (name.equals("message")) {
                if(DEBUG) {
                    Log.i("MESSAGE tag: ", "MATCHED");
                }
                // TODO: create message object and display to screen
            }
            else {
                skip(parser);
            }
        }
        return routeList;
    }

    private List readSchedule(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG) {
            Log.i("readSchedule()", "***BEGINNING***");
        }
        parser.require(XmlPullParser.START_TAG, ns, "schedule");
        List<Route> routeList = new ArrayList<Route>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("request")) {
                routeList.add(readRouteObject(parser));
            }
        }
        return routeList;
    }


    //TODO: Refactor this
    private Route readRouteObject(XmlPullParser parser)  throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "trip");
        String origin = null;
        String destination = null;
        String fare = null;
        String origTimeMin = null;
        String origTimeDate = null;
        String destTimeMin = null;
        String destTimeDate = null;
        String clipper = null;
        String tripTime = null;

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            String relType = parser.getAttributeValue(null, "");
            if(name.equals("trip")) {
                origin = parser.getAttributeValue(null, "origin");
                destination = parser.getAttributeValue(null, "destination");
                fare = parser.getAttributeValue(null, "fare");
                origTimeMin = parser.getAttributeValue(null, "origTimeMin");
                origTimeDate = parser.getAttributeValue(null, "origTimeDate");
                destTimeMin = parser.getAttributeValue(null, "destTimeMin");
                destTimeDate = parser.getAttributeValue(null, "destTimeDate");
                clipper = parser.getAttributeValue(null, "clipper");
                tripTime = parser.getAttributeValue(null, "tripTime");
            }
            if(name.equals("leg")) {
                // do something
            }
        }

        Route route = new Route();
        route.setDestination(destination);
        route.setOrigin(origin);
        route.setFare(fare);
        return route;
    }

    // Skip tags that it's not interested in
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG) {
            Log.i("skip()", "***SKIPPING***");
        }

        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
