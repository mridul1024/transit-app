package com.example.gaijinsmash.transitapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        return String.format("%02d:%02d", hour, minute);
    }

    public static String convertTo12Hr(String input) {
        String output = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date date = sdf.parse(input);
            output = new SimpleDateFormat("hh:mm a").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
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

    // Format time by eliminatting whitespace
    public static String formatTime(String input) {
        String output = input.replaceFirst("\\s", "");
        return output;
    }
}
