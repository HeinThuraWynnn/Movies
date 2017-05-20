package io.github.emrys919.movies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MovieProvider extends ContentProvider {

    private MovieDbHelper movieDbHelper;

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_COLUMN = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String auth = MovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(auth, MovieContract.PATH_MOVIE, CODE_MOVIE);
        uriMatcher.addURI(auth, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_COLUMN);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insert(MovieContract.MovieEntry.TABLE_NAME,
                                null, value);
                        if (id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CODE_MOVIE_WITH_COLUMN:
                String movieId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieId};
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + " =? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                Log.e("Emrys", "Unknown Uri " + uri);
        }
        if (cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int rowDeleted = 0;

        if (selection == null) selection = "1";

        if (sUriMatcher.match(uri) == CODE_MOVIE) {
            rowDeleted = db.delete(
                    MovieContract.MovieEntry.TABLE_NAME,
                    selection,
                    selectionArgs
            );
        }

        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int rowUpdated = 0;

        if (sUriMatcher.match(uri) == CODE_MOVIE) {
            rowUpdated = db.update(
                    MovieContract.MovieEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );
        }

        if (rowUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowUpdated;
    }
}
