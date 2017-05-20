package io.github.emrys919.movies.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.emrys919.movies.R;
import io.github.emrys919.movies.util.Constants;
import io.github.emrys919.movies.util.MovieDbUtils;

import static io.github.emrys919.movies.util.Constants.ID_DETAIL_LOADER;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.movie_cover) ImageView movieCover;
    @BindView(R.id.movie_title) TextView movieTitle;
    @BindView(R.id.movie_genre) TextView movieGenre;
    @BindView(R.id.movie_overview) TextView movieOverview;
    @BindView(R.id.movie_rating) TextView movieRating;
    @BindView(R.id.movie_release_date) TextView movieReleaseDate;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mUri = getIntent().getData();
        if (mUri == null) finish();

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_DETAIL_LOADER:
                return new CursorLoader(
                        this,
                        mUri,
                        Constants.MOVIE_PROJECTION,
                        null,
                        null,
                        null
                );
            default:
                throw new RuntimeException("Loader isn't implemented: "+ id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || !data.moveToFirst()) {
            return;
        }

        prepareMovieData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void prepareMovieData(Cursor mCursor) {
        String movieCoverPath = mCursor.getString(Constants.INDEX_MOVIE_POSTER_PATH);
        Picasso.with(this).load(Constants.IMG_BASE_URL + movieCoverPath).into(movieCover);
        /*Glide.with(mContext)
                .load(Constants.IMG_BASE_URL + movieCoverPath)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.movieCover);*/

        String movieTitleString = mCursor.getString(Constants.INDEX_TITLE);
        movieTitle.setText(movieTitleString);

        String movieGenreString = mCursor.getString(Constants.INDEX_GENRE);
        String genreString = MovieDbUtils.getMovieGenre(this, movieGenreString);
        movieGenre.setText(genreString);

        String movieOverviewString = mCursor.getString(Constants.INDEX_OVERVIEW);
        movieOverview.setText(movieOverviewString);

        String movieRatingString = mCursor.getString(Constants.INDEX_RATING);
        movieRating.setText(movieRatingString);

        String movieReleaseDateString = mCursor.getString(Constants.INDEX_MOVIE_DATE);
        movieReleaseDate.setText(movieReleaseDateString);
    }
}
