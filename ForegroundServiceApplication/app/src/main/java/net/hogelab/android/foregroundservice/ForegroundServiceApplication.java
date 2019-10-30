package net.hogelab.android.foregroundservice;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ForegroundServiceApplication extends Application {
    private static final String TAG = ForegroundServiceApplication.class.getSimpleName();

    public static final String CHANNEL_ID = "ForegroundService Channel ID";
    public static final String CHANNEL_NAME = "ForegroundService Channel Name";
    public static final String CHANNEL_DESCRIPTION = "ForegroundService Channel Description";
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        super.onCreate();

        // Create Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Start Service
        Intent intent = new Intent(this, MyForegroundService.class);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startService(intent);
        } else {
            startForegroundService(intent);
        }
    }
}
