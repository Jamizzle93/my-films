package com.mysticwater.myfilms.utils.filmcontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.JsonUtils;

public class UpcomingFilmsDbHelper {

    public static Cursor getAllFilms(Context context) {
        return context.getContentResolver().query(FilmsProvider.UpcomingFilms.CONTENT_URI, null,
                null, null, null);
    }

    public static void insertFilm(Context context, Film film) {
        if (!TextUtils.isEmpty(film.getPosterPath()) && !TextUtils.isEmpty(film.getBackdropPath())) {
            ContentValues cv = new ContentValues();
            String filmJson = JsonUtils.objectToJson(film);
            cv.put(FilmColumns.ID, film.getId());
            cv.put(FilmColumns.FILM, filmJson);
            context.getContentResolver().insert(FilmsProvider.UpcomingFilms.CONTENT_URI, cv);
        }
    }

}
