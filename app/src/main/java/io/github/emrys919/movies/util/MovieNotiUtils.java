package io.github.emrys919.movies.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import io.github.emrys919.movies.R;
import io.github.emrys919.movies.activity.MainActivity;

import static io.github.emrys919.movies.util.Constants.ID_MOVIE_PENDING_INTENT;

public class MovieNotiUtils {

    public static void notifyMovie(Context context) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .setSmallIcon(R.drawable.ic_movie)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("")
                .setContentText("Click to show movie lists.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.ID_MOVIE_NOTI, notificationBuilder.build());

        MovieDbUtils.saveLastNotiTime(context, System.currentTimeMillis());
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_movie);
        return largeIcon;
    }

    private static PendingIntent contentIntent(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                ID_MOVIE_PENDING_INTENT,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
}
