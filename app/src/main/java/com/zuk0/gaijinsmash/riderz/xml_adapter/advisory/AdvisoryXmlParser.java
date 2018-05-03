package com.zuk0.gaijinsmash.riderz.xml_adapter.advisory;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.zuk0.gaijinsmash.riderz.debug.MyDebug;
import com.zuk0.gaijinsmash.riderz.model.bart.Advisory;
import com.zuk0.gaijinsmash.riderz.network.FetchInputStream;
import com.zuk0.gaijinsmash.riderz.xml_adapter.XmlParserAbstract;
import com.zuk0.gaijinsmash.riderz.xml_adapter.XmlParserInterface;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AdvisoryXmlParser extends XmlParserAbstract implements XmlParserInterface {

    private Context mContext;
    private Advisory mAdvisory;

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
    private static final String ns = null;

    public AdvisoryXmlParser(Context context) {
        mContext = context;
        mAdvisory = new Advisory();
    }

    public List<Advisory> getList(String call) throws IOException, XmlPullParserException {
        if(MyDebug.DEBUG)
            Log.i("makeCall()", "with " + call);
        InputStream is = new FetchInputStream(mContext).connectToApi(call);
        List<Advisory> results = parse(is); //todo: fix this
        is.close();
        return results;
    }

    public List<Advisory> parse(InputStream in) throws IOException, XmlPullParserException {
        if(MyDebug.DEBUG) {
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

    public List<Advisory> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(MyDebug.DEBUG) {
            Log.i("readFeed():", "***BEGINNING***");
        }
        parser.require(XmlPullParser.START_TAG, ns, "root");
        List<Advisory> list = new ArrayList<>();
        String date;
        String time;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first tag
            switch (name) {
                case "date":
                    date = readDate(parser);
                    if (date != null)
                        mAdvisory.setDate(date); // breaks here
                    if (MyDebug.DEBUG)
                        Log.i("mDate", date);
                    break;
                case "time":
                    time = readTime(parser);
                    if (time != null)
                        mAdvisory.setTime(time);
                    if (MyDebug.DEBUG)
                        Log.i("mTime", time);
                    break;
                case "bsa":
                    if (MyDebug.DEBUG)
                        Log.i("BSA tag: ", "MATCHED");
                    list.add(readAdvisoryObject(parser));
                    break;
                default:
                    XmlParserAbstract.skip(parser);
                    break;
            }
        }
        list.add(mAdvisory);
        return list;
    }

    private Advisory readAdvisoryObject(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "bsa");
        String mStation = null;
        String mType = null;
        String mDescription = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            switch (name) {
                case "station":
                    mStation = readStation(parser);
                    if (MyDebug.DEBUG)
                        Log.i("station", mStation);
                    break;
                case "type":
                    mType = readType(parser);
                    if (MyDebug.DEBUG)
                        Log.i("type", mType);
                    break;
                case "description":
                    mDescription = readDescription(parser);
                    if (MyDebug.DEBUG)
                        Log.i("description", mDescription);
                    break;
            }
        }
        Advisory mAdvisory = new Advisory();
        mAdvisory.setStation(mStation);
        mAdvisory.setType(mType);
        mAdvisory.setDescription(mDescription);
        return mAdvisory;
    }
    //----------------------------------------------------------------------------------------------
    // Processes name tags in the BSA feed
    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(MyDebug.DEBUG)
            Log.i("readDate():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "date");
        String date = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "date");
        return date;
    }

    private String readTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(MyDebug.DEBUG)
            Log.i("readTime():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "time");
        String time = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "time");
        return time;
    }

    private String readStation(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(MyDebug.DEBUG)
            Log.i("readStation():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "station");
        String station = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "station");
        return station;
    }

    private String readType(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(MyDebug.DEBUG)
            Log.i("readType():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "type");
        String type = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "type");
        return type;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(MyDebug.DEBUG)
            Log.i("readDescription():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }
} // End of Class
