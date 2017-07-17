package com.example.gaijinsmash.transitapp.xmlparser;

import android.util.Log;
import android.util.Xml;

import com.example.gaijinsmash.transitapp.internet.InternetOperations;
import com.example.gaijinsmash.transitapp.model.Station;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanj on 6/30/2017.
 */

public class StationXMLParser {

    private static final boolean DEBUG = true; // True: turns on debug logging, False: off
    private static final String API_KEY = "Q7Z9-PZ53-9QXT-DWE9";
    private static final String BASE_URI = "http://api.bart.gov/api/";
    private static final String TEST_URI = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V";

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
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

    private List readStations(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG) {
            Log.i("readStations()", "***BEGINNING***");
        }
        parser.require(XmlPullParser.START_TAG, ns, "stations");
        List<Station> stationList = new ArrayList<Station>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if(name.equals("station")) {
                stationList.add(readStationObject(parser));
            }
        }
        return stationList;
    }

    private Station readStationObject(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "station");
        String mName = null;
        String mAbbreviation = null;
        String mLatitude = null;
        String mLongitude = null;
        String mAddress = null;
        String mCity = null;
        String mCounty = null;
        String mState = null;
        String mZipcode = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if(name.equals("name")) {
                mName = readName(parser);
                if(DEBUG) {
                    Log.i("mName", mName);
                }
            }
            if (name.equals("abbr")) {
                mAbbreviation = readAbbr(parser);
                if(DEBUG) {
                    Log.i("abbreviation", mAbbreviation);
                }
            }
            if (name.equals("gtfs_latitude")) {
                mLatitude = readLatitude(parser);
                if(DEBUG) {
                    Log.i("latitude", mLatitude);
                }
            }
            if (name.equals("gtfs_longitude")) {
                mLongitude = readLongitude(parser);
                if(DEBUG) {
                    Log.i("longitude", mLongitude);
                }
            }
            if (name.equals("address")) {
                mAddress = readAddress(parser);
                if(DEBUG) {
                    Log.i("address", mAddress);
                }
            }
            if (name.equals("city")) {
                mCity = readCity(parser);
                if(DEBUG) {
                    Log.i("city", mCity);
                }
            }
            if (name.equals("county")) {
                mCounty = readCounty(parser);
                if(DEBUG) {
                    Log.i("county", mCounty);
                }
            }
            if (name.equals("state")) {
                mState = readState(parser);
                if(DEBUG) {
                    Log.i("state", mState);
                }
            }
            if (name.equals("zipcode")) {
                mZipcode = readZipcode(parser);
                if(DEBUG) {
                    Log.i("zipcode", mZipcode);
                }
            }
        }
        return new Station(mName);
    }

    //----------------------------------------------------------------------------------------------

    // Processes name tags in the StationInfo feed
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readName()", "***BEGINNING***");
        }

        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private String readAddress(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readAddress()", "***BEGINNING***");
        }

        parser.require(XmlPullParser.START_TAG, ns, "address");
        String address = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "address");
        return address;
    }

    private String readLatitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readLatitude()", "***BEGINNING***");
        }

        parser.require(XmlPullParser.START_TAG, ns, "gtfs_latitude");
        String latitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "gtfs_latitude");
        return latitude;
    }

    private String readLongitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readLongitude()", "***BEGINNING***");
        }

        parser.require(XmlPullParser.START_TAG, ns, "gtfs_longitude");
        String longitude = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "gtfs_longitude");
        return longitude;
    }

    private String readAbbr(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readAbbr()", "***BEGINNING***");
        }

        parser.require(XmlPullParser.START_TAG, ns, "abbr");
        String abbr = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "abbr");
        return abbr;
    }

    private String readCity(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readCity()", "***BEGINNING***");
        }

        parser.require(XmlPullParser.START_TAG, ns, "city");
        String city = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "city");
        return city;
    }

    private String readCounty(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readCounty()", "***BEGINNING***");
        }

        parser.require(XmlPullParser.START_TAG, ns, "county");
        String county = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "county");
        return county;
    }

    private String readState(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readState()", "***BEGINNING***");
        }

        parser.require(XmlPullParser.START_TAG, ns, "state");
        String state = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "state");
        return state;
    }

    private String readZipcode(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readZipcode()", "***BEGINNING***");
        }

        parser.require(XmlPullParser.START_TAG, ns, "zipcode");
        String zipcode = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "zipcode");
        return zipcode;
    }

    // Extract text values of tags:
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG) {
            Log.i("readText()", "***CONVERTING***");
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
