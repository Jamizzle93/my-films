package com.mysticwater.myfilms.utils;

import com.mysticwater.myfilms.data.Film;

import java.util.Calendar;
import java.util.Comparator;

public class FilmComparator implements Comparator<Film> {
    @Override
    public int compare(Film film1, Film film2) {

        Calendar film1Cal = CalendarUtils.dateStringToCalendar(film1.getReleaseDate());
        Calendar film2Cal = CalendarUtils.dateStringToCalendar(film2.getReleaseDate());

        int result1 = film1Cal.compareTo(film2Cal);
        if (result1 == 0)
        {
            return film1.getTitle().compareTo(film2.getTitle());
        }

        return result1;
    }

}
