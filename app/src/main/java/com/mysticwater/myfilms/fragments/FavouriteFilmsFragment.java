package com.mysticwater.myfilms.fragments;

import com.google.gson.Gson;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.FilmComparator;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmColumns;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmsDbHelper;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmsProvider;
import com.mysticwater.myfilms.views.adapters.FilmAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouriteFilmsFragment extends Fragment {

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

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) mLayoutView.findViewById(R.id
                .swipe_refresh_films);
        swipeRefreshLayout.setVisibility(View.GONE);

        return mLayoutView;
    }

    public void onResume() {
        super.onResume();

        fillList();
    }

    private void fillList() {
        mFilms.clear();

        Uri favouriteFilmsUri = FilmsProvider.FavouriteFilms.CONTENT_URI;
        Cursor allFilms = FilmsDbHelper.getAllFilms(getActivity(), favouriteFilmsUri);
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

}
