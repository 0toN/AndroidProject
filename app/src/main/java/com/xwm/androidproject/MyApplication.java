package com.xwm.androidproject;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * @author Created by Adam on 2019-02-15
 */
public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}