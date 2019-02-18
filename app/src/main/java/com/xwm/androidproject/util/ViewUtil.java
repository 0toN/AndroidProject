package com.xwm.androidproject.util;

import android.util.Log;
import android.view.View;

/**
 * View相关通用逻辑类
 */
public class ViewUtil {
    private static final String TAG = "ViewUtil";

    private static View.OnTouchListener onTouchListener = (view, event) -> {
        view.performClick();
        return true;
    };

    public static void show(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hide(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public static void invisible(View view) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static boolean isVisible(View view) {
        if (view == null) {
            return false;
        }
        return view.getVisibility() == View.VISIBLE;
    }

    public static void toggle(View view) {
        if (view != null) {
            view.setVisibility(view.isShown() ? View.GONE : View.VISIBLE);
        }
    }

    public static void showIf(View view, boolean condition) {
        if (view != null) {
            view.setVisibility(condition ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 消费View的Touch事件，防止touch渗透
     *
     * @param view
     */
    public static void shieldTouchEvent(View view) {
        if (view != null) {
            view.setOnTouchListener(onTouchListener);
        }
    }

    /**
     * 偏移
     *
     * @param view
     * @param xOffset
     * @param yOffset
     */
    public static void offset(View view, int xOffset, int yOffset) {
        int left = view.getLeft() + xOffset;
        int top = view.getTop() + yOffset;
        int width = view.getWidth();
        int height = view.getHeight();
        int right = left + width;
        int bottom = top + height;
        view.layout(left, top, right, bottom);
        Log.d(TAG, "move margin left : " + left + " top : " + top + " view : " + view);
//        view.setTag(R.id.center, null);
    }
}
