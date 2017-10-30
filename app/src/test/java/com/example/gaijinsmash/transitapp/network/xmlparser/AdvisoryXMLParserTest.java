package com.example.gaijinsmash.transitapp.network.xmlparser;

import android.content.Context;

import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.network.xmlparser.AdvisoryXmlParser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.InputStream;

public class AdvisoryXMLParserTest {
    static final String TEST_URI = "http://api.bart.gov/api/bsa.aspx?cmd=bsa&key=Q7Z9-PZ53-9QXT-DWE9";
    AdvisoryXmlParser advisoryXmlParser;
    InputStream is;

    @Mock
    Context mContext;

    @Before
    void setUp() throws Exception {
        advisoryXmlParser = new AdvisoryXmlParser(mContext);
        is = new FetchInputStream(mContext).connectToApi(TEST_URI);
    }

    @Test
    void AdvisoryXmlParser_should_return_true() throws Exception {
        advisoryXmlParser.parse(is);
    }
}
