package io.github.emrys919.movies.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.emrys919.movies.R;
import io.github.emrys919.movies.adapter.MovieAdapter;
import io.github.emrys919.movies.model.Movie;
import io.github.emrys919.movies.model.MoviesResponse;
import io.github.emrys919.movies.rest.ApiClient;
import io.github.emrys919.movies.rest.ApiInterface;
import io.github.emrys919.movies.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

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

        mMovieAdapter = new MovieAdapter(movieList);
        mMovieRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMovieRecycler.setHasFixedSize(true);
        mMovieRecycler.setAdapter(mMovieAdapter);
    }

    private void requestApi() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getTopRatedMovies(Constants.API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    movieList.addAll(response.body().getResults());
                    mMovieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
    }
}
