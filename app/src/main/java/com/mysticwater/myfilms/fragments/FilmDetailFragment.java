package com.mysticwater.myfilms.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.utils.CalendarUtils;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmsDbHelper;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmsProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilmDetailFragment extends Fragment {

    private static final String LOG_TAG = "FilmDetailFragment";

    // UI Elements
    @BindView(R.id.film_title)
    TextView filmTitle;
    @BindView(R.id.film_overview_text)
    ExpandableTextView filmOverview;
    @BindView(R.id.film_release_date_text)
    TextView filmReleaseDate;
    @BindView(R.id.film_favourite_button)
    MaterialFavoriteButton favouriteFilm;
    @BindView(R.id.film_genres_text)
    TextView filmGenres;
    @BindView(R.id.film_rating_text)
    TextView filmRating;
    @BindView(R.id.film_imdb_text)
    TextView filmImdbId;

    // Bundle strings
    public static final String FILM_ID = "FilmId";

    private Film mFilm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle filmBundle = getArguments();
        int id = filmBundle.getInt(FILM_ID);

        mFilm = FilmsDbHelper.getFilm(getActivity(), id);

        if (mFilm != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("");
            }

            // Load the title
            filmTitle.setText(mFilm.getTitle());

            // Load the description
            filmOverview.setText(mFilm.getOverview());

            // Load the release date
            String releaseDate = CalendarUtils.convertDateToLocale(getActivity(), mFilm
                    .getReleaseDate());
            filmReleaseDate.setText(releaseDate);

            // Handle favouriting
            favouriteFilm.setFavorite(FilmsDbHelper.isFilmFavourite(getActivity(), mFilm));
            favouriteFilm.setOnFavoriteChangeListener(
                    new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            Uri favouriteFilmUri = FilmsProvider.FavouriteFilms.CONTENT_URI;

                            if (favorite) {
                                FilmsDbHelper.insertFilm(getActivity(), favouriteFilmUri, mFilm);
                            } else {
                                FilmsDbHelper.removeFilm(getActivity(), favouriteFilmUri, mFilm);
                            }
                        }
                    });
        }

//        List<Genre> genreList = mFilm.getGenreIds();
//        if (genreList != null) {
//            StringBuilder genreBuilder = new StringBuilder();
//            for (int i : genreList) {
//                if (genreBuilder.length() > 0) {
//                    genreBuilder.append(", ");
//                }
//                Genre genre =
//                genreBuilder.append(genre.toString());
//            }
//            filmGenres.setText(genreBuilder.toString());
//        }

        if (mFilm.getVoteAverage() != null) {
            filmRating.setText(String.valueOf(mFilm.getVoteAverage()));
        }

        filmImdbId.setText("");

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
