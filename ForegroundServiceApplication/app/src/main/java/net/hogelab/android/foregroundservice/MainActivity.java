package net.hogelab.android.foregroundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.widget.TextView;

import net.hogelab.android.foregroundservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MainViewModel viewModel;
    private ActivityMainBinding binding;

    private MyForegroundServiceConnection mServiceConnection = new MyForegroundServiceConnection();
    private IBinder binder;
    private boolean isServiceBound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        viewModel = new MainViewModel(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setViewModel(viewModel);
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");

        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");

        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");

        super.onResume();

        bindMyForegroundService();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");

        super.onPause();

        unbindMyForegroundService();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");

        super.onDestroy();
    }



    // --------------------------------------------------

    private void bindMyForegroundService() {
        if (!isServiceBound) {
            Log.d(TAG, "bindService");

            bindService(new Intent(MainActivity.this, MyForegroundService.class),
                    mServiceConnection,
                    Context.BIND_AUTO_CREATE);
        }
    }

    private void unbindMyForegroundService() {
        if (isServiceBound) {
            Log.d(TAG, "unbindService");

            unbindService(mServiceConnection);
            isServiceBound = false;
        }
    }



    // --------------------------------------------------

    private class MyForegroundServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "MyForegroundServiceConnection#onServiceConnected");

            binder = service;
            Log.d(TAG, "Bound service: " + service.toString());

            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "MyForegroundServiceConnection#onServiceDisconnected");

            isServiceBound = false;
        }
    }
}
