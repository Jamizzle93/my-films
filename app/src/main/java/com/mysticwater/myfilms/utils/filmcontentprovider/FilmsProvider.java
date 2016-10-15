package com.mysticwater.myfilms.utils.filmcontentprovider;

import android.net.Uri;

import com.mysticwater.myfilms.BuildConfig;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = FilmsProvider.AUTHORITY, database = FilmsDatabase.class)
public final class FilmsProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String UPCOMING_FILMS = "UpcomingFilms";
        String FAVOURITE_FILMS = "FavouriteFilms";
        String NOW_SHOWING_FILMS = "NowShowingFilms";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = FilmsDatabase.UPCOMING_FILMS)
    public static class UpcomingFilms {
        @ContentUri(
                path = Path.UPCOMING_FILMS,
                type = "vnd.android.cursor.dir/film",
                defaultSort = FilmColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.UPCOMING_FILMS);

    }

    @TableEndpoint(table = FilmsDatabase.FAVOURITE_FILMS)
    public static class FavouriteFilms {
        @ContentUri(
                path = Path.FAVOURITE_FILMS,
                type = "vnd.android.cursor.dir/film",
                defaultSort = FilmColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.FAVOURITE_FILMS);
    }

    @TableEndpoint(table = FilmsDatabase.NOW_SHOWING_FILMS)
    public static class NowShowingFilms {
        @ContentUri(
                path = Path.NOW_SHOWING_FILMS,
                type = "vnd.android.cursor.dir/film",
                defaultSort = FilmColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.NOW_SHOWING_FILMS);
    }
}
