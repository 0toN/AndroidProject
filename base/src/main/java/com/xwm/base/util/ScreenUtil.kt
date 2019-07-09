package com.xwm.base.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.view.WindowManager



/**
 * @author Created by Adam on 2018-08-09
 */
object ScreenUtil {

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    fun getScreenWidth(): Int {
        val wm = Utils.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    fun getScreenHeight(): Int {
        val wm = Utils.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.y
    }

    /**
     * Return the density of screen.
     *
     * @return the density of screen
     */
    fun getScreenDensity(): Float {
        return Resources.getSystem().displayMetrics.density
    }

    /**
     * Return the screen density expressed as dots-per-inch.
     *
     * @return the screen density expressed as dots-per-inch
     */
    fun getScreenDensityDpi(): Int {
        return Resources.getSystem().displayMetrics.densityDpi
    }
}
