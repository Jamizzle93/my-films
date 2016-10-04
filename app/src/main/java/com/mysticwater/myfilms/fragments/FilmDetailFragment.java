package com.mysticwater.myfilms.fragments;

import com.google.gson.Gson;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysticwater.myfilms.FilmDetailActivity;
import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmColumns;

import static com.mysticwater.myfilms.utils.filmcontentprovider.FilmsProvider.Films.CONTENT_URI;

public class FilmDetailFragment extends Fragment {

    private static final String LOG_TAG = "FilmDetailFragment";

    private View mLayoutView;

    // Bundle strings
    public static final String FILM_ID = "FilmId";

    private Film mFilm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutView = inflater.inflate(R.layout.fragment_upcoming_films, container, false);

        Bundle filmBundle = getArguments();
        int id = filmBundle.getInt(FILM_ID);

        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor allFilms = getActivity().getContentResolver().query(
                CONTENT_URI,
                null,
                FilmColumns.ID + " = ?",
                selectionArgs,
                null
        );

        if (allFilms != null) {
            allFilms.moveToFirst();
            String filmJson = allFilms.getString(allFilms.getColumnIndex(FilmColumns.FILM));
            mFilm = new Gson().fromJson(filmJson, Film.class);
        }

        if (mFilm != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(mFilm.getTitle());
            }
        }

        return mLayoutView;
    }

}
