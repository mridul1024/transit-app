package com.example.gaijinsmash.transitapp.utils;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class ApiStringBuilderTest {

     ApiStringBuilder builder;
     String stationA;
     String stationB;
     String API_BASE;
     String API_KEY;

    @Before
    public void setUp() throws Exception {
        builder = new ApiStringBuilder();
        API_BASE = "http://api.bart.gov/api/";
        API_KEY = "&key=Q7Z9-PZ53-9QXT-DWE9";
    }

    @Test
    public void getAllStationsTest() {
        String expected = API_BASE + "stn.aspx?cmd=stns" + API_KEY;
        String actual = builder.getAllStations();
        assertTrue("API strings are equal", expected.equals(actual));
    }

    @Test
    public void getHolidayInfoTest() {
        String expected = API_BASE + "  " + API_KEY;
        String actual = builder.getHolidayInfo();
        assertTrue("API strings are equal", expected.equals(actual));
    }

    @Test
    public void getBSATest() throws Exception {
        String expected = "http://api.bart.gov/api/bsa.aspx?cmd=bsa&key=Q7Z9-PZ53-9QXT-DWE9";
        String actual = builder.getBSA();
        assertTrue("Method returns proper url string", expected.equals(actual));
    }
}
