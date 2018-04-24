package com.zuk0.gaijinsmash.riderz.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static junit.framework.Assert.assertTrue;

public class TimeAndDateTest {

    String timeExample1 = "13:45";
    String timeExample2 = "00:23";
    String timeExample3 = "12:22 PM";
    String timeExample4 = "22:11:00 AM PST";
    String dateExample1 = "12-30-2017";
    String dateExample2 = "01-22-2018";

    @Test
    public void getTodaysDateTest() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
        String expectedDate = sdf.format(Calendar.getInstance().getTime());
        assertTrue("current date is returned properly", TimeAndDate.getTodaysDate().equals(expectedDate));
    }

    @Test
    public void getCurrentTimeTest() throws Exception {
        String expectedTime = new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime());
        assertTrue("current time is returned properly", TimeAndDate.getCurrentTime().equals(expectedTime));
    }

    @Test
    public void testDateFormatting() throws Exception {
        assertTrue("format date correctly to mm/dd/yyyy", TimeAndDate.formatDate(dateExample1).equals("12/30/2017"));
    }

    @Test
    public void testRegex() throws Exception {
        assertTrue("first whitespace should be eliminated", TimeAndDate.formatTime(timeExample3).equals("12:22PM"));
    }

    @Test
    public void format24hrTimeTest() throws Exception {
        String input = "21:12:00 AM PST";
        String result = TimeAndDate.format24hrTime(input);
        String expected = "21:12 PST";
        assertTrue("time format", expected.equals(result));

        String input2 = "01:22:00 AM PST";
        String result2 = TimeAndDate.format24hrTime(input);
        String expected2 = "01:22 PST";
        assertTrue("time format", expected.equals(result));
    }

    @Test
    public void convertTo12HrTest() throws Exception {
        String input = "17:11:00 PM PST";
        String result = TimeAndDate.convertTo12Hr(input);
        String expected = "5:11 PM PST";
        assertTrue("time format", expected.equals(result));

        String input2 = "24:04:00 AM PST";
        String result2 = TimeAndDate.convertTo12Hr(input);
        String expected2 = "12:04 AM PST";
        assertTrue("time format", expected.equals(result));
    }

}
