package com.xwm.androidproject.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 软键盘隐藏显示工具类
 */
object KeyboardUtil {

    /**
     * 隐藏软键盘
     */
    fun hideSoftKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val binder = view.windowToken
            val manager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 显示软键盘
     */
    fun showSoftKeyboard(activity: Activity, editText: EditText?) {
        if (editText != null) {
            editText.requestFocus()
            val manager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(editText, 0)
        }
    }
}
