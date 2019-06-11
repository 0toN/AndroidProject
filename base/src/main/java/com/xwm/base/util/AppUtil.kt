package com.xwm.base.util

import android.content.Context
import android.text.TextUtils

object AppUtil {

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
