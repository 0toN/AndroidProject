package com.xwm.base.config

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.xwm.base.util.Utils

/**
 * Created by xwm on 2019-06-06
 */
class AppConfig {

    companion object {
        val INSTANCE by lazy {
            AppConfig()
        }
    }

    fun initConfig(app: Application) {
        Utils.init(app)
        initLeakCanary(app)
    }

    private fun initLeakCanary(app: Application) {
        if (LeakCanary.isInAnalyzerProcess(app.applicationContext)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(app)
    }
}