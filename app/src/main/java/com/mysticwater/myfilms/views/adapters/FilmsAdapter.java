package com.mysticwater.myfilms.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.data.Film;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

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

    @Override
    public Film getItem(int i) {
        return mFilms.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.row_film, viewGroup, false);
        }

        final Film film = getItem(i);

        //TODO - Extract data from film and fill in UI

        rowView.setOnClickListener(__ -> mFilmListener.onFilmClick(film));

        return rowView;
    }

    public interface FilmListener {

        void onFilmClick(Film clickedFilm);

    }

}
