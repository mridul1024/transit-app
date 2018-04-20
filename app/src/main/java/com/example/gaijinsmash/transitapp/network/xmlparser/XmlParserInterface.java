package com.example.gaijinsmash.transitapp.network.xmlparser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public interface XmlParserInterface {

    Object getList(String url) throws IOException, XmlPullParserException;

    Object readFeed(XmlPullParser parser) throws XmlPullParserException, IOException;

}
