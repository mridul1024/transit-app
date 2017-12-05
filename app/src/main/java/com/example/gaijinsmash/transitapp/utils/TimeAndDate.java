package com.example.gaijinsmash.transitapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeAndDate {

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
        SimpleDateFormat sdf = new SimpleDateFormat("mm-dd-yyyy");
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
