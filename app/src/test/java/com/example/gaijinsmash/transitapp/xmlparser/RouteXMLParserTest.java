package com.example.gaijinsmash.transitapp.xmlparser;

import android.content.Context;

import com.example.gaijinsmash.transitapp.network.xmlparser.RouteXMLParser;
import com.example.gaijinsmash.transitapp.network.xmlparser.StationXMLParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RouteXMLParserTest {

    static final String TEST_URI = "http://example.com";

    @Mock
    Context mMockContext;

    @Test
    public void RouteXMLParser_should_Return_True() throws Exception {
        assertTrue("", false);
    }
}
