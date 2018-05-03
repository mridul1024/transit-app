package com.zuk0.gaijinsmash.riderz.utils;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class BartApiStringBuilderTest {

    private String API_BASE;
    private String API_KEY;

    @Before
    public void setUp() {
        API_BASE = "http://api.bart.gov/api/";
        API_KEY = "&key=Q7Z9-PZ53-9QXT-DWE9";
    }

    @Test
    public void getAllStationsTest() {
        String expected = API_BASE + "stn.aspx?cmd=stns" + API_KEY;
        String actual = BartApiStringBuilder.getAllStations();
        assertTrue("API strings are equal", expected.equals(actual));
    }


    @Test
    public void getBSATest() {
        String expected = "http://api.bart.gov/api/bsa.aspx?cmd=bsa&key=Q7Z9-PZ53-9QXT-DWE9";
        String actual = BartApiStringBuilder.getBSA();
        assertTrue("Method returns proper url string", expected.equals(actual));
    }

    @Test
    public void getRealTimeEstimateTest() {
        String expected = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=RICH&dir=n&key=Q7Z9-PZ53-9QXT-DWE9";
        String actual = BartApiStringBuilder.getRealTimeEstimate("RICH", "n");
        assertTrue("Method  returns proper API string", expected.equals(actual));
    }
}
