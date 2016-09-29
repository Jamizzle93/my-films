package com.mysticwater.myfilms.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.network.TheMovieDbService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingFilmsFragment extends Fragment {

    private View mLayoutView;

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

        return mLayoutView;
    }

}
