package com.mysticwater.myfilms.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarUtils {

    public static String calendarToString(Calendar calendar, SimpleDateFormat simpleDateFormat)
    {
        try
        {
            return simpleDateFormat.format(calendar.getTime());
        }
        catch (Exception e)
        {
            Log.e("CalendarUtils", "Uh oh, we couldn't format the calendar: " + calendar);
            return "";
        }

    }

    public static String calendarToString(Calendar calendar)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendarToString(calendar, simpleDateFormat);
    }

}
