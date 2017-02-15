package com.mysticwater.myfilms.data.source;

import android.support.annotation.NonNull;

import com.mysticwater.myfilms.data.Film;

import java.util.List;
import java.util.Observable;

/**
 * Main entry point for films data
 */
public interface FilmsDataSource {

    Observable<List<Film>> getFilms();

    Observable<Film> getFilm(@NonNull String filmId);

}
