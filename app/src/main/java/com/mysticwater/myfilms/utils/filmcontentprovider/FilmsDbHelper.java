package com.mysticwater.myfilms.utils.filmcontentprovider;

import com.google.gson.Gson;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mysticwater.myfilms.data.Film;
import com.mysticwater.myfilms.utils.JsonUtils;

public class FilmsDbHelper {

    public static Film getFilm(Context context, int id) {
        Uri uri = FilmsProvider.FavouriteFilms.CONTENT_URI;

        Film film = getFilm(context, uri, id);
        if (film == null) {
            uri = FilmsProvider.UpcomingFilms.CONTENT_URI;
            film = getFilm(context, uri, id);
        }
        if (film == null){
            uri = FilmsProvider.NowShowingFilms.CONTENT_URI;
            film = getFilm(context, uri, id);
        }

        return film;
    }

    public static Film getFilm(Context context, Uri uri, int id) {
        Film film = null;

        String selection = FilmColumns.ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        Cursor filmCursor = context.getContentResolver().query(
                uri,
                null,
                selection,
                selectionArgs,
                null
        );

        if (filmCursor != null) {
            if (filmCursor.moveToFirst()) {
                String filmJson = filmCursor.getString(filmCursor.getColumnIndex(FilmColumns.FILM));
                film = new Gson().fromJson(filmJson, Film.class);
                filmCursor.close();
            }
        }

        return film;
    }


    public static boolean isFilmFavourite(Context context, Film film) {
        int id = film.getId();
        Uri uri = FilmsProvider.FavouriteFilms.CONTENT_URI;
        Film checkFilm = getFilm(context, uri, id);
        return checkFilm != null;
    }

    public static Cursor getAllFilms(Context context, Uri uri)
    {
        return context.getContentResolver().query(uri, null,
                null, null, null);
    }

    public static void insertFilm(Context context, Uri uri, Film film) {
        ContentValues cv = new ContentValues();
        String filmJson = JsonUtils.objectToJson(film);
        cv.put(FilmColumns.ID, film.getId());
        cv.put(FilmColumns.FILM, filmJson);
        context.getContentResolver().insert(uri, cv);
    }

    public static void removeFilm(Context context, Uri uri, Film film) {
        String selectionClause = FilmColumns.ID + " = ?";
        String[] selectionArgs = {String.valueOf(film.getId())};
        context.getContentResolver().delete(
                uri,
                selectionClause,
                selectionArgs
        );
    }

}
