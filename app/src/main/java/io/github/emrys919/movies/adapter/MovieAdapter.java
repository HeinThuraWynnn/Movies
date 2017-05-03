package io.github.emrys919.movies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.emrys919.movies.R;
import io.github.emrys919.movies.model.Movie;

/**
 * Created by myo on 5/2/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.movieTitle.setText(movieList.get(position).getTitle());
        holder.data.setText(movieList.get(position).getReleaseDate());
        holder.movieDescription.setText(movieList.get(position).getOverview());
        holder.rating.setText(String.valueOf(movieList.get(position).getVoteAverage()));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView movieTitle;
        @BindView(R.id.subtitle) TextView data;
        @BindView(R.id.description) TextView movieDescription;
        @BindView(R.id.rating) TextView rating;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
