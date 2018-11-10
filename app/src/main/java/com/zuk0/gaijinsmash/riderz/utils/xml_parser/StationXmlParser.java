package com.zuk0.gaijinsmash.riderz.utils.xml_parser;

import android.util.Log;
import android.util.Xml;

import com.zuk0.gaijinsmash.riderz.BuildConfig;
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StationXmlParser {

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
    private static final String ns = null;

    private InputStream mInputStream;

    public StationXmlParser(InputStream is) {
        mInputStream = is;
    }

    public List<Station> getList() throws IOException, XmlPullParserException {
        List<Station> results = parse(mInputStream);
        mInputStream.close();
        return results;
    }

    private List<Station> parse(InputStream is) throws XmlPullParserException, IOException {
        if(BuildConfig.DEBUG)
            Log.d("parse()", "***BEGINNING***");

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

    private List<Station> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(BuildConfig.DEBUG)
            Log.d("readFeed():", "***BEGINNING***");

        List<Station> stationList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first tag
            if (name.equals("stations")) {
                stationList = readStations(parser);
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return stationList;
    }

    private List<Station> readStations(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(BuildConfig.DEBUG)
            Log.d("readStations()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "stations");
        List<Station> stationList = new ArrayList<>();
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
        String mPlatformInfo = null;
        String mIntro = null;
        String mCrossStreet = null;
        String mFood = null;
        String mShopping = null;
        String mAttraction = null;
        String mLink = null;

        // todo: change to switch case
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "name":
                    mName = readName(parser);
                    if (BuildConfig.DEBUG)
                        Log.i("mName", mName);
                    break;
                case "abbr":
                    mAbbreviation = readAbbr(parser);
                    if (BuildConfig.DEBUG)
                        Log.i("abbreviation", mAbbreviation);
                    break;
                case "gtfs_latitude":
                    mLatitude = readLatitude(parser);
                    if (BuildConfig.DEBUG)
                        Log.i("latitude", mLatitude);
                    break;
                case "gtfs_longitude":
                    mLongitude = readLongitude(parser);
                    if (BuildConfig.DEBUG)
                        Log.i("longitude", mLongitude);
                    break;
                case "address":
                    mAddress = readAddress(parser);
                    if (BuildConfig.DEBUG)
                        Log.i("address", mAddress);
                    break;
                case "city":
                    mCity = readCity(parser);
                    if (BuildConfig.DEBUG)
                        Log.i("city", mCity);
                    break;
                case "county":
                    mCounty = readCounty(parser);
                    if (BuildConfig.DEBUG)
                        Log.i("county", mCounty);
                    break;
                case "state":
                    mState = readState(parser);
                    if (BuildConfig.DEBUG)
                        Log.i("state", mState);
                    break;
                case "zipcode":
                    mZipcode = readZipcode(parser);
                    if (BuildConfig.DEBUG)
                        Log.i("zipcode", mZipcode);
                    break;
                case "platform_info":
                    mPlatformInfo = readPlatformInfo(parser);
                    break;
                case "intro":
                    mIntro = readIntro(parser);
                    break;
                case "cross_street":
                    mCrossStreet = readCrossStreet(parser);
                    break;
                case "food":
                    mFood = readFood(parser);
                    break;
                case "shopping":
                    mShopping = readShopping(parser);
                    break;
                case "attraction":
                    mAttraction = readAttraction(parser);
                    break;
                case "link":
                    mLink = readLink(parser);
                    break;
                default:
                    XmlParserAbstract.skip(parser);
                    break;
            }
        }

        Station station = new Station();
        station.setName(mName);
        station.setAbbr(mAbbreviation);
        station.setLatitude(Double.parseDouble(mLatitude));
        station.setLongitude(Double.parseDouble(mLongitude));
        station.setAddress(mAddress);
        station.setCity(mCity);
        station.setCounty(mCounty);
        station.setState(mState);
        station.setZipcode(mZipcode);
        station.setPlatformInfo(mPlatformInfo);
        station.setIntro(mIntro);
        station.setAttraction(mAttraction);
        station.setCrossStreet(mCrossStreet);
        station.setShopping(mShopping);
        station.setFood(mFood);
        station.setLink(mLink);
        return station;
    }
    //----------------------------------------------------------------------------------------------
    // Processes name tags in the StationInfo feed
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readName()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = XmlParserAbstract.readText(parser);

        // ADD REGEX to modify "international"
        //name.replace("International", "Int'l");
        //todo name.replaceAll("International", "Int'l");
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private String readAddress(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readAddress()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "address");
        String address = XmlParserAbstract.readText(parser);
        //todo address.replaceAll("International", "Int'l");
        parser.require(XmlPullParser.END_TAG, ns, "address");
        return address;
    }

    private String readLatitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readLatitude()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "gtfs_latitude");
        String latitude = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "gtfs_latitude");
        return latitude;
    }

    private String readLongitude(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readLongitude()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "gtfs_longitude");
        String longitude = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "gtfs_longitude");
        return longitude;
    }

    private String readAbbr(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readAbbr()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "abbr");
        String abbr = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "abbr");
        return abbr;
    }

    private String readCity(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readCity()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "city");
        String city = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "city");
        return city;
    }

    private String readCounty(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readCounty()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "county");
        String county = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "county");
        return county;
    }

    private String readState(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readState()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "state");
        String state = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "state");
        return state;
    }

    private String readZipcode(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readZipcode()", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "zipcode");
        String zipcode = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "zipcode");
        return zipcode;
    }

    private String readPlatformInfo(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readPlatformInfo()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "platform_info");
        String platformInfo = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "platform_info");
        return platformInfo;
    }

    private String readIntro(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readIntro()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "intro");
        String intro = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "intro");
        return intro;
    }

    private String readCrossStreet(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readCrossStreet()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "cross_street");
        String crossStreet = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cross_street");
        return crossStreet;
    }

    private String readFood(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readFood()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "food");
        String food = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "food");
        return food;
    }

    private String readShopping(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readShopping()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "shopping");
        String shopping = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "shopping");
        return shopping;
    }

    private String readAttraction(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readAttraction()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "attraction");
        String attraction = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "attraction");
        return attraction;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(BuildConfig.DEBUG)
            Log.i("readLink()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }
}