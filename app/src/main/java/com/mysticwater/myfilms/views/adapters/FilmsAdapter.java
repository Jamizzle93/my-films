package com.mysticwater.myfilms.views.adapters;

import android.widget.BaseAdapter;

import com.mysticwater.myfilms.model.Film;

import java.util.List;

public class FilmsAdapter extends BaseAdapter {

    private List<Film> mFilms;
    private FilmListener mFilmListener;

    public FilmsAdapter(List<Film> films, FilmListener filmListener)
    {
        setList(films);
        mFilmListener = filmListener;
    }

    public void replaceData(List<Film> films) {
        setList(films);
        notifyDataSetChanged();
    }

    private void setList(List<Film> films) {
        mFilms = checkNotNull(films);
    }

    @Override
    public int getCount() {
        return mFilms.size();
    }

    public interface FilmListener {

        void onFilmClick(Film clickedFilm);

    }

}
