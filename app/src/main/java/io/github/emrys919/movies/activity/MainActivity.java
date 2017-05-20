package io.github.emrys919.movies.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.emrys919.movies.R;
import io.github.emrys919.movies.adapter.MovieAdapter;
import io.github.emrys919.movies.data.MovieContract;
import io.github.emrys919.movies.sync.MovieSyncTask;
import io.github.emrys919.movies.util.Constants;
import io.github.emrys919.movies.util.MovieDbUtils;

import static android.R.attr.id;
import static io.github.emrys919.movies.util.Constants.ID_MAIN_LOADER;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        MovieAdapter.MovieOnClickHandler, SwipeRefreshLayout.OnRefreshListener,
        OnMoreListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rc_movie)
    SuperRecyclerView mMovieRecycler;

    private MovieAdapter mMovieAdapter;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mMovieAdapter = new MovieAdapter(this, this);
        mMovieRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMovieRecycler.setAdapter(mMovieAdapter);
        mMovieRecycler.setRefreshListener(this);
        mMovieRecycler.setOnMoreListener(this);

        getSupportLoaderManager().initLoader(ID_MAIN_LOADER, null, this);

        MovieSyncTask.syncMovie(this, page);
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

    @Override
    public void onClick(String uri) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Uri uriForClickedMovie = MovieDbUtils.buildWeatherUriWithDate(uri);
        intent.setData(uriForClickedMovie);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refreshing..", Toast.LENGTH_SHORT).show();
        MovieSyncTask.syncMovie(this, page);
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        page ++;
        MovieSyncTask.syncMovie(this, page);
    }
}
