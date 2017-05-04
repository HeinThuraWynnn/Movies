package io.github.emrys919.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.emrys919.movies.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movie.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_MOVIE_TABLE =
            "CREATE TABLE " + MovieEntry.TABLE_NAME    + " ("
            + MovieEntry._ID                           + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieEntry.COLUMN_MOVIE_ADULT            + " BOOL, "
            + MovieEntry.COLUMN_MOVIE_BACKDROP_PATH    + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_GENRE_IDS        + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_OVERVIEW         + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE   + " TEXT, "
            //+ MovieEntry.COLUMN_MOVIE_BOOKMARK         + " INTEGER, "
            + MovieEntry.COLUMN_MOVIE_LANGUAGES        + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_POPULARTY        + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_POSTER_PATH      + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_RELEASE_DATE     + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_TITLE            + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_VIDEO            + " BOOL, "
            //+ MovieEntry.COLUMN_MOVIE_TYPE             + " TEXT,"
            + MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE     + " TEXT, "
            + MovieEntry.COLUMN_MOVIE_VOTE_COUNT       + " TEXT);";

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
