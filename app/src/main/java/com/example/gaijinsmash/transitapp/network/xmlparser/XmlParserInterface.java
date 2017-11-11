package com.example.gaijinsmash.transitapp.network.xmlparser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by ryanj on 11/6/2017.
 */

public interface XmlParserInterface {

    public List<?> getList(String url) throws IOException, XmlPullParserException;

}