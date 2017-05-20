package io.github.emrys919.movies.sync;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import static io.github.emrys919.movies.util.Constants.SYNC_FLEXTIME_SEC;
import static io.github.emrys919.movies.util.Constants.SYNC_INTERVAL_SEC;

public class MovieSyncUtils {
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
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SEC, SYNC_INTERVAL_SEC + SYNC_FLEXTIME_SEC))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncMovieJob);
    }

    synchronized public static void initialize(final Context context) {
        if (sInitialized) {
            return;
        }
        //scheduleMovieSync(context);
        startSync(context);
        sInitialized = true;
    }

    private static void startSync(Context context) {

    }
}
