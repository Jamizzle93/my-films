package com.mysticwater.myfilms.utils.filmcontentprovider;

import com.google.gson.Gson;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.JsonUtils;

import static com.mysticwater.myfilms.utils.filmcontentprovider.FilmsProvider.UpcomingFilms.CONTENT_URI;

/**
 * Created by james-home on 13/10/2016.
 */

public class FilmsDbHelper {

    public static Film getFilm(Context context, int id) {
        Film film = FavouriteFilmsDbHelper.getFilm(context, id);
        if (film == null) {
            film = UpcomingFilmsDbHelper.getFilm(context, id);
        }

        return film;
    }

    public static class UpcomingFilmsDbHelper {

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

        public static Film getFilm(Context context, int id) {
            Film film = null;

            String selection = FilmColumns.ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(id)};

            Cursor filmCursor = context.getContentResolver().query(
                    CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    null
            );

            if (filmCursor != null) {
                filmCursor.moveToFirst();
                String filmJson = filmCursor.getString(filmCursor.getColumnIndex(FilmColumns.FILM));
                film = new Gson().fromJson(filmJson, Film.class);
                filmCursor.close();
            }

            return film;
        }

    }

    public static class FavouriteFilmsDbHelper {

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

        public static Film getFilm(Context context, int id) {
            Film film = null;

            String selection = FilmColumns.ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(id)};

            Cursor filmCursor = context.getContentResolver().query(
                    CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    null
            );

            if (filmCursor != null) {
                filmCursor.moveToFirst();
                String filmJson = filmCursor.getString(filmCursor.getColumnIndex(FilmColumns.FILM));
                film = new Gson().fromJson(filmJson, Film.class);
                filmCursor.close();
            }

            return film;
        }

    }

}
