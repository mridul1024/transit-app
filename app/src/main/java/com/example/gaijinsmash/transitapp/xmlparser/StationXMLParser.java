package com.example.gaijinsmash.transitapp.xmlparser;

import android.util.Log;
import android.util.Xml;

import com.example.gaijinsmash.transitapp.internet.InternetOperations;
import com.example.gaijinsmash.transitapp.model.Station;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanj on 6/30/2017.
 */

public class StationXMLParser {

    private static final boolean DEBUG = true; // True: turns on debug logging, False: off
    private static final String API_KEY = "Q7Z9-PZ53-9QXT-DWE9";
    private static final String BASE_URI = "http://api.bart.gov/api/";
    private static final String TEST_URI = "http://api.bart.gov/api/stn.aspx?cmd=stninfo&orig=ssan";

    // TODO: What is ns for?
    private static final String ns = null;

    // For testing purposes only
    public List testCall() throws IOException, XmlPullParserException {
        return makeCall(TEST_URI);
    }

    public String getApiString() {
            // TODO: need to add ability to customize calls
            return BASE_URI;
    }

    // Insert the API URL in "call"
    public List makeCall(String call) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("makeCall()", "with " + call);
        }

        InputStream is = InternetOperations.connectToApi(call);
        List results = parse(is);

        return results;
    }

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        if(DEBUG) {
            Log.i("begin parse()", "with " + in.toString());
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
            // TODO: Finish debug logic
        }
        List entries = new ArrayList();
        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                entries.add(readStation(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Station readStation(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG) {
            // TODO: Finish debug logic
        }

        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String mName = null;
        String mAddress = null;
        String mAbbreviation = null;
        String mCity = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("name")) {
                mName = readName(parser);
            } else if (name.equals("address")) {
                mAddress = readAddress(parser);
            } else if (name.equals("abbreviation")) {
                mAbbreviation = readAbbr(parser);
            } else if (name.equals("city")) {
                mCity = readCity(parser);
            }
        }

        // TODO: add more data to Station Object after parsing
        return new Station(mName);
    }

    //----------------------------------------------------------------------------------------------

    // Processes name tags in the StationInfo feed
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            // TODO: Finish debug logic
        }

        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private String readAddress(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            // TODO: Finish debug logic
        }

        parser.require(XmlPullParser.START_TAG, ns, "abbreviation");
        String abbr = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "abbreviation");
        return abbr;
    }

    private String readAbbr(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            // TODO: Finish debug logic
        }

        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private String readCity(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            // TODO: Finish debug logic
        }

        parser.require(XmlPullParser.START_TAG, ns, "city");
        String city = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "city");
        return city;
    }

    // Extract text values of tags:
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            // TODO: Finish debug logic
        }

        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skip tags that it's not interested in
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG) {
            // TODO: Finish debug logic
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
