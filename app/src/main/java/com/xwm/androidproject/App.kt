package com.xwm.androidproject

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import com.bumptech.glide.Glide
import com.xwm.base.config.AppConfig
import com.xwm.base.util.LogUtil

/**
 * @author Created by Adam on 2019-02-15
 */
class App : Application(), ViewModelStoreOwner {

    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        AppConfig.INSTANCE.initConfig(this)
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

    /**
     * 低内存的时候执行
     */
    override fun onLowMemory() {
        super.onLowMemory()
        LogUtil.d("onLowMemory")
        Glide.get(this).clearMemory()
    }

    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        LogUtil.d("onTrimMemory")
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }
}