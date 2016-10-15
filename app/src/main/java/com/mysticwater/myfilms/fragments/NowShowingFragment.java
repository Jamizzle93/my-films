package com.mysticwater.myfilms.fragments;

import com.google.gson.Gson;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.model.FilmResults;
import com.mysticwater.myfilms.network.TheMovieDbService;
import com.mysticwater.myfilms.utils.CalendarUtils;
import com.mysticwater.myfilms.utils.FilmComparator;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmColumns;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmsDbHelper;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmsProvider;
import com.mysticwater.myfilms.views.adapters.FilmAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NowShowingFragment extends Fragment {

    private static final String LOG_TAG = "UpcomingFilmsFragment";

    private View mLayoutView;

    // List View
    private RecyclerView mFilmsRecyclerView;
    private FilmAdapter mFilmsAdapter;
    private List<Film> mFilms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.fragment_film_list, container, false);

        mFilms = new ArrayList<>();

        // Setup ListView
        mFilmsRecyclerView = (RecyclerView) mLayoutView.findViewById(R.id.list_films);
        List<Film> films = new ArrayList<>();
        mFilmsAdapter = new FilmAdapter(getActivity(), films);
        mFilmsRecyclerView.setAdapter(mFilmsAdapter);
        mFilmsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return mLayoutView;
    }

    public void onResume() {
        super.onResume();

        Calendar calendar = Calendar.getInstance();

        String calendarString = CalendarUtils.calendarToString(calendar);

        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);

        final Call<FilmResults> nowPlayingFilms = theMovieDbService.nowPlaying(getString(R.string
                .moviedb_api_key), calendarString);

        nowPlayingFilms.enqueue(new Callback<FilmResults>() {
            @Override
            public void onResponse(Call<FilmResults> call, Response<FilmResults> response) {
                FilmResults films = response.body();
                if (films != null) {
                    Uri nowShowingUri = FilmsProvider.NowShowingFilms.CONTENT_URI;
                    deleteAllFilms();
                    for (Film film : films.getFilms()) {
                        // Only care about films with a poster and backdrop
                        if (!TextUtils.isEmpty(film.getPosterPath()) && !TextUtils.isEmpty(film.getBackdropPath())) {
                            FilmsDbHelper.insertFilm(getActivity(), nowShowingUri, film);
                        }
                    }
                    fillList();
                }
            }

            @Override
            public void onFailure(Call<FilmResults> call, Throwable t) {
                Crashlytics.log(Log.ERROR, LOG_TAG, "Failed to get response for upcoming releases. " + t.getMessage());
                fillList();
            }
        });
    }

    private void fillList() {
        mFilms.clear();

        Uri nowShowingUri = FilmsProvider.NowShowingFilms.CONTENT_URI;
        Cursor allFilms = FilmsDbHelper.getAllFilms(getActivity(), nowShowingUri);
        if (allFilms != null) {
            while (allFilms.moveToNext()) {
                String filmJson = allFilms.getString(allFilms.getColumnIndex(FilmColumns.FILM));
                Film film = new Gson().fromJson(filmJson, Film.class);
                mFilms.add(film);
            }
            allFilms.close();
        }

        Collections.sort(mFilms, new FilmComparator());
        mFilmsAdapter.updateData(mFilms);
    }

    private void deleteAllFilms() {
        getActivity().getContentResolver().delete(FilmsProvider.NowShowingFilms.CONTENT_URI, "1", null);
    }

}
