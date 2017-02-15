package com.mysticwater.myfilms.filmdetail;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.data.Film;
import com.mysticwater.myfilms.data.Genre;
import com.mysticwater.myfilms.network.TheMovieDbService;
import com.mysticwater.myfilms.utils.CalendarUtils;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmsDbHelper;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmsProvider;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmDetailFragment extends Fragment {

    private static final String LOG_TAG = "FilmDetailFragment";

    // UI Elements
    @BindView(R.id.film_detail_layout)
    RelativeLayout filmDetailLayout;
    @BindView(R.id.film_loading_progress)
    ProgressBar filmLoadingProgress;
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
    @BindView(R.id.film_runtime_text)
    TextView filmRuntime;
    @BindView(R.id.film_imdb_button)
    ImageView imdbButton;
    @BindView(R.id.film_add_event)
    ImageView addEventButton;

    // Bundle strings
    public static final String FILM_ID = "FilmId";

    private Film mFilm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle filmBundle = getArguments();
        int id = filmBundle.getInt(FILM_ID);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        mFilm = FilmsDbHelper.getFilm(getActivity(), id);

        showLoadingProgress(true);

        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);

        final Call<Film> getFilm = theMovieDbService.film(String.valueOf(id), getString(R.string.moviedb_api_key));

        getFilm.enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                showLoadingProgress(false);

                Film film = response.body();
                if (film != null) {
                    mFilm = film;
                    loadFilmDetails();
                }
            }

            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                Crashlytics.log(Log.ERROR, LOG_TAG, "Failed to get response for film. " +
                        t.getMessage());

                showLoadingProgress(false);

                loadFilmDetails();
            }
        });

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

    private void loadFilmDetails() {
        if (mFilm != null) {

            // Load the title
            filmTitle.setText(mFilm.getTitle());

            // Load the description
            filmOverview.setText(mFilm.getOverview());

            // Load the release date
            String releaseDate = CalendarUtils.convertDateToLocale(getActivity(), mFilm
                    .getReleaseDate());
            filmReleaseDate.setText(releaseDate);

            final Calendar releaseDateCal = CalendarUtils.dateStringToCalendar(mFilm.getReleaseDate());

            // Update the calendar button
            addEventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, releaseDateCal.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, releaseDateCal.getTimeInMillis())
                            .putExtra(CalendarContract.Events.TITLE, mFilm.getTitle())
                            .putExtra(CalendarContract.Events.ALL_DAY, true)
                            .putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getTimeZone("UTC"));
                    startActivity(intent);

                }
            });

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

            List<Genre> genreList = mFilm.getGenres();
            if (genreList != null) {
                StringBuilder genreBuilder = new StringBuilder();
                for (Genre genre : genreList) {
                    if (genreBuilder.length() > 0) {
                        genreBuilder.append(", ");
                    }
                    genreBuilder.append(genre.getName());
                }
                filmGenres.setText(genreBuilder.toString());
            }

            if (mFilm.getVoteAverage() != null) {
                filmRating.setText(String.valueOf(mFilm.getVoteAverage()));
            }

            final String imdbId = mFilm.getImdbId();
            if (!TextUtils.isEmpty(imdbId)) {
                imdbButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("http://www.imdb.com/title/" + imdbId);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }
            else
            {
                imdbButton.setVisibility(View.GONE);
            }

            if (mFilm.getRuntime() != null)
            {
                int runtime = mFilm.getRuntime();
                int hours = (runtime / 60);
                int minutes = (runtime % 60);

                String runtimeHours  = getResources().getQuantityString(R.plurals.hours, hours,
                    hours);
                String runtimeMins = getResources().getQuantityString(R.plurals.minutes, minutes,
                        minutes);
                filmRuntime.setText(runtimeHours + " " + runtimeMins);
            }

        }
    }

    private void showLoadingProgress(boolean show) {
        if (show) {
            filmLoadingProgress.setVisibility(View.VISIBLE);
            filmDetailLayout.setVisibility(View.GONE);
        } else {
            filmLoadingProgress.setVisibility(View.GONE);
            filmDetailLayout.setVisibility(View.VISIBLE);
        }
    }

}
