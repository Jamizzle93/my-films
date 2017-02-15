package com.mysticwater.myfilms.data.source;

import com.mysticwater.myfilms.data.Film;

import java.util.Map;

/**
 * Concrete implementation to load films from the data sources into a cache.
 * <p/>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class FilmsRepository implements FilmsDataSource {

    private static FilmsRepository INSTANCE = null;

    private final FilmsDataSource mFilmsRemoteDataSource;
    private final FilmsDataSource mFilmsLocalDataSource;

    Map<String, Film> mCachedFilms;

    private FilmsRepository(FilmsDataSource filmsRemoteDataSource,
                            FilmsDataSource filmsLocalDataSource)
    {
        mFilmsRemoteDataSource = filmsRemoteDataSource;
        mFilmsLocalDataSource = filmsLocalDataSource;
    }

    public static FilmsRepository getInstance(FilmsDataSource fil)

}
