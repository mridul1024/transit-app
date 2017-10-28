package com.example.gaijinsmash.transitapp.network.xmlparser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.example.gaijinsmash.transitapp.model.bart.Advisory;
import com.example.gaijinsmash.transitapp.model.bart.Route;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.XMLFormatter;

/**
 * Created by ryanj on 10/27/2017.
 */

public class AdvisoryXmlParser extends XmlParserAbstract {

    private XmlParserAbstract xmlParserAbstract;
    private Context mContext = null;
    private static final boolean DEBUG = true;

    // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
    private static final String ns = null;

    public AdvisoryXmlParser(Context mContext) {
        if(mContext == null)
            this.mContext = mContext;
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
            if(name.equals("date")) {
                mDate = readDate(parser);
                if(DEBUG)
                    Log.i("mDate", mDate);
            }
            else if(name.equals("time")) {
                mTime = readTime(parser);
                if(DEBUG)
                    Log.i("mTime", mTime);
            }
            else if (name.equals("bsa")) {
                if(DEBUG) {
                    Log.i("BSA tag: ", "MATCHED");
                }
                list = readBSA(parser);
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
        return list;
    }

    public List readBSA(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(DEBUG) {
            Log.i("readBSA();", "***BEGINNING");
        }
        List<Advisory> list = new ArrayList<Advisory>();

        return list;
    }

    //----------------------------------------------------------------------------------------------
    // Processes name tags in the BSA feed
    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readDate():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "date");
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "date");
        return date;
    }

    private String readTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readTime():", "***BEGINNING***");

        parser.require(XmlPullParser.START_TAG, ns, "time");
        String time = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "time");
        return time;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        if(DEBUG)
            Log.i("readText():", "***CONVERTING***");
        String result = "";
        if(parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.next();
        }
        return result;
    }

    // From XmlParaserAbstract.java
    public void skip(XmlPullParser parser) throws XmlPullParserException, IOException {}

}
