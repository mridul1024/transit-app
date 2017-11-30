package com.example.gaijinsmash.transitapp.model;

import com.example.gaijinsmash.transitapp.model.bart.Advisory;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class AdvisoryTest {

    protected Advisory advisory;

    @Before
    public void setUp() throws Exception {
        advisory = new Advisory();
        advisory.setTime("01:45:00 PM PST");
    }

    @Test
    public void getTwentyFourHourFormatTest() throws Exception {
        String time = advisory.getTime();
        String time2 = advisory.getTwentyFourHr();
        System.out.println(time);
        System.out.println(time2);
        assertTrue(time2.equals("13:45:00"));
        assertTrue(time != time2);

        // check 24 hour format is produced
    }
}
