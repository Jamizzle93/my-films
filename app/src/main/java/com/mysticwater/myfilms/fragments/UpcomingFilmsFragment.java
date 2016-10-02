package com.mysticwater.myfilms.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.model.FilmResults;
import com.mysticwater.myfilms.network.TheMovieDbService;
import com.mysticwater.myfilms.utils.CalendarUtils;
import com.mysticwater.myfilms.utils.FilmComparator;
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
    private ListView mFilmsList;
    private FilmAdapter mFilmsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutView = inflater.inflate(R.layout.fragment_upcoming_films, container, false);

        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);
        final Call<Film> filmCall = theMovieDbService.film("76341", getString(R.string.moviedb_api_key));

        filmCall.enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                Film film = response.body();
                System.out.println(film.getTitle());
            }

            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.MONTH, 1);

        String startCalString = CalendarUtils.calendarToString(startCal);
        String endCalString = CalendarUtils.calendarToString(endCal);

        final Call<FilmResults> upcomingFilms = theMovieDbService.upcomingReleases(getString(R.string
                .moviedb_api_key), startCalString, endCalString);

        upcomingFilms.enqueue(new Callback<FilmResults>() {
            @Override
            public void onResponse(Call<FilmResults> call, Response<FilmResults> response) {
                FilmResults films = response.body();
                if (films != null) {
                    for (Film film : films.getFilms()) {
                        System.out.println(film.getTitle());
                    }
                    fillList(films.getFilms());
                }
            }

            @Override
            public void onFailure(Call<FilmResults> call, Throwable t) {
                t.printStackTrace();
            }
        });

        // Setup ListView
        mFilmsList = (ListView) mLayoutView.findViewById(R.id.list_upcoming_films);
        ArrayList<Film> filmsList = new ArrayList<>();
        mFilmsAdapter = new FilmAdapter(getActivity(), filmsList);
        mFilmsList.setAdapter(mFilmsAdapter);

        return mLayoutView;
    }

    private void fillList(List<Film> films)
    {
        mFilmsAdapter.clear();
        Collections.sort(films, new FilmComparator());
        mFilmsAdapter.addAll(films);
        mFilmsAdapter.notifyDataSetChanged();
    }

}
