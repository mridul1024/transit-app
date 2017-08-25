package com.example.gaijinsmash.transitapp.xml;

import com.example.gaijinsmash.transitapp.xmlparser.RouteXMLParser;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by ryanj on 8/23/2017.
 */

public class RouteXMLParserTest {

    @Before
    public void setupTest() throws Exception {
        String uri = "http://example.com";
        RouteXMLParser parser = new RouteXMLParser();
        parser.makeCall(uri);
    }

    @Test
    public void RouteXMLParser_should_Return_True() throws Exception {

    }
}
