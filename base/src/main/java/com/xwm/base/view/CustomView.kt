package com.xwm.base.view

import android.content.Context
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 * 自定义View的基类
 *
 * @author Created by Adam on 2018-12-13
 */
class CustomView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    /**
     * the width of current view.
     */
    protected var mWidth: Int = 0

    /**
     * the height of current view.
     */
    protected var mHeight: Int = 0

    /**
     * default Paint.
     */
    protected var mDeafultPaint = Paint()

    /**
     * default TextPaint
     */
    protected var mDefaultTextPaint = TextPaint()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }
}
