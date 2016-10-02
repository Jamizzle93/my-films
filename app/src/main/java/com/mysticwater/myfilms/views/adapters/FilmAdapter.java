package com.mysticwater.myfilms.views.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.CalendarUtils;

import java.util.List;

public class FilmAdapter extends ArrayAdapter<Film> {

    private Context mContext;

    public FilmAdapter(Context context, List<Film> films)
    {
        super(context, R.layout.row_upcoming_film, films);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        FilmHolder filmHolder;
        if (convertView == null)
        {
            filmHolder = new FilmHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_upcoming_film, parent, false);

            filmHolder.poster = (ImageView) convertView.findViewById(R.id
                    .row_upcoming_poster_image);
            filmHolder.title = (TextView) convertView.findViewById(R.id.row_upcoming_title_text);
            filmHolder.releaseDate = (TextView) convertView.findViewById(R.id
                    .row_upcoming_release_text);

            convertView.setTag(filmHolder);
        } else {
            filmHolder = (FilmHolder) convertView.getTag();
        }

        Film film = getItem(position);
        if (film != null)
        {
            String title = film.getTitle();
            if (!TextUtils.isEmpty(title)) {
                filmHolder.title.setText(title);
            }

            String releaseDate = film.getReleaseDate();
            if (!TextUtils.isEmpty(releaseDate)) {
                String localDate = CalendarUtils.convertDateToLocale(mContext,
                        releaseDate);
                filmHolder.releaseDate.setText(localDate);
            }

            String posterPath = film.getPosterPath();
            if (!TextUtils.isEmpty(posterPath)) {
                //TODO - Get the poster...
            }
        }

        return convertView;
    }

    private static class FilmHolder {
        ImageView poster;
        TextView title;
        TextView releaseDate;
    }

}
