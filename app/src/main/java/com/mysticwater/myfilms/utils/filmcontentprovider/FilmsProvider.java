package com.mysticwater.myfilms.utils.filmcontentprovider;

import android.net.Uri;

import com.mysticwater.myfilms.BuildConfig;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = FilmsProvider.AUTHORITY, database = FilmsDatabase.class)
public final class FilmsProvider {

    //public static final String AUTHORITY = "com.mysticwater.myfilms.utils.filmcontentprovider.FilmsProvider";
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String FILMS = "films";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = FilmsDatabase.FILMS)
    public static class Films
    {
        @ContentUri(
                path = Path.FILMS,
                type = "vnd.android.cursor.dir/film",
                defaultSort = FilmColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.FILMS);

    }
}
