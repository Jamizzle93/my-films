package com.mysticwater.myfilms.nowshowing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.filmdetail.FilmDetailActivity;
import com.mysticwater.myfilms.data.Film;
import com.mysticwater.myfilms.views.adapters.FilmAdapter;
import com.mysticwater.myfilms.views.adapters.FilmsAdapter;

import java.util.ArrayList;
import java.util.List;

public class NowShowingFragment extends Fragment implements NowShowingContract.View {

    private static final String LOG_TAG = "NowShowingFragment";

    private FilmsContract.Presenter mPresenter;

    private FilmsAdapter mListAdapter;

    private View mNoFilmsView;

    private View mLayoutView;

    private RecyclerView mFilmsRecyclerView;
    private FilmAdapter mFilmsAdapter;
    private List<Film> mFilms;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public NowShowingFragment() {
        // Requires empty public constructor
    }

    public static NowShowingFragment newInstance() {
        return new NowShowingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new FilmsAdapter(new ArrayList<>(0), mFilmListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull FilmsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_film_list, container, false);

        // Setup the list view
        RecyclerView filmsRecyclerView = (ListView) root.findViewById(R.id.list_films);
        filmsRecyclerView.setAdapter(mListAdapter);

        // TODO - Set up others...

//        mFilms = new ArrayList<>();
//
//        // Setup ListView
//        mFilmsRecyclerView = (RecyclerView) mLayoutView.findViewById(R.id.list_films);
//        List<Film> films = new ArrayList<>();
//        mFilmsAdapter = new FilmAdapter(getActivity(), films);
//        mFilmsRecyclerView.setAdapter(mFilmsAdapter);
//        mFilmsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        mSwipeRefreshLayout = (SwipeRefreshLayout) mLayoutView.findViewById(R
//                .id.swipe_refresh_films);
//        mSwipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        Crashlytics.log(Log.INFO, LOG_TAG, "onRefresh called from SwipeRefreshLayout");
//
//                        refreshList();
//                    }
//                }
//        );

        return root;
    }

    /**
     * Listener for clicks on films in RecyclerView
     */
    FilmsAdapter.FilmListener mFilmListener = new FilmsAdapter.FilmListener() {
        @Override
        public void onFilmClick(Film clickedFilm) {
            mPresenter.openFilmDetails(clickedFilm);
        }

    };

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null)
        {
            return;
        }
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id
                .swipe_refresh_films);

        // Make sure setRefreshing() is called after the layout is done with
        srl.post(() -> srl.setRefreshing(active));
    }

    @Override
    public void showFilms(List<Film> films) {
        mFilmsAdapter.replaceData(films);

        //TODO - Update visibility of views
        //mFilmsView.setVisibility(View.VISIBLE);
        mNoFilmsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoFilms()
    {
        //TODO - Update visibility of bits
    }

    @Override
    public void showFilmDetailsUi(String filmId) {
        Intent intent = new Intent(getContext(), FilmDetailActivity.class);
        intent.putExtra(FilmDetailActivity.EXTRA_FILM_ID, filmId);
        startActivity(intent);
    }

    @Override
    public void showLoadingFilmsError()
    {
        //TODO - Make string constant
        showMessage("Error while loading films");
    }

    private void showMessage(String message)
    {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive()
    {
        return isAdded();
    }

//    private void refreshList() {
//        String regionCode = Helpers.getRegionCode(getActivity());
//
//        Calendar calendar = Calendar.getInstance();
//
//        String calendarString = CalendarUtils.calendarToString(calendar);
//
//        TheMovieDbService theMovieDbService = TheMovieDbService.retrofit.create(TheMovieDbService.class);
//
//        final Call<FilmResults> nowPlayingFilms = theMovieDbService.nowPlaying(getString(R.string
//                .moviedb_api_key), regionCode, calendarString);
//
//        nowPlayingFilms.enqueue(new Callback<FilmResults>() {
//            @Override
//            public void onResponse(Call<FilmResults> call, Response<FilmResults> response) {
//                mSwipeRefreshLayout.setRefreshing(false);
//
//                FilmResults films = response.body();
//                if (films != null) {
//                    Uri nowShowingUri = FilmsProvider.NowShowingFilms.CONTENT_URI;
//                    deleteAllFilms();
//                    for (Film film : films.getFilms()) {
//                        // Only care about films with a poster and backdrop
//                        if (!TextUtils.isEmpty(film.getPosterPath()) && !TextUtils.isEmpty(film.getBackdropPath())) {
//                            FilmsDbHelper.insertFilm(getActivity(), nowShowingUri, film);
//                        }
//                    }
//                    fillList();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FilmResults> call, Throwable t) {
//                Crashlytics.log(Log.ERROR, LOG_TAG, "Failed to get response for upcoming releases. " + t.getMessage());
//
//                mSwipeRefreshLayout.setRefreshing(false);
//
//                fillList();
//            }
//        });
//    }
//
//    private void fillList() {
//        mFilms.clear();
//
//        Uri nowShowingUri = FilmsProvider.NowShowingFilms.CONTENT_URI;
//        Cursor allFilms = FilmsDbHelper.getAllFilms(getActivity(), nowShowingUri);
//        if (allFilms != null) {
//            while (allFilms.moveToNext()) {
//                String filmJson = allFilms.getString(allFilms.getColumnIndex(FilmColumns.FILM));
//                Film film = new Gson().fromJson(filmJson, Film.class);
//                mFilms.add(film);
//            }
//            allFilms.close();
//        }
//
//        Collections.sort(mFilms, new FilmComparator());
//        mFilmsAdapter.updateData(mFilms);
//    }
//
//    private void deleteAllFilms() {
//        getActivity().getContentResolver().delete(FilmsProvider.NowShowingFilms.CONTENT_URI, "1", null);
//    }

}
