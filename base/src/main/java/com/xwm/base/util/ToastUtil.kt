package com.xwm.base.util

import android.text.TextUtils
import android.widget.Toast

/**
 * Toast消息辅助类
 */
object ToastUtil {
    private lateinit var toast: Toast

    /**
     * 显示Toast短消息
     *
     * @param msg 内容
     */
    fun showShort(msg: String) {
        toast.cancel()
        if (!TextUtils.isEmpty(msg)) {
            toast = Toast.makeText(Utils.app, msg, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    /**
     * 显示Toast短消息
     *
     * @param resId String id
     */
    fun showShort(resId: Int) {
        toast.cancel()
        toast = Toast.makeText(Utils.app, resId, Toast.LENGTH_SHORT)
        toast.show()
    }


    /**
     * 显示Toast长消息
     *
     * @param msg 内容
     */
    fun showLong(msg: String) {
        toast.cancel()
        if (!TextUtils.isEmpty(msg)) {
            toast = Toast.makeText(Utils.app, msg, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    /**
     * 显示Toast长消息
     *
     * @param resId String id
     */
    fun showLong(resId: Int) {
        toast.cancel()
        toast = Toast.makeText(Utils.app, resId, Toast.LENGTH_LONG)
        toast.show()
    }
}
