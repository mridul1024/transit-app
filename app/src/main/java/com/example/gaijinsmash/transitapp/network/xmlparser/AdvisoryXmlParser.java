package com.example.gaijinsmash.transitapp.network.xmlparser;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.util.Xml;

import com.example.gaijinsmash.transitapp.model.bart.Advisory;
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AdvisoryXmlParser extends XmlParserAbstract implements XmlParserInterface {

    private XmlParserAbstract xmlParserAbstract;
    private Context mContext = null;
    private static final boolean DEBUG = true;
    private Advisory advisory = null;

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
    private static final String ns = null;

    public AdvisoryXmlParser(Context mContext) {
        if(mContext == null)
            this.mContext = mContext;
        advisory = new Advisory();
    }

    public List<Advisory> getList(String call) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("makeCall()", "with " + call);
        InputStream is = new FetchInputStream(mContext).connectToApi(call);
        List<Advisory> results = parse(is);
        is.close();
        return results;
    }

    public List parse(InputStream in) throws IOException, XmlPullParserException {
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

    public List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG) {
            Log.i("readFeed():", "***BEGINNING***");
        }
        parser.require(XmlPullParser.START_TAG, ns, "root");
        List<Advisory> list = new ArrayList<Advisory>();
        String mDate = null;
        String mTime = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the first tag
            // todo: change to switch statements
            if(name.equals("date")) {
                mDate = readDate(parser);
                if(mDate != null)
                    advisory.setDate(mDate); // breaks here
                if(DEBUG)
                    Log.i("mDate", mDate);
            }
            else if(name.equals("time")) {
                mTime = readTime(parser);
                if(mTime != null)
                    advisory.setTime(mTime);
                if(DEBUG)
                    Log.i("mTime", mTime);
            }
            else if (name.equals("bsa")) {
                if(DEBUG)
                    Log.i("BSA tag: ", "MATCHED");
                list.add(readAdvisoryObject(parser));
            }
            else {
                XmlParserAbstract.skip(parser);
            }
        }
        list.add(advisory);
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
            if(name.equals("station")) {
                mStation = readStation(parser);
                if(DEBUG)
                    Log.i("station", mStation);
            }
            else if(name.equals("type")) {
                mType = readType(parser);
                if(DEBUG)
                    Log.i("type", mType);
            }
            else if(name.equals("description")) {
                mDescription = readDescription(parser);
                if(DEBUG)
                    Log.i("description", mDescription);
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
        if(DEBUG)
            Log.i("readDate():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "date");
        String date = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "date");
        return date;
    }

    private String readTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readTime():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "time");
        String time = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "time");
        return time;
    }

    private String readStation(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readStation():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "station");
        String station = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "station");
        return station;
    }

    private String readType(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readType():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "type");
        String type = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "type");
        return type;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readDescription():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = XmlParserAbstract.readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }
} // End of Class
