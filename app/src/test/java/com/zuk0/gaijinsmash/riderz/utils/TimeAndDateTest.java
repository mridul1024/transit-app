package com.zuk0.gaijinsmash.riderz.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static junit.framework.Assert.assertTrue;

public class TimeAndDateTest {

    @Test
    public void getTodaysDateTest() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
        String expectedDate = sdf.format(Calendar.getInstance().getTime());
        assertTrue("current date is returned properly", TimeAndDate.getTodaysDate().equals(expectedDate));
    }

    @Test
    public void getCurrentTimeTest() {
        String time = TimeAndDate.getCurrentTime();
        assertTrue("current time is returned properly", !time.equalsIgnoreCase(""));
    }

    @Test
    public void testFormatDate() {
        String dateExample1 = "12-30-2017";
        assertTrue("format date correctly to MM/dd/yyyy", TimeAndDate.formatDate(dateExample1).equals("12/30/2017"));
    }

    @Test
    public void testRegex() {
        String timeExample3 = "12:22 PM";
        assertTrue("first whitespace should be eliminated", TimeAndDate.formatTime(timeExample3).equals("12:22PM"));
    }

    @Test
    public void format24hrTimeTest() {
        String input = "21:12:00 AM PST";
        String result = TimeAndDate.format24hrTime(input);
        String expected = "21:12 PST";
        assertTrue("Time is converted properly", expected.equals(result));

        String input2 = "01:22:00 AM PST";
        String result2 = TimeAndDate.format24hrTime(input2);
        String expected2 = "01:22 PST";
        assertTrue("Time is converted properly", expected2.equals(result2));
    }

    /*
    @Test
    public void convertTo12HrTest() {
        String input = "17:11:00 PM PST";
        String result = TimeAndDate.convertTo12Hr(input);
        String expected = "5:11 PM PST";
        String expected2 = "4:11 PM PST";
        assertTrue("Time is converted properly", expected.equalsIgnoreCase(result) || expected2.equalsIgnoreCase(result));

        String input2 = "21:04:00 PM PST";
        String result2 = TimeAndDate.convertTo12Hr(input2);
        String expected3 = "09:04 PM PST";
        String expected4 = "08:04 PM PST";
        assertTrue("Time is converted properly", expected3.equalsIgnoreCase(result2) || expected4.equalsIgnoreCase(result2));
    }
    */
    @Test
    public void convertTo12HrForTripTest() {
        String input = "22:01";
        String expected = "10:01 PM";
        assertTrue("Time is converted properly", expected.equalsIgnoreCase(TimeAndDate.convertTo12HrForTrip(input)));
    }

    @Test
    public void getCurrentHourTest() {
        assertTrue("Integer value is returned", TimeAndDate.getCurrentHour() >= 0);
    }
}
