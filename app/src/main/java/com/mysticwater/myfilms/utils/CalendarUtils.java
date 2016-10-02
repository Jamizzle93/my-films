package com.mysticwater.myfilms.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarUtils {

    public static final String API_DATE_FORMAT = "yyyy-MM-dd";

    private static SimpleDateFormat getApiSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }

    public static String calendarToString(Calendar calendar, SimpleDateFormat simpleDateFormat) {
        try {
            return simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            Log.e("CalendarUtils", "Uh oh, we couldn't format the calendar: " + calendar);
            return "";
        }

    }

    public static String calendarToString(Calendar calendar) {
        return calendarToString(calendar, getApiSimpleDateFormat());
    }

    public static String calendarDateForLocale(Calendar calendar, Locale locale) {
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        return dateFormatter.format(calendar.getTime());
    }

    public static String convertCalendarToLocaleDateString(Context context, Calendar calendar) {
        Locale currentLocale;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            currentLocale = context.getResources().getConfiguration().locale;
        } else {
            currentLocale = context.getResources().getConfiguration().getLocales().get(0);
        }

        return calendarDateForLocale(calendar, currentLocale);
    }

    public static Calendar dateStringToCalendar(String date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getApiSimpleDateFormat().parse(date));
            return calendar;
        } catch (Exception e) {
            Log.e("CalendarUtils", "Uh oh, we couldn't parse the date into a calendar: " +
                    date);
            return null;
        }
    }

    public static String convertDateToLocale(Context context, String date)
    {
        Calendar calendar = dateStringToCalendar(date);
        return convertCalendarToLocaleDateString(context, calendar);
    }

}
