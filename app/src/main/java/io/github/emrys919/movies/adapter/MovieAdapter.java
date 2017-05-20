package io.github.emrys919.movies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.emrys919.movies.R;
import io.github.emrys919.movies.util.Constants;
import io.github.emrys919.movies.util.MovieDbUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    private MovieOnClickHandler mClickhandler;

    public interface MovieOnClickHandler {
        void onClick(String name);
    }

    public MovieAdapter(Context mContext, MovieOnClickHandler mClickhandler) {
        this.mContext = mContext;
        this.mClickhandler = mClickhandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String movieCoverPath = mCursor.getString(Constants.INDEX_MOVIE_POSTER_PATH);
        Picasso.with(mContext).load(Constants.IMG_BASE_URL + movieCoverPath).into(holder.movieCover);
        /*Glide.with(mContext)
                .load(Constants.IMG_BASE_URL + movieCoverPath)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.movieCover);*/

        String movieTitle = mCursor.getString(Constants.INDEX_TITLE);
        holder.movieTitle.setText(movieTitle);

        String movieGenre = mCursor.getString(Constants.INDEX_GENRE);
        String genreString = MovieDbUtils.getMovieGenre(mContext, movieGenre);
        holder.movieGenre.setText(genreString);

        String movieOverview = mCursor.getString(Constants.INDEX_OVERVIEW);
        holder.movieOverview.setText(movieOverview);

        String movieRating = mCursor.getString(Constants.INDEX_RATING);
        holder.movieRating.setText(movieRating);

        String movieReleaseDate = mCursor.getString(Constants.INDEX_MOVIE_DATE);
        holder.movieReleaseDate.setText(movieReleaseDate);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        @BindView(R.id.movie_cover) ImageView movieCover;
        @BindView(R.id.movie_title) TextView movieTitle;
        @BindView(R.id.movie_genre) TextView movieGenre;
        @BindView(R.id.movie_overview) TextView movieOverview;
        @BindView(R.id.movie_rating) TextView movieRating;
        @BindView(R.id.movie_release_date) TextView movieReleaseDate;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            String movieUri = mCursor.getString(Constants.INDEX_MOVIE_ID);
            mClickhandler.onClick(movieUri);
        }
    }
}
