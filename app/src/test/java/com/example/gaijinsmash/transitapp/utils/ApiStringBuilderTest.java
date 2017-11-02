package com.example.gaijinsmash.transitapp.utils;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by ryanj on 9/17/2017.
 */

public class ApiStringBuilderTest {

     ApiStringBuilder builder;
     String stationA;
     String stationB;

    @Before
    public void setUp() throws Exception {
        builder = new ApiStringBuilder();
    }

    @Test
    public void getBSATest() throws Exception {
        String expected = "http://api.bart.gov/api/bsa.aspx?cmd=bsa&key=Q7Z9-PZ53-9QXT-DWE9";
        String actual = builder.getBSA();
        assertTrue("Method returns proper url string", expected.equals(actual));
    }
}
