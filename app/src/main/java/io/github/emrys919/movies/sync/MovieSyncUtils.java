package io.github.emrys919.movies.sync;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import io.github.emrys919.movies.data.MovieContract;
import io.github.emrys919.movies.util.Constants;

public class MovieSyncUtils {

    private static final int SYNC_INTERVAL_MIN = 2;
    private static final int SYNC_INTERVAL_SEC = (int) TimeUnit.MINUTES.toSeconds(SYNC_INTERVAL_MIN);
    private static final int SYNC_FLEXTIME_SEC = SYNC_INTERVAL_SEC / 2;
    private static boolean sInitialized;

    private static void scheduleMovieSync(Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncMovieJob = dispatcher.newJobBuilder()
                .setTag("movie-sync")
                .setService(MovieJobService.class)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(30, 60))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncMovieJob);
    }

    synchronized public static void initialize(final Context context) {
        if (sInitialized) {
            return;
        }

        scheduleMovieSync(context);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri movieQueryUri = MovieContract.MovieEntry.MOVIE_CONTENT_URI;
                Cursor cursor = context.getContentResolver().query(
                        movieQueryUri,
                        Constants.MOVIE_PROJECTION,
                        null,
                        null,
                        null);
                if (null == cursor || cursor.getCount() == 0) {
                    startSync(context);
                }
                cursor.close();
            }
        });
        checkForEmpty.start();
        sInitialized = true;
    }

    public static void startSync(Context context) {
        MovieSyncTask.syncMovie(context);
    }
}
