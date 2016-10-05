package com.mysticwater.myfilms.fragments;

import com.google.gson.Gson;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmColumns;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

import static com.mysticwater.myfilms.utils.filmcontentprovider.FilmsProvider.Films.CONTENT_URI;

public class FilmDetailFragment extends Fragment {

    private static final String LOG_TAG = "FilmDetailFragment";

    // UI Elements
    @BindView(R.id.film_backdrop) ImageView filmBackdrop;
    @BindView(R.id.film_title) TextView filmTitle;
    @BindView(R.id.film_overview_text) ExpandableTextView filmOverview;

    // Bundle strings
    public static final String FILM_ID = "FilmId";

    private Film mFilm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle filmBundle = getArguments();
        int id = filmBundle.getInt(FILM_ID);

        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor allFilms = getActivity().getContentResolver().query(
                CONTENT_URI,
                null,
                FilmColumns.ID + " = ?",
                selectionArgs,
                null
        );

        if (allFilms != null) {
            allFilms.moveToFirst();
            String filmJson = allFilms.getString(allFilms.getColumnIndex(FilmColumns.FILM));
            mFilm = new Gson().fromJson(filmJson, Film.class);
        }

        if (mFilm != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(mFilm.getTitle());
            }

            // Load in the backdrop
            String backdropPath = mFilm.getBackdropPath();
            if (!TextUtils.isEmpty(backdropPath)) {
                // TODO - Update after new release of Picasso
                String imageUri = getString(R.string.moviedb_backdrop_w1280_url, backdropPath);

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .build();

                Picasso.Builder builder = new Picasso.Builder(getActivity());

                builder.downloader(new OkHttp3Downloader(okHttpClient))
                        .build()
                        .load(imageUri)
                        .tag(getActivity())
                        .into(filmBackdrop);
            }
            else
            {
                filmBackdrop.setImageResource(0);
            }

            // Load the title
            filmTitle.setText(mFilm.getTitle());

            // Load the description
            filmOverview.setText(mFilm.getOverview());
        }

        return view;
    }

}
