package net.hogelab.android.foregroundservice;

import android.content.Intent;

import java.lang.ref.WeakReference;

public class MainViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private final WeakReference<MainActivity> activityReference;

    MainViewModel(MainActivity activity) {
        activityReference = new WeakReference<>(activity);
    }


    public void onClickWifiStart() {
    }

    public void onClickWifiStop() {
    }

    public void onClickSubActivity() {
        MainActivity activity = activityReference.get();
        if (activity != null) {
            Intent intent = new Intent(activity, SubActivity.class);
            activity.startActivity(intent);
        }
    }
}
