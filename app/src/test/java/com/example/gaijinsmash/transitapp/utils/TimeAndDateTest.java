package com.example.gaijinsmash.transitapp.utils;

import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;

import java.sql.Time;

import static junit.framework.Assert.assertTrue;

public class TimeAndDateTest {

    String timeExample1 = "13:45";
    String timeExample2 = "00:23";
    String timeExample3 = "12:22 PM";
    String dateExample1 = "12-30-2017";
    String dateExample2 = "01-22-2018";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testTimeConversion() throws Exception {
        String result = TimeAndDate.convertTo12Hr(timeExample1);
        assertTrue("converts correctly to hh:mm PM", result.equals("01:45 PM"));
        assertTrue("converts correctly to hh:mm AM", TimeAndDate.convertTo12Hr(timeExample2).equals("12:23 AM"));
    }

    @Test
    public void testDateFormatting() throws Exception {
        assertTrue("format date correctly to mm/dd/yyyy", TimeAndDate.formatDate(dateExample1).equals("12/30/2017"));
    }

    @Test
    public void testRegex() throws Exception {
        assertTrue("whitespace should be eliminated", TimeAndDate.formatTime(timeExample3).equals("12:22PM"));
    }
}
