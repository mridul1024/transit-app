package com.zuk0.gaijinsmash.riderz.xml_adapter.etd;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.zuk0.gaijinsmash.riderz.debug.MyDebug;
import com.zuk0.gaijinsmash.riderz.model.bart.RealTimeEstimate;
import com.zuk0.gaijinsmash.riderz.network.FetchInputStream;
import com.zuk0.gaijinsmash.riderz.xml_adapter.XmlParserAbstract;
import com.zuk0.gaijinsmash.riderz.xml_adapter.XmlParserInterface;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RealTimeXmlParser implements XmlParserInterface {
    private static final String ns = null;
    private Context mContext;

    public RealTimeXmlParser(Context context) {
        this.mContext = context;
    }


    @Override
    public List<RealTimeEstimate> getList(String url) throws IOException, XmlPullParserException {
        if(MyDebug.DEBUG)
            Log.i("makeCall()", "with " + url);

        InputStream is = new FetchInputStream(mContext).connectToApi(url);
        List<RealTimeEstimate> results = parse(is);
        is.close();
        return results;
    }

    private List<RealTimeEstimate> parse(InputStream is) throws XmlPullParserException, IOException {
        if(MyDebug.DEBUG)
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
    public List<RealTimeEstimate> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        if(MyDebug.DEBUG)
            Log.d("readFeed", "***BEGINNING***");
        List<RealTimeEstimate> rteList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "root");
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("station")) {
                rteList = readStation(parser);
            } else {
                XmlParserAbstract.skip(parser);
            }
        }
        return rteList;
    }

    private List<RealTimeEstimate> readStation(XmlPullParser parser) throws XmlPullParserException, IOException{
        if(MyDebug.DEBUG)
            Log.d("readStation()", "***BEGINNING***");
        List<RealTimeEstimate> rteList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "station");
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("name")) {

            } else if(name.equals("abbr")) {

            } else if(name.equals("etd")){

            } else if(name.equals("destination")){

            } else if(name.equals("abbr")) {

            } else if(name.equals("minutes")) {

            }

            else {
                XmlParserAbstract.skip(parser);
            }
        }
        return rteList;
    }
}
