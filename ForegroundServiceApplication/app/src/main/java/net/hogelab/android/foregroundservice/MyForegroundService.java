package net.hogelab.android.foregroundservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.lang.ref.WeakReference;

public class MyForegroundService extends Service {
    private static final String TAG = MyForegroundService.class.getSimpleName();

    private static int MY_FOREGROUND_SERVICE_ID = 1;


    static class MessageHandler extends Handler {
        private final WeakReference<Service> service;

        MessageHandler(Service service) {
            this.service = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                default:
                    super.handleMessage(msg);
            }
        }
    }


    private Messenger messenger;


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent startIntent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        // startService 引数の Intent
        if (startIntent != null) {
            Bundle extras = startIntent.getExtras();
            if (extras != null) {
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startForeground(MY_FOREGROUND_SERVICE_ID, createDefaultNotification());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");

        messenger = new Messenger(new MessageHandler(this));

        return messenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");

        messenger = null;

        return false;
    }


    private Notification createDefaultNotification() {
        return new NotificationCompat.Builder(
                this,
                ForegroundServiceApplication.CHANNEL_ID)
                .setContentTitle(ForegroundServiceApplication.CHANNEL_NAME)
                .build();
    }
}
