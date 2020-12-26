package com.xwm.base.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager

/**
 * 软键盘隐藏显示工具类
 */
object KeyboardUtil {

    /**
     * 隐藏软键盘
     */
    fun hideSoftKeyboard(view: View) {
        view.requestFocus()
        val binder = view.windowToken
        val manager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(binder, 0)
    }

    /**
     * 显示软键盘
     */
    fun showSoftKeyboard(view: View) {
        view.requestFocus()
        val manager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(view, 0)
    }

    /**
     * 判断软键盘是否显示
     */
    fun isKeyboardShowing(window: Window): Boolean {
        val decorView: View = window.decorView
        //获取当前屏幕的高度
        val screenHeight = decorView.height
        val rect = Rect()
        //获取View可见区域的bottom
        decorView.getWindowVisibleDisplayFrame(rect)
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom
    }

    /**
     * 获取输入法的高度
     */
    fun getKeyboardHeight(window: Window): Int {
        val decorView: View = window.decorView
        val screenHeight = decorView.height
        val rect = Rect()
        decorView.getWindowVisibleDisplayFrame(rect)
        return screenHeight - rect.bottom
    }
}