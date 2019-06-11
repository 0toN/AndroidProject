package com.xwm.androidproject

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import androidx.multidex.MultiDex
import com.xwm.base.config.AppConfig
import com.xwm.base.config.GlideApp
import com.xwm.base.util.LogUtil

/**
 * @author Created by Adam on 2019-02-15
 */
class App : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        AppConfig.INSTANCE.initConfig(this)
    }


    /**
     * 程序终止的时候执行
     */
    override fun onTerminate() {
        super.onTerminate()
        LogUtil.d("onTerminate")
    }


    /**
     * 低内存的时候执行
     */
    override fun onLowMemory() {
        super.onLowMemory()
        LogUtil.d("onLowMemory")
        GlideApp.get(this).clearMemory()
    }

    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        LogUtil.d("onTrimMemory")
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            GlideApp.get(this).clearMemory()
        }
        GlideApp.get(this).trimMemory(level)
    }
}