package io.github.emrys919.movies.util;

import io.github.emrys919.movies.data.MovieContract;

/**
 * Created by myo on 5/2/17.
 */

public class Constants {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public static final String API_KEY = "4d74ac9c6ddf771e77f46ab810c373bf";

    public static final int ID_MAIN_LOADER = 10;

    public static final String[] MOVIE_PROJECTION =  {
            MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_GENRE_IDS,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE
    };
    public static final int INDEX_TITLE = 0;
    public static final int INDEX_GENRE = 1;
    public static final int INDEX_OVERVIEW = 2;
    public static final int INDEX_RATING = 3;
    public static final int INDEX_MOVIE_POSTER_PATH = 4;
    public static final int INDEX_MOVIE_DATE = 5;
}
