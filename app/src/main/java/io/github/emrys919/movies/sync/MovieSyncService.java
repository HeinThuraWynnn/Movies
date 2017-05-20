package io.github.emrys919.movies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class MovieSyncService extends IntentService {

    public MovieSyncService() {
        super("MovieSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MovieSyncTask.syncMovie(this, 1);
    }
}
