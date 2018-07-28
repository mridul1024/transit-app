package com.zuk0.gaijinsmash.riderz.xml_adapter.trip;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.zuk0.gaijinsmash.riderz.debug.DebugController;
import com.zuk0.gaijinsmash.riderz.data.model.Fare;
import com.zuk0.gaijinsmash.riderz.data.model.FullTrip;
import com.zuk0.gaijinsmash.riderz.data.model.Leg;
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

public class TripXMLParser implements XmlParserInterface {

    private Context mContext = null;

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
    private static final String ns = null;

    /**
     *
     * NOTE: All station names are returned abbreviated and they will be stored as such
     *          in the FullTrip objects. Therefore, you will need to make a call to the
     *          StationDatabase to fetch the full names later on.
     *
     */

    public TripXMLParser(Context mContext) {
        if(this.mContext == null)
            this.mContext = mContext;
    }

    public List<FullTrip> getList(String url) throws IOException, XmlPullParserException {
        if(DebugController.DEBUG)
            Log.i("makeCall()", "with " + url);
        InputStream is = new FetchInputStream(mContext).connectToApi(url);
        return parse(is);
    }

    private List<FullTrip> parse(InputStream in) throws XmlPullParserException, IOException {
        if(DebugController.DEBUG)
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

    public List<FullTrip> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DebugController.DEBUG)
            Log.i("readFeed():", "***BEGINNING***");
        List<FullTrip> tripList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first tag
            if (name.equals("schedule")) {
                if(DebugController.DEBUG)
                    Log.i("SCHEDULE tag: ", "MATCHED");
                tripList = readSchedule(parser);
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return tripList;
    }

    private List<FullTrip> readSchedule(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DebugController.DEBUG)
            Log.i("readSchedule()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "schedule");
        List<FullTrip> tripList = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("request")) {
                tripList = readRequest(parser);
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return tripList;
    }

    private List<FullTrip> readRequest(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DebugController.DEBUG)
            Log.i("readRequest()", "***BEGINNING***");
        parser.require(XmlPullParser.START_TAG, ns, "request");
        List<FullTrip> tripList = new ArrayList<>();
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if(name.equals("trip")) {
                tripList.add(readFullTripObject(parser));
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return tripList;
    }

    private FullTrip readFullTripObject(XmlPullParser parser) throws XmlPullParserException, IOException {
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
        List<Leg> legList = new ArrayList<>();
        List<Fare> fareList = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "trip");
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_TAG) {
            String name = parser.getName();
            switch (name) {
                case "trip":
                    origin = parser.getAttributeValue(null, "origin"); // abbr

                    if (DebugController.DEBUG)
                        Log.i("origin", origin);
                    destination = parser.getAttributeValue(null, "destination"); // abbr

                    if (DebugController.DEBUG)
                        Log.i("destination", destination);
                    fare = parser.getAttributeValue(null, "fare"); // BigDecimal or Currency

                    if (DebugController.DEBUG)
                        Log.i("fare: ", fare);
                    origTimeMin = parser.getAttributeValue(null, "origTimeMin");
                    if (DebugController.DEBUG)
                        Log.i("origTimeMin", origTimeMin);
                    origTimeDate = parser.getAttributeValue(null, "origTimeDate");
                    if (DebugController.DEBUG)
                        Log.i("origTimeDate", origTimeDate);
                    destTimeMin = parser.getAttributeValue(null, "destTimeMin");
                    if (DebugController.DEBUG)
                        Log.i("destTimeMin", destTimeMin);
                    destTimeDate = parser.getAttributeValue(null, "destTimeDate");
                    if (DebugController.DEBUG)
                        Log.i("destTimeDate", destTimeDate);
                    clipper = parser.getAttributeValue(null, "clipper");
                    if (DebugController.DEBUG)
                        Log.i("clipper", clipper);
                    tripTime = parser.getAttributeValue(null, "tripTime");
                    if (DebugController.DEBUG)
                        Log.i("tripTime", tripTime);
                    co2 = parser.getAttributeValue(null, "co2");
                    if (DebugController.DEBUG)
                        Log.i("co2", co2);
                    break;
                case "fares":
                    fareList = readFares(parser);
                    break;
                case "leg":
                    legList.add(readLegObject(parser));
                    break;
                default:
                    XmlParserAbstract.skip(parser);
                    break;
            }
            eventType = parser.next();
        }

        Trip trip = new Trip();
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

        FullTrip fullTrip = new FullTrip();
        fullTrip.setTrip(trip);
        fullTrip.setFareList(fareList);
        fullTrip.setLegList(legList);
        return fullTrip;
    }

    private List<Fare> readFares(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DebugController.DEBUG)
            Log.i("readFares()", "***BEGINNING***");
        List<Fare> fareList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "fares");
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("fare")) {
                fareList.add(readFareObject(parser));
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return fareList;
    }

    private Fare readFareObject(XmlPullParser parser) throws XmlPullParserException, IOException{
        String fareAmount = "";
        String fareClass = "";
        String fareName = "";

        parser.require(XmlPullParser.START_TAG, ns, "fare");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_TAG) {
            String name = parser.getName();
            if(name.equals("fare")) {
                fareAmount = parser.getAttributeValue(null,"amount");
                if(DebugController.DEBUG){
                    Log.i("amount", fareAmount);
                }
                fareClass = parser.getAttributeValue(null, "class");
                if(DebugController.DEBUG){
                    Log.i("class", fareClass);
                }
                fareName = parser.getAttributeValue(null, "name");
                if(DebugController.DEBUG) {
                    Log.i("name", fareName);
                }
            }
            else {
                XmlParserAbstract.skip(parser);
            }
            eventType = parser.next();
        }

        Fare fare = new Fare();
        fare.setFareAmount(fareAmount);
        fare.setFareClass(fareClass);
        fare.setFareName(fareName);
        return fare;
    }

    private Leg readLegObject(XmlPullParser parser) throws XmlPullParserException, IOException {
        int order = 0;
        //int transferCode = 0;
        String line = "";
        int bikeFlag = 0;
        String trainHeadStation = "";
        String origTimeMin = "";
        String origTimeDate = "";
        String destTimeMin = "";
        String destTimeDate = "";
        String origin = "";
        String destination = "";

        parser.require(XmlPullParser.START_TAG, ns, "leg");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_TAG) {
            String name = parser.getName();
            if(name.equals("leg")) {
                order = Integer.parseInt(parser.getAttributeValue(null,"order"));
                if(DebugController.DEBUG)
                    Log.i("order", String.valueOf(order));
                /*
                if(!parser.getAttributeValue(null,"transfercode").equals("")) {
                    transferCode = Integer.parseInt(parser.getAttributeValue(null, "transfercode"));
                    if(DebugController.DEBUG)
                        Log.i("transferCode", String.valueOf(transferCode));
                }
                */
                line = parser.getAttributeValue(null, "line");
                if(DebugController.DEBUG)
                    Log.i("line", String.valueOf(line));
                bikeFlag = Integer.parseInt(parser.getAttributeValue(null, "bikeflag"));
                if(DebugController.DEBUG)
                    Log.i("bikeFlag", String.valueOf(bikeFlag));
                trainHeadStation = parser.getAttributeValue(null,"trainHeadStation");
                if(DebugController.DEBUG)
                    Log.i("trainHeadStation", trainHeadStation);
                origTimeDate = parser.getAttributeValue(null, "origTimeDate");
                if(DebugController.DEBUG)
                    Log.i("origTimeDate", origTimeDate);
                origTimeMin = parser.getAttributeValue(null,"origTimeMin");
                if(DebugController.DEBUG)
                    Log.i("origTimeMin", origTimeMin);
                destTimeDate = parser.getAttributeValue(null,"destTimeDate");
                if(DebugController.DEBUG)
                    Log.i("destTimeDate", destTimeDate);
                destTimeMin = parser.getAttributeValue(null, "destTimeMin");
                if(DebugController.DEBUG)
                    Log.i("destTimeMin", destTimeMin);
                origin = parser.getAttributeValue(null,"origin");
                if(DebugController.DEBUG)
                    Log.i("origin", origin);
                destination = parser.getAttributeValue(null, "destination");
                if(DebugController.DEBUG)
                    Log.i("destination", destination);
            }
            else {
                XmlParserAbstract.skip(parser);
            }
            eventType = parser.next();
        }

        Leg leg = new Leg();
        leg.setOrder(order);
        //leg.setTransferCode(transferCode);
        leg.setLine(line);
        leg.setBikeFlag(bikeFlag);
        leg.setTrainHeadStation(trainHeadStation);
        leg.setOrigTimeMin(origTimeMin);
        leg.setOrigTimeDate(origTimeDate);
        leg.setDestTimeDate(destTimeDate);
        leg.setDestTimeMin(destTimeMin);
        leg.setOrigin(origin);
        leg.setDestination(destination);
        return leg;
    }
}
