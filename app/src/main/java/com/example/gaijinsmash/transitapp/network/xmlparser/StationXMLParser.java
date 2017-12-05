package com.example.gaijinsmash.transitapp.network.xmlparser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanj on 6/30/2017.
 */

public class StationXMLParser implements XmlParserInterface {

    private Context mContext = null;
    private static final boolean DEBUG = true;

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
    private static final String ns = null;

    public StationXMLParser(Context mContext) {
        if(this.mContext == null)
            this.mContext = mContext;
    }

    // TODO: remove this method?
    public List<Station> getList(String call) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("makeCall()", "with " + call);
        InputStream is = new FetchInputStream(mContext).connectToApi(call);
        List<Station> results = parse(is);
        is.close();
        return results;
    }

    public List<Station> parse(InputStream is) throws XmlPullParserException, IOException {
        if(DEBUG)
            Log.i("parse()", "***BEGINNING***");

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            is.close();
        }
    }

    public List<Station> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG)
            Log.i("readFeed():", "***BEGINNING***");

        List<Station> stationList = new ArrayList<Station>();
        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first tag
            if (name.equals("stations")) {
                if(DEBUG)
                    Log.i("STATIONS tag: ", "MATCHED");
                stationList = readStations(parser);
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return stationList;
    }

    private List<Station> readStations(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG)
            Log.i("readStations()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "stations");
        List<Station> stationList = new ArrayList<Station>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("station")) {
                stationList.add(readStationObject(parser));
            } else {
                XmlParserAbstract.skip(parser);
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
                if(DEBUG)
                    Log.i("mName", mName);
            }
            else if (name.equals("abbr")) {
                mAbbreviation = readAbbr(parser);
                if(DEBUG)
                    Log.i("abbreviation", mAbbreviation);
            }
            else if (name.equals("gtfs_latitude")) {
                mLatitude = readLatitude(parser);
                if(DEBUG)
                    Log.i("latitude", mLatitude);
            }
            else if (name.equals("gtfs_longitude")) {
                mLongitude = readLongitude(parser);
                if(DEBUG)
                    Log.i("longitude", mLongitude);
            }
            else if (name.equals("address")) {
                mAddress = readAddress(parser);
                if(DEBUG)
                    Log.i("address", mAddress);
            }
            else if (name.equals("city")) {
                mCity = readCity(parser);
                if(DEBUG)
                    Log.i("city", mCity);
            }
            else if (name.equals("county")) {
                mCounty = readCounty(parser);
                if(DEBUG)
                    Log.i("county", mCounty);
            }
            else if (name.equals("state")) {
                mState = readState(parser);
                if(DEBUG)
                    Log.i("state", mState);
            }
            else if (name.equals("zipcode")) {
                mZipcode = readZipcode(parser);
                if(DEBUG)
                    Log.i("zipcode", mZipcode);
            }
        }

        Station mStation = new Station(mName);
        mStation.setAbbreviation(mAbbreviation);
        mStation.setLatitude(Double.parseDouble(mLatitude));
        mStation.setLongitude(Double.parseDouble(mLongitude));
        mStation.setAddress(mAddress);
        mStation.setCity(mCity);
        mStation.setCounty(mCounty);
        mStation.setState(mState);
        mStation.setZipcode(mZipcode);
        return mStation;
    }
    //----------------------------------------------------------------------------------------------
    // Processes name tags in the StationInfo feed

    //TODO: refactor into simple method that takes two parameters(XmlPullParser and String)
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readName()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = XmlParserAbstract.readText(parser);

        // ADD REGEX to modify "international"
        name.replace("International", "Int'l");
        //name.replaceAll("International", "Int'l");
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private String readAddress(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readAddress()", "***BEGINNING***");


        parser.require(XmlPullParser.START_TAG, ns, "address");
        String address = XmlParserAbstract.readText(parser);
        address.replaceAll("International", "Int'l");
        parser.require(XmlPullParser.END_TAG, ns, "address");
        return address;
    }

    private String readLatitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readLatitude()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "gtfs_latitude");
        String latitude = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "gtfs_latitude");
        return latitude;
    }

    private String readLongitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readLongitude()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "gtfs_longitude");
        String longitude = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "gtfs_longitude");
        return longitude;
    }

    private String readAbbr(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readAbbr()", "***BEGINNING***");


        parser.require(XmlPullParser.START_TAG, ns, "abbr");
        String abbr = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "abbr");
        return abbr;
    }

    private String readCity(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readCity()", "***BEGINNING***");


        parser.require(XmlPullParser.START_TAG, ns, "city");
        String city = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "city");
        return city;
    }

    private String readCounty(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readCounty()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "county");
        String county = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "county");
        return county;
    }

    private String readState(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readState()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "state");
        String state = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "state");
        return state;
    }

    private String readZipcode(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readZipcode()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "zipcode");
        String zipcode = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "zipcode");
        return zipcode;
    }
}
