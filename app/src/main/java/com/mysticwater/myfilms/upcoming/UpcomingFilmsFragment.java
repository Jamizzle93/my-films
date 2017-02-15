package com.mysticwater.myfilms.upcoming;

import com.google.gson.Gson;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.data.Film;
import com.mysticwater.myfilms.data.FilmResults;
import com.mysticwater.myfilms.network.TheMovieDbService;
import com.mysticwater.myfilms.utils.CalendarUtils;
import com.mysticwater.myfilms.utils.FilmComparator;
import com.mysticwater.myfilms.utils.Helpers;
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

public class UpcomingFilmsFragment extends Fragment {

    private static final String LOG_TAG = "UpcomingFilmsFragment";

    private View mLayoutView;

    // List View
    private RecyclerView mFilmsRecyclerView;
    private FilmAdapter mFilmsAdapter;
    private List<Film> mFilms;

    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) mLayoutView.findViewById(R
                .id.swipe_refresh_films);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Crashlytics.log(Log.INFO, LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        refreshList();
                    }
                }
        );


        return mLayoutView;
    }

    public void onResume() {
        super.onResume();

        refreshList();
    }

    private void refreshList() {
        String regionCode = Helpers.getRegionCode(getActivity());

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.MONTH, 1);

        String startCalString = CalendarUtils.calendarToString(startCal);
        String endCalString = CalendarUtils.calendarToString(endCal);

        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);

        final Call<FilmResults> upcomingFilms = theMovieDbService.upcomingReleases(getString(R.string
                .moviedb_api_key), regionCode, startCalString, endCalString);

        upcomingFilms.enqueue(new Callback<FilmResults>() {
            @Override
            public void onResponse(Call<FilmResults> call, Response<FilmResults> response) {
                mSwipeRefreshLayout.setRefreshing(false);

                FilmResults films = response.body();
                if (films != null) {
                    Uri upcomingFilmsUri = FilmsProvider.UpcomingFilms.CONTENT_URI;
                    deleteAllFilms();
                    for (Film film : films.getFilms()) {
                        // Only care about films with a poster and backdrop
                        if (!TextUtils.isEmpty(film.getPosterPath()) && !TextUtils.isEmpty(film.getBackdropPath())) {
                            FilmsDbHelper.insertFilm(getActivity(), upcomingFilmsUri, film);
                        }
                    }
                    fillList();
                }
            }

            @Override
            public void onFailure(Call<FilmResults> call, Throwable t) {
                Crashlytics.log(Log.ERROR, LOG_TAG, "Failed to get response for upcoming releases. " + t.getMessage());

                mSwipeRefreshLayout.setRefreshing(false);

                fillList();
            }
        });
    }

    private void fillList() {
        mFilms.clear();

        Uri upcomingFilmsUri = FilmsProvider.UpcomingFilms.CONTENT_URI;
        Cursor allFilms = FilmsDbHelper.getAllFilms(getActivity(), upcomingFilmsUri);
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
        getActivity().getContentResolver().delete(FilmsProvider.UpcomingFilms.CONTENT_URI, "1", null);
    }


}
