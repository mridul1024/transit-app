package com.example.gaijinsmash.transitapp.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(c.getTime());
    }

    // returns in 12 hour format
    public static String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("hh:mm a", Locale.US);
        Date date = df.getCalendar().getTime();
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return df.format(date);
    }

    public static String formatTime(String input) {
        return input.replaceFirst("\\s", "");
    }

    // Format date for api string - mm/dd/yyyy
    public static String formatDate(String input) {
        String output = "";
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("mm/dd/yyyy");
        output = sdf.format(date);
        return output;
    }

    // Remove am/pm from 24hour time
    public static String format24hrTime(String input) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a z");
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("HH:mm z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return sdf.format(date);
    }

    public static String convertTo12Hr(String input) {
        Date date = null;
        DateFormat df = new SimpleDateFormat("HH:mm:ss a z", Locale.US);
        try {
            date = df.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat printFormat = new SimpleDateFormat("hh:mm a z");
        printFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return printFormat.format(date);
    }

    //todo: check if time is returning properly to API call
    public static String convertTo12HrForTrip(String input) {
        String output = "";
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            date = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("hh:mm a");
        output = sdf.format(date);
        return output;
    }

    public static int getCurrentHour() {
        DateFormat df = new SimpleDateFormat("HH", Locale.US);
        Date date = df.getCalendar().getTime();
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String time = df.format(date);
        Log.i("TIME HOUR", time);
        return  Integer.valueOf(time);
    }
}
