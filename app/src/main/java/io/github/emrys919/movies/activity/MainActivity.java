package io.github.emrys919.movies.activity;

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
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.emrys919.movies.R;
import io.github.emrys919.movies.adapter.MovieAdapter;
import io.github.emrys919.movies.data.MovieContract;
import io.github.emrys919.movies.sync.MovieSyncUtils;
import io.github.emrys919.movies.util.Constants;
import io.github.emrys919.movies.util.MovieNotiUtils;

import static android.R.attr.id;
import static io.github.emrys919.movies.util.Constants.ID_MAIN_LOADER;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rc_movie) RecyclerView mMovieRecycler;
    @BindView(R.id.fab) FloatingActionButton fab;

    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieNotiUtils.notifyMovie(MainActivity.this);
            }
        });

        mMovieAdapter = new MovieAdapter(this);
        mMovieRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMovieRecycler.setHasFixedSize(true);
        mMovieRecycler.setAdapter(mMovieAdapter);

        getSupportLoaderManager().initLoader(ID_MAIN_LOADER, null, this);
        MovieSyncUtils.initialize(this);
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
