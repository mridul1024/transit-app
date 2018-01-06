package com.example.gaijinsmash.transitapp.network.xmlparser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public interface XmlParserInterface {

    public List<?> getList(String url) throws IOException, XmlPullParserException;

    public List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException;

}
