package com.xwm.base.util

import android.util.Log
import android.view.View
import android.view.ViewGroup

/**
 * View相关通用逻辑类
 */
object ViewUtil {
    private const val TAG = "ViewUtil"

    private val onTouchListener = View.OnTouchListener { _, _ ->
        //        view.performClick()
        true
    }

    fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    fun hide(view: View) {
        view.visibility = View.GONE
    }

    fun invisible(view: View) {
        view.visibility = View.INVISIBLE
    }

    fun isVisible(view: View): Boolean {
        return view.visibility == View.VISIBLE
    }

    fun toggle(view: View) {
        view.visibility = if (view.isShown) View.GONE else View.VISIBLE
    }

    fun showIf(view: View, condition: Boolean) {
        view.visibility = if (condition) View.VISIBLE else View.GONE
    }

    /**
     * 消费View的Touch事件，防止touch渗透
     *
     * @param view
     */
    fun shieldTouchEvent(view: View) {
        view.setOnTouchListener(onTouchListener)
    }

    fun removeFromParent(view: View?) {
        view?.let {
            val parent = view.parent
            if (parent is ViewGroup) {
                parent.removeView(view)
            }
        }
    }

    /**
     * 偏移
     *
     * @param view
     * @param xOffset
     * @param yOffset
     */
    fun offset(view: View, xOffset: Int, yOffset: Int) {
        val left = view.left + xOffset
        val top = view.top + yOffset
        val width = view.width
        val height = view.height
        val right = left + width
        val bottom = top + height
        view.layout(left, top, right, bottom)
        Log.d(TAG, "move margin left : $left top : $top view : $view")
        //        view.setTag(R.id.center, null);
    }
}
