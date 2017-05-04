package io.github.emrys919.movies.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.emrys919.movies.R;
import io.github.emrys919.movies.adapter.MovieAdapter;
import io.github.emrys919.movies.data.MovieContract;
import io.github.emrys919.movies.data.MovieContract.MovieEntry;
import io.github.emrys919.movies.model.Movie;
import io.github.emrys919.movies.model.MoviesResponse;
import io.github.emrys919.movies.rest.ApiClient;
import io.github.emrys919.movies.rest.ApiInterface;
import io.github.emrys919.movies.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.id;
import static io.github.emrys919.movies.util.Constants.ID_MAIN_LOADER;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.rc_movie) RecyclerView mMovieRecycler;

    private MovieAdapter mMovieAdapter;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        requestApi();

        mMovieAdapter = new MovieAdapter(this);
        mMovieRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMovieRecycler.setHasFixedSize(true);
        mMovieRecycler.setAdapter(mMovieAdapter);

        getSupportLoaderManager().initLoader(ID_MAIN_LOADER, null, this);
    }

    private void requestApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getTopRatedMovies(Constants.API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    movieList = response.body().getResults();
                    int responseSize = movieList.size();
                    Log.i(TAG, "Response size : " + responseSize);
                    ContentValues[] movieValues = new ContentValues[responseSize];
                    if (responseSize > 0) {
                        for (int i=0; i<responseSize; i++) {
                            Movie movie = movieList.get(i);
                            ContentValues values = new ContentValues();
                            values.put(MovieEntry._ID, movie.getId());
                            values.put(MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
                            values.put(MovieEntry.COLUMN_MOVIE_GENRE_IDS, movie.getGenreIds().toString());
                            values.put(MovieEntry.COLUMN_MOVIE_LANGUAGES, movie.getOriginalLanguage());
                            values.put(MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
                            values.put(MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
                            values.put(MovieEntry.COLUMN_MOVIE_POPULARTY, movie.getPopularity() + "");
                            values.put(MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
                            values.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
                            values.put(MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
                            values.put(MovieEntry.COLUMN_MOVIE_VIDEO, movie.getVideo());
                            values.put(MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage() + "");
                            values.put(MovieEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount() + "");

                            movieValues[i] = values;
                        }

                        if (movieValues.length != 0) {
                            ContentResolver resolver = getContentResolver();
                            resolver.delete(
                                    MovieEntry.MOVIE_CONTENT_URI,
                                    null,
                                    null
                            );

                            resolver.bulkInsert(
                                    MovieEntry.MOVIE_CONTENT_URI,
                                    movieValues
                            );

                            resolver.notifyChange(MovieEntry.MOVIE_CONTENT_URI, null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Check your connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case ID_MAIN_LOADER:
                Log.e(TAG, "on create loader returning" + i);
                return new CursorLoader(
                        this,
                        MovieContract.MovieEntry.MOVIE_CONTENT_URI,
                        Constants.MOVIE_PROJECTION,
                        null,
                        null,
                        null
                );

            default:
                throw new RuntimeException("Loader hasn't implemented." + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mMovieAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }
}
