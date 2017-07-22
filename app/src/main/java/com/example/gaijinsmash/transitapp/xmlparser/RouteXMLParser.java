package com.example.gaijinsmash.transitapp.xmlparser;

import android.util.Log;
import android.util.Xml;

import com.example.gaijinsmash.transitapp.model.Station;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanj on 7/19/2017.
 */

public class RouteXMLParser {

    private static final boolean DEBUG = true; // True: turns on debug logging, False: off

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
    private static final String ns = null;

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
        List<Station> stationList = new ArrayList<Station>();
        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first tag
            if (name.equals("stations")) {
                if(DEBUG) {
                    Log.i("STATIONS tag: ", "MATCHED");
                }
                stationList = readStations(parser);
            } else {
                skip(parser);
            }
        }
        return stationList;
    }
}
