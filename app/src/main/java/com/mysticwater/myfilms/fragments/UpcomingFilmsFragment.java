package com.mysticwater.myfilms.fragments;

import com.google.gson.Gson;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.model.FilmResults;
import com.mysticwater.myfilms.network.TheMovieDbService;
import com.mysticwater.myfilms.utils.CalendarUtils;
import com.mysticwater.myfilms.utils.FilmComparator;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmColumns;
import com.mysticwater.myfilms.utils.filmcontentprovider.UpcomingFilmsDbHelper;
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

    private View mLayoutView;

    // List View
    private RecyclerView mFilmsRecyclerView;
    private FilmAdapter mFilmsAdapter;
    private List<Film> mFilms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.fragment_upcoming_films, container, false);

        mFilms = new ArrayList<>();

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.MONTH, 1);

        String startCalString = CalendarUtils.calendarToString(startCal);
        String endCalString = CalendarUtils.calendarToString(endCal);

        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);

        final Call<FilmResults> upcomingFilms = theMovieDbService.upcomingReleases(getString(R.string
                .moviedb_api_key), startCalString, endCalString);

        upcomingFilms.enqueue(new Callback<FilmResults>() {
            @Override
            public void onResponse(Call<FilmResults> call, Response<FilmResults> response) {
                FilmResults films = response.body();
                if (films != null) {
                    deleteAllFilms();
                    for (Film film : films.getFilms()) {
                        UpcomingFilmsDbHelper.insertFilm(getActivity(), film);
                    }
                    fillList();
                }
            }

            @Override
            public void onFailure(Call<FilmResults> call, Throwable t) {
                fillList();
            }
        });

        // Setup ListView
        mFilmsRecyclerView = (RecyclerView) mLayoutView.findViewById(R.id.list_upcoming_films);
        List<Film> films = new ArrayList<>();
        mFilmsAdapter = new FilmAdapter(getActivity(), films);
        mFilmsRecyclerView.setAdapter(mFilmsAdapter);
        mFilmsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return mLayoutView;
    }

    private void fillList() {
        mFilms.clear();

        Cursor allFilms = UpcomingFilmsDbHelper.getAllFilms(getActivity());
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

    private void deleteAllFilms()
    {
        getActivity().getContentResolver().delete(FilmsProvider.UpcomingFilms.CONTENT_URI, "1", null);
    }


}
