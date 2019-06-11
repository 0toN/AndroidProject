package com.xwm.base.util

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.RequiresPermission

/**
 * @author Created by Adam on 2018-11-08
 */
object NetworkUtil {
    /**
     * Return whether network is connected.
     *
     * Must hold
     * `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
     *
     * @return `true`: connected<br></br>`false`: disconnected
     */
    val isConnected: Boolean
        @RequiresPermission(ACCESS_NETWORK_STATE)
        get() {
            val context = Utils.app
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info: NetworkInfo? = cm.activeNetworkInfo
            return info != null && info.isConnected
        }
}
