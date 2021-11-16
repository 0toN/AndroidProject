package com.xwm.base.util

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import java.net.NetworkInterface
import java.net.SocketException


/**
 * @author Created by Adam on 2018-11-08
 */
object NetworkUtil {

    private val connectivityManager by lazy {
        Utils.app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    @RequiresPermission(ACCESS_NETWORK_STATE)
    private fun getActiveNetworkInfo(): NetworkInfo? {
        return connectivityManager.activeNetworkInfo
    }

    /**
     * Return whether network is connected.
     *
     * Must hold `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
     *
     * @return `true`: connected<br></br>`false`: disconnected
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isConnected(): Boolean {
        val info = getActiveNetworkInfo()
        return info != null && info.isConnected
    }

    /**
     * Return whether using mobile data.
     *
     * Must hold `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
     *
     * @return `true`: yes<br></br>`false`: no
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isMobileData(): Boolean {
        val info = getActiveNetworkInfo()
        return (null != info && info.isAvailable
                && info.type == ConnectivityManager.TYPE_MOBILE)
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isWifiData(): Boolean {
        val info = getActiveNetworkInfo()
        return (null != info && info.isAvailable
                && info.type == ConnectivityManager.TYPE_WIFI)
    }

    fun getIPAddr(): String {
        var ip = ""
        val mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        if (mobileNetworkInfo != null && mobileNetworkInfo.isConnected) {
            ip = getLocalIpAddress()
        } else if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected) {
            ip = getWifiIPAddress()
        }
        return ip
    }

    //GPRS连接下的ip
    private fun getLocalIpAddress(): String {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        return inetAddress.hostAddress.toString()
                    }
                }
            }
        } catch (ex: SocketException) {
        }
        return ""
    }

    private fun getWifiIPAddress(): String {
        val wifiManager = Utils.app.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifiManager.connectionInfo
        val sb = StringBuilder()
        sb.append(info.ipAddress and 0xFF).append(".")
        sb.append(info.ipAddress shr 8 and 0xFF).append(".")
        sb.append(info.ipAddress shr 16 and 0xFF).append(".")
        sb.append(info.ipAddress shr 24 and 0xFF)
        return sb.toString()
    }
}
