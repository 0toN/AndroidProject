package com.xwm.androidproject.view;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义View的基类
 *
 * @author Created by Adam on 2018-12-13
 */
public class CustomView extends View {

    /**
     * the width of current view.
     */
    protected int mWidth;

    /**
     * the height of current view.
     */
    protected int mHeight;

    /**
     * default Paint.
     */
    protected Paint mDeafultPaint = new Paint();

    /**
     * default TextPaint
     */
    protected TextPaint mDefaultTextPaint = new TextPaint();


    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }
}
