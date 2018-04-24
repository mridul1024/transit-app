package com.zuk0.gaijinsmash.riderz.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeAndDate {


    public static String getTodaysDate() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return sdf.format(c.getTime());
    }

    // returns in 24 hour format
    public static String getCurrentTime() {
        long millis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.HOUR_OF_DAY)+ ":" + calendar.get(Calendar.MINUTE);
    }

    public static String formatTime(String input) {
        return input.replaceFirst("\\s", "");
    }

    // Format date for api string - mm/dd/yyyy
    public static String formatDate(String input) {
        String output;
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
        output = sdf.format(date);
        return output;
    }

    // Remove am/pm from 24hour time
    public static String format24hrTime(String input) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a z", Locale.US);
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("HH:mm z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("PST8PDT"));
        //todo: PST8PDT timezone id isn't working properly
        return sdf.format(date);
    }

    public static String convertTo12Hr(String input) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a z", Locale.US);
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("hh:mm a z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("PST8PDT"));
        //todo: PST8PDT timezone id isn't working properly
        return sdf.format(date);
    }

    public static String convertTo12HrForTrip(String input) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("hh:mm a", Locale.US);
        return sdf.format(date);
    }

    public static int getCurrentHour() {
        DateFormat df = new SimpleDateFormat("HH", Locale.US);
        Date date = df.getCalendar().getTime();
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String time = df.format(date);
        return  Integer.valueOf(time);
    }
}
