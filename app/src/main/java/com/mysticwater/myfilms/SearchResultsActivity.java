package com.mysticwater.myfilms;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.model.FilmResults;
import com.mysticwater.myfilms.network.TheMovieDbService;
import com.mysticwater.myfilms.utils.filmcontentprovider.FilmsDbHelper;
import com.mysticwater.myfilms.views.adapters.FilmAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SearchResultsActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_refresh_search)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.search_films_list)
    RecyclerView mFilmsRecyclerView;

    private List<Film> mFilms;
    private FilmAdapter mFilmsAdapter;

    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        handleIntent(getIntent());

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle(getString(R.string.title_search_results));
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
        }

        // Setup recycler view
        mFilms = new ArrayList<>();
        mFilmsAdapter = new FilmAdapter(this, mFilms);
        mFilmsRecyclerView.setAdapter(mFilmsAdapter);
        mFilmsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Crashlytics.log(Log.INFO, LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                searchFilms(mQuery);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            searchFilms(mQuery);
        }
    }

    private void searchFilms(String query) {
        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);

        final Call<FilmResults> searchFilms = theMovieDbService.searchFilms(getString(R.string
                .moviedb_api_key), query);

        searchFilms.enqueue(new Callback<FilmResults>() {
            @Override
            public void onResponse(Call<FilmResults> call, Response<FilmResults> response) {
                mSwipeRefresh.setRefreshing(false);

                FilmResults films = response.body();
                if (films != null) {
                    for (Film film : films.getFilms()) {
                        // Only care about films with a poster and backdrop
                        if (!TextUtils.isEmpty(film.getPosterPath()) && !TextUtils.isEmpty(film.getBackdropPath())) {
                            mFilms.add(film);
                        }
                    }
                    fillList();
                }
            }

            @Override
            public void onFailure(Call<FilmResults> call, Throwable t) {
                Crashlytics.log(Log.ERROR, LOG_TAG, "Failed to get response for upcoming releases. " + t.getMessage());

                mSwipeRefresh.setRefreshing(false);

                fillList();
            }
        });
    }

    private void fillList() {
        List<Film> data = new ArrayList<>(mFilms);
        mFilmsAdapter.updateData(data);
    }
}
