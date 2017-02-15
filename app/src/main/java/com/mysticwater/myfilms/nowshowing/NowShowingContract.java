package com.mysticwater.myfilms.nowshowing;

import android.support.annotation.NonNull;

import com.mysticwater.myfilms.BasePresenter;
import com.mysticwater.myfilms.BaseView;
import com.mysticwater.myfilms.data.Film;

import java.util.List;

public interface NowShowingContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showFilms(List<Film> films);

        void showFilmDetailsUi(String filmId);

        void showLoadingFilmsError();

        void showNoFilms();

        boolean isActive();
    }

    interface Presenter extends BasePresenter
    {

        void result(int requestCode, int resultCode);

        void loadFilms();

        void openFilmDetails(@NonNull Film requestedFilm);

    }

}
