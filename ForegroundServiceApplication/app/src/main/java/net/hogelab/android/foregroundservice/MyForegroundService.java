package net.hogelab.android.foregroundservice;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyForegroundService extends Service {
    private static final String TAG = MyForegroundService.class.getSimpleName();

    public static int NOTIFICATION_ID = 1;

    public static final int MESSAGE_WHAT_START_WIFILOCK = 1000;
    public static final int MESSAGE_WHAT_STOP_WIFILOCK = 1001;

    private static final long PERIODIC_TASK_DELAY_MILLIS = 10 * 1000;


    static class MessageHandler extends Handler {
        private final WeakReference<MyForegroundService> service;

        MessageHandler(MyForegroundService service) {
            this.service = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull Message message) {
            boolean handled = false;

            MyForegroundService service = this.service.get();
            if (service != null) {
                handled = service.handleMessage(message);
            }

            if (!handled) {
                super.handleMessage(message);
            }
        }
    }


    private Handler handler;
    private Messenger messenger;

    private PowerManager powerManager;
    private WifiManager.WifiLock wifiLock;

    private Runnable periodicTask;


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        super.onCreate();

        handler = new Handler(Looper.getMainLooper());
        messenger = new Messenger(new MessageHandler(this));

        powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        WifiManager wiFiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wiFiManager != null) {
            wifiLock = wiFiManager.createWifiLock(getPackageName());
        }

        periodicTask = this::onPeriodicTask;
        handler.postDelayed(periodicTask, PERIODIC_TASK_DELAY_MILLIS);
    }

    @Override
    public int onStartCommand(Intent startIntent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        // startService 引数の Intent
//        if (startIntent != null) {
//            Bundle extras = startIntent.getExtras();
//            if (extras != null) {
//            }
//        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, createDefaultNotification());
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

        return messenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");

        return false;
    }


    private boolean handleMessage(@NonNull Message message) {
        boolean handled = false;

        switch (message.what) {
            case MESSAGE_WHAT_START_WIFILOCK:
                startWifiLock();
                handled = true;
                break;

            case MESSAGE_WHAT_STOP_WIFILOCK:
                stopWifiLock();
                handled = true;
                break;

            default:
                break;
        }

        return handled;
    }

    private void startWifiLock() {
        if (wifiLock != null) {
            if (!wifiLock.isHeld()) {
                wifiLock.acquire();
            }
        }
    }

    private void stopWifiLock() {
        if (wifiLock != null) {
            if (wifiLock.isHeld()) {
                wifiLock.release();
            }
        }
    }


    private void onPeriodicTask() {
        Log.d(TAG, "onPeriodicTask");

        Thread pingTask = new Thread(this::ping);
        pingTask.start();

        handler.postDelayed(periodicTask, PERIODIC_TASK_DELAY_MILLIS);
    }


    private void ping() {
        try {
            URL pingUrl = new URL("http://www.google.com/");
            HttpURLConnection connection = (HttpURLConnection) pingUrl.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            int statusCode = connection.getResponseCode();
            Log.d(TAG, "ping: statusCode=" + statusCode);
        } catch (MalformedURLException e) {
            Log.d(TAG, "ping: MalformedURLException");
        } catch (IOException e) {
            Log.d(TAG, "ping: IOException");
        }
    }


    private Notification createDefaultNotification() {
        return new NotificationCompat.Builder(
                this,
                ForegroundServiceApplication.CHANNEL_ID)
                .setContentTitle(ForegroundServiceApplication.CHANNEL_NAME)
                .build();
    }
}
