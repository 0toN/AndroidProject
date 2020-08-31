package com.xwm.base.config

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.didichuxing.doraemonkit.DoraemonKit
import com.tencent.mmkv.MMKV
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
        if (shouldInit(app)) {
            DoraemonKit.install(app, "a8be61c3898d0a9ac0f4eee66680f58c")
        }
        MMKV.initialize(app)
    }

    private fun shouldInit(app: Application): Boolean {
        val am = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfos = am.runningAppProcesses
        val mainProcessName = app.packageName
        val myPid = android.os.Process.myPid()
        if (processInfos != null) {
            for (info in processInfos) {
                if (info.pid == myPid && mainProcessName == info.processName) {
                    return true
                }
            }
        }
        return false
    }
}