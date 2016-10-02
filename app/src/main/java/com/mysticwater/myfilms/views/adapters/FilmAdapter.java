package com.mysticwater.myfilms.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.CalendarUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class FilmAdapter extends ArrayAdapter<Film> {

    private Context mContext;

    public FilmAdapter(Context context, List<Film> films) {
        super(context, R.layout.row_upcoming_film, films);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilmHolder filmHolder;
        if (convertView == null) {
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
        if (film != null) {
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
                // TODO - Update after new release of Picasso
                String imageUri = mContext.getString(R.string.moviedb_image_w500_url, posterPath);

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .build();

                Picasso.Builder builder = new Picasso.Builder(mContext);

                int pixelWidth = dpToPixels(100);
                int pixelHeight = dpToPixels(150);

                builder.downloader(new OkHttp3Downloader(okHttpClient))
                        .build()
                        .load(imageUri)
                        .resize(pixelWidth, pixelHeight)
                        .centerCrop()
                        .tag(mContext)
                        .into(filmHolder.poster);
            }
            else
            {
                filmHolder.poster.setImageResource(0);
            }
        }

        return convertView;
    }

    private static class FilmHolder {
        ImageView poster;
        TextView title;
        TextView releaseDate;
    }

    private int dpToPixels(int dp) {
        Resources r = mContext.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

}
