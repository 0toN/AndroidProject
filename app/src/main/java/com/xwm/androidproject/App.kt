package com.xwm.androidproject

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

import com.squareup.leakcanary.LeakCanary

/**
 * @author Created by Adam on 2019-02-15
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        initLeakCanary()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}