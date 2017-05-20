package io.github.emrys919.movies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.List;

import io.github.emrys919.movies.data.MovieContract.MovieEntry;
import io.github.emrys919.movies.model.Movie;
import io.github.emrys919.movies.model.MoviesResponse;
import io.github.emrys919.movies.rest.ApiClient;
import io.github.emrys919.movies.rest.ApiInterface;
import io.github.emrys919.movies.util.Constants;
import io.github.emrys919.movies.util.MovieDbUtils;
import io.github.emrys919.movies.util.MovieNotiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class MovieSyncTask {

    synchronized public static void syncMovie(final Context context, int page) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getPopularMovies(Constants.API_KEY, page);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {

                    List<Movie> movieList = response.body().getResults();
                    int responseSize = movieList.size();
                    Log.i(TAG, "Response size : " + responseSize);

                    if (responseSize > 0) {

                        ContentValues[] movieValues =
                                MovieDbUtils.getMovieContentValues(movieList);

                        if (movieValues.length != 0) {
                            ContentResolver resolver = context.getContentResolver();
                            /*resolver.delete(
                                    MovieEntry.MOVIE_CONTENT_URI,
                                    null,
                                    null
                            );*/

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
            }
        });

        long ellipseTime = MovieNotiUtils.getTimePeriodFromLastNoti(context);

        if (ellipseTime >= DateUtils.DAY_IN_MILLIS ) {
            MovieNotiUtils.notifyMovie(context);
        }
    }
}
