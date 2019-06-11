package com.xwm.base.util

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewConfiguration
import android.view.Window

/**
 * @author Created by Adam on 2018-08-09
 */
object ScreenUtil {

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private val isNavBarOverride: String?
        get() {
            var sNavBarOverride: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    val c = Class.forName("android.os.SystemProperties")
                    val m = c.getDeclaredMethod("get", String::class.java)
                    m.isAccessible = true
                    sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
                } catch (ignored: Throwable) {
                }

            }
            return sNavBarOverride
        }

    /**
     * 获取屏幕宽高
     *
     * @param activity
     * @return
     */
    fun getScreenSize(activity: Activity): Point {
        val display = activity.windowManager.defaultDisplay
        val screenSize = Point()
        display.getSize(screenSize)
        if (isComprehensiveScreenMode(activity)) {
            screenSize.y += getNavigationBarHeight(activity)
        }
        return screenSize
    }

    /**
     * 隐藏虚拟按键
     *
     * @param context
     * @param window
     */
    fun hideNavBar(context: Context, window: Window) {
        if (!hasNavBar(context)) {
            return
        }
        val decorView = window.decorView
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            decorView.systemUiVisibility = View.GONE
        } else {
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
            decorView.systemUiVisibility = uiOptions
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 检测MIUI等国产操作系统是否开启全面屏手势操作
     *
     * @param context
     * @return
     */
    fun isComprehensiveScreenMode(context: Context): Boolean {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            result = Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0)
        }
        return result != 0
    }

    /**
     * 获取虚拟按键的高度
     *
     * @param context
     * @return
     */
    fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        if (hasNavBar(context)) {
            val resources = context.resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    fun hasNavBar(context: Context): Boolean {
        val res = context.resources
        val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
        if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId)
            val sNavBarOverride = isNavBarOverride
            if ("1" == sNavBarOverride) {
                hasNav = false
            } else if ("0" == sNavBarOverride) {
                hasNav = true
            }
            return hasNav
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey()
        }
    }
}
