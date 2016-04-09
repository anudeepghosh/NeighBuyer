package com.example.nilanjandaw.movies;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by Nilanjan Daw on 21/03/2016.
 * Project: Movies
 */
public interface Columns {

    @DataType(INTEGER)
    @PrimaryKey
    String _ID = "movie_id";
    @DataType(TEXT)
    @NotNull
    String TITLE = "title";
    @DataType(TEXT)
    @NotNull
    String POSTER_PATH = "poster_path";
    @DataType(TEXT)
    @NotNull
    String BACKDROP_PATH = "backdrop_path";
    @DataType(TEXT)
    @NotNull
    String OVERVIEW = "overview";
    @DataType(TEXT)
    @NotNull
    String RELEASE_DATE = "release_date";
    @DataType(TEXT)
    @NotNull
    String VOTE_AVERAGE = "vote_average";
    @DataType(TEXT)
    @NotNull
    String VOTE_COUNT = "vote_count";

}
