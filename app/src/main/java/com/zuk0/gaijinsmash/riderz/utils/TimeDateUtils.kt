package com.zuk0.gaijinsmash.riderz.utils

import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.BsaXmlResponse
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeDateUtils {

    val isAfterHours: Boolean
        get() {
            val hour = currentHour
            return hour in 1..4
        }

    val isDaytime: Boolean
        get() {
            val millis = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            return calendar[Calendar.HOUR_OF_DAY] in 5..17
        }

    val isMorning: Boolean
        get() {
            val millis = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            return calendar[Calendar.HOUR_OF_DAY] in 5..11
        }

    val isDusk: Boolean
        get() {
            val millis = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            return calendar[Calendar.HOUR_OF_DAY] in 17..19
        }

    val isNightTime: Boolean
        get() {
            val millis = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            return calendar[Calendar.HOUR_OF_DAY] in 18..24
        }

    val todaysDate: String
        get() {
            val c = Calendar.getInstance()
            val day = c[Calendar.DAY_OF_MONTH]
            val month = c[Calendar.MONTH]
            val year = c[Calendar.YEAR]
            c[year, month] = day
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            return sdf.format(c.time)
        }

    // returns in 24 hour format
    val currentTime: String
        get() {
            val millis = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            return calendar[Calendar.HOUR_OF_DAY].toString() + ":" + calendar[Calendar.MINUTE]
        }

    fun formatTime(input: String): String {
        return input.replaceFirst("\\s".toRegex(), "")
    }

    // Format date for api string - mm/dd/yyyy
    fun formatDate(input: String?): String {
        val output: String
        var date: Date? = null
        var sdf = SimpleDateFormat("MM-dd-yyyy", Locale.US)
        try {
            date = sdf.parse(input)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        output = sdf.format(date)
        return output
    }

    // Remove am/pm from 24hour time
    fun format24hrTime(input: String?): String {
        var date: Date? = null
        var sdf = SimpleDateFormat("HH:mm:ss a z", Locale.US)
        try {
            date = sdf.parse(input)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        sdf = SimpleDateFormat("HH:mm z", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("PST8PDT")
        //todo: PST8PDT timezone id isn't working properly
        return sdf.format(date)
    }

    fun convertTo12Hr(input: String?): String {
        var date: Date? = null
        var sdf = SimpleDateFormat("HH:mm:ss a z", Locale.US)
        try {
            date = sdf.parse(input)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        sdf = SimpleDateFormat("hh:mm a z", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("PST8PDT")
        //todo: PST8PDT timezone id isn't working properly
        return sdf.format(date)
    }

    // For Trip api calls
    fun convertTo12HrForTrip(input: String?): String {
        var date: Date? = null
        var sdf = SimpleDateFormat("HH:mm", Locale.US)
        try {
            date = sdf.parse(input)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        sdf = SimpleDateFormat("hh:mm a", Locale.US)
        return sdf.format(date)
    }

    val currentHour: Int
        get() {
            val df: DateFormat = SimpleDateFormat("HH", Locale.US)
            val date = df.calendar.time
            df.timeZone = TimeZone.getTimeZone("America/Los_Angeles")
            val time = df.format(date)
            return Integer.valueOf(time)
        }

    // This is for home screen
    fun getFormattedTime(bsa: BsaXmlResponse, is24HrTimeOn: Boolean): String {
        return if (is24HrTimeOn) {
            format24hrTime(bsa.time)
        } else {
            convertTo12Hr(bsa.time)
        }
    }
}