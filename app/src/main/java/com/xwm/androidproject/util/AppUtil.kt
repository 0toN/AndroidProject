package com.xwm.androidproject.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.text.TextUtils
import com.xwm.androidproject.App

object AppUtil {

    /**
     * 判断当前应用是否是debug状态
     */
    val isApkInDebug: Boolean
        get() {
            val context = App.context
            try {
                val info = context.applicationInfo
                return info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            } catch (e: Exception) {
                return false
            }

        }

    /**
     * Return whether the app is installed.
     *
     * @param context
     * @param packageName
     * @return
     */
    fun isAppInstalled(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        return !TextUtils.isEmpty(packageName) && packageManager.getLaunchIntentForPackage(packageName) != null
    }

}
