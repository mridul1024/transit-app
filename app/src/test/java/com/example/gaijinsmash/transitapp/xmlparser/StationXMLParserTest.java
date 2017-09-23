package com.example.gaijinsmash.transitapp.xmlparser;

import android.content.Context;

import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.network.xmlparser.StationXMLParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * Created by ryanj on 8/23/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class StationXMLParserTest {

    static final String TEST_URI = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V";

    @Mock
    Context mMockContext;

    @Before
    public void setupTest() {

    }

    @Test
    public void testXmlParser_shouldReturnTrue() throws IOException, XmlPullParserException {
        assertTrue("Stations list is not null", testCall() != null);
    }

    public List testCall() throws IOException, XmlPullParserException {
        StationXMLParser stationXMLParser = new StationXMLParser(mMockContext);
        return stationXMLParser.getStations();
    }
}
