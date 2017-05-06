package io.github.emrys919.movies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by emrys on 5/6/17.
 */

public class MovieSyncService extends IntentService {

    public MovieSyncService() {
        super("MovieSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MovieSyncTask.syncMovie(this);
    }
}
