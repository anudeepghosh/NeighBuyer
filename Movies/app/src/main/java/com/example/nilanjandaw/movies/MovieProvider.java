package com.example.nilanjandaw.movies;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Nilanjan Daw on 21/03/2016.
 * Project: Movies
 */

@ContentProvider(authority = MovieProvider.AUTHORITY,
        database = MovieDatabase.class,
        packageName = "com.example.nilanjandaw.movies.provider"
)
public class MovieProvider {

    public static final String AUTHORITY = "com.example.nilanjandaw.movies.MovieProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.MOVIES)
    public static class Movies {

        @ContentUri(
                path = MovieDatabase.MOVIES,
                type = "vnd.android.cursor.item/movie",
                defaultSort = Columns.TITLE + " ASC"
        )
        public static final Uri CONTENT_URI = buildUri(MovieDatabase.MOVIES);

        @InexactContentUri(
                name = "FAVOURITE_MOVIE_ID",
                path = MovieDatabase.MOVIES + "/#",
                type = "vnd.android.cursor.item/favourite_movie",
                whereColumn = Columns._ID,
                pathSegment = 1
        )
        public static Uri withId(long id) {
            return buildUri(MovieDatabase.MOVIES, String.valueOf(id));
        }

        @InexactContentUri(
                name = "FAVOURITE_MOVIE_TITLE",
                path = MovieDatabase.MOVIES + "/$",
                type = "vnd.android.cursor.item/favourite_movie",
                whereColumn = Columns.TITLE,
                pathSegment = 1
        )
        public static Uri withTitle(String title) {
            return buildUri(MovieDatabase.MOVIES, title);
        }
    }
}
