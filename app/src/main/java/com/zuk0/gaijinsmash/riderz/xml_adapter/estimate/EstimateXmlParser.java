package com.zuk0.gaijinsmash.riderz.xml_adapter.estimate;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.zuk0.gaijinsmash.riderz.debug.DebugController;
import com.zuk0.gaijinsmash.riderz.data.model.etd_response.Estimate;
import com.zuk0.gaijinsmash.riderz.data.model.Trip;
import com.zuk0.gaijinsmash.riderz.utils.FetchInputStream;
import com.zuk0.gaijinsmash.riderz.xml_adapter.XmlParserAbstract;
import com.zuk0.gaijinsmash.riderz.xml_adapter.XmlParserInterface;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
    NOTE: The class's getList() will return a Trip object that contains a List<Estimate>
 */

public class EstimateXmlParser implements XmlParserInterface {
    private static final String ns = null;
    private Context mContext; // todo: memory leak?
    private String mOrigin;

    public EstimateXmlParser(Context context) {
        this.mContext = context;
    }

    @Override
    public List<Trip> getList(String url) throws IOException, XmlPullParserException {
        if(DebugController.DEBUG)
            Log.i("makeCall()", "with " + url);
        InputStream is = new FetchInputStream(mContext).connectToApi(url);
        List<Trip> results = parse(is);
        is.close();
        return results;
    }

    private List<Trip> parse(InputStream is) throws XmlPullParserException, IOException {
        if(DebugController.DEBUG)
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

    @Override
    public List<Trip> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DebugController.DEBUG)
            Log.d("readFeed", "***BEGINNING***");
        List<Trip>  tripList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "root");
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            switch(name) {
                case "station":
                    if(DebugController.DEBUG) Log.d("station tag", "MATCHED");
                    tripList = readStation(parser);
                    break;
                default:
                    XmlParserAbstract.skip(parser);
                    break;
            }
        }
        return tripList;
    }

    private List<Trip> readStation(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "station");
        List<Trip> tripList = new ArrayList<>();

        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "name":
                    mOrigin = readName(parser);
                    if(DebugController.DEBUG) Log.d("name tag", "MATCHED");
                    break;
                case "etd":
                    //create a new Trip Object and add to TripList
                    tripList.add(readEtd(parser));
                    if(DebugController.DEBUG) Log.d("etd tag", "MATCHED");
                    break;
                default:
                    XmlParserAbstract.skip(parser);
                    break;
            }
        }
        return tripList;
    }
    private Trip readEtd(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "etd");
        Trip trip = new Trip();
        String destination = null;
        String abbr = null;
        List<Estimate> estimateList = new ArrayList<>();

        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "destination":
                    destination = readDestination(parser);
                    if(DebugController.DEBUG) Log.d("destination tag", "MATCHED");
                    break;
                case "abbreviation":
                    if(DebugController.DEBUG) Log.d("abbr tag", "MATCHED");
                    abbr = readAbbr(parser);
                    break;
                case "estimate":
                    if(DebugController.DEBUG) Log.d("estimate tag", "MATCHED");
                    estimateList.add(readEstimateObject(parser));
                    break;
                default:
                    XmlParserAbstract.skip(parser);
                    break;
            }
        }
        trip.setOrigin(mOrigin);          // full name, not abbr
        trip.setDestination(destination); // full name, not abbr
        trip.setEstimateList(estimateList);
        trip.setDestinationAbbr(abbr);
        return trip;
    }

    private Estimate readEstimateObject(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "estimate");
        String minutes = null;
        String platform = null;
        String direction = null;
        String color = null;
        String hexcolor = null;
        String bikeflag = null;
        Estimate estimate = new Estimate();

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "minutes":
                    minutes = readMinutes(parser);
                    if(DebugController.DEBUG)
                        Log.d("minutes", minutes);
                    break;
                case "platform":
                    platform = readPlatform(parser);
                    if(DebugController.DEBUG)
                        Log.d("platform", platform);
                    break;
                case "direction":
                    direction = readDirection(parser);
                    if(DebugController.DEBUG)
                        Log.d("direction", direction);
                    break;
                case "color":
                    color = readColor(parser);
                    if(DebugController.DEBUG)
                        Log.d("color", color);
                    break;
                case "hexcolor":
                    hexcolor = readHexcolor(parser);
                    if(DebugController.DEBUG)
                        Log.d("hexcolor", hexcolor);
                    break;
                case "bikeflag":
                    bikeflag = readBikeflag(parser);
                    if(DebugController.DEBUG)
                        Log.d("bikeflag", bikeflag);
                    break;
                default:
                    XmlParserAbstract.skip(parser);
                    break;
            }
        }
        estimate.setMinutes(minutes);
        estimate.setPlatform(Integer.valueOf(platform));
        estimate.setDirection(direction);
        estimate.setColor(color);
        estimate.setHexcolor(hexcolor);
        estimate.setBikeflag(Integer.valueOf(bikeflag));
        return estimate;
    }

    private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }
    private String readDestination(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "destination");
        String destination = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "destination");
        return destination;
    }
    private String readAbbr(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "abbreviation");
        String abbr = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "abbreviation");
        return abbr;
    }
    private String readMinutes(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "minutes");
        String minutes = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "minutes");
        return minutes;
    }
    private String readPlatform(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "platform");
        String platform = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "platform");
        return platform;
    }

    private String readDirection(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "direction");
        String direction = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "direction");
        return direction;
    }
    private String readColor(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "color");
        String color = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "color");
        return color;
    }
    private String readHexcolor(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "hexcolor");
        String hexcolor = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "hexcolor");
        return hexcolor;
    }
    private String readBikeflag(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "bikeflag");
        String bikeflag = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bikeflag");
        return bikeflag;
    }
}
