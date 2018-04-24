package com.zuk0.gaijinsmash.riderz.network.xmlparser;

import android.content.Context;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class StationXMLParserTest {

    static final String TEST_URI = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V";

    @Mock
    Context mMockContext;

    @Before
    public void setupTest() {

    }


}
