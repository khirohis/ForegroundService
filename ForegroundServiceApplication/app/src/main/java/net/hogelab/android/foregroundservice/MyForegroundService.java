package net.hogelab.android.foregroundservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyForegroundService extends Service {
    private static final String TAG = MyForegroundService.class.getSimpleName();

    static class MessageHandler extends Handler {
        private Context applicationContext;

        MessageHandler(Context context) {
            applicationContext = context.getApplicationContext();
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
            // Notification
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

        return false;
    }
}
