package com.mysticwater.myfilms.utils.filmcontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.JsonUtils;

public class FavouriteFilmsDbHelper {

    public static Cursor getAllFilms(Context context) {
        return context.getContentResolver().query(FilmsProvider.FavouriteFilms.CONTENT_URI, null,
                null, null, null);
    }

    public static void insertFilm(Context context, Film film) {
        ContentValues cv = new ContentValues();
        String filmJson = JsonUtils.objectToJson(film);
        cv.put(FilmColumns.ID, film.getId());
        cv.put(FilmColumns.FILM, filmJson);
        context.getContentResolver().insert(FilmsProvider.FavouriteFilms.CONTENT_URI, cv);
    }

    public static void removeFilm(Context context, Film film) {
        String selectionClause = FilmColumns.ID + " = ?";
        String[] selectionArgs = {String.valueOf(film.getId())};
        context.getContentResolver().delete(
                FilmsProvider.FavouriteFilms.CONTENT_URI,
                selectionClause,
                selectionArgs
        );
    }

}
