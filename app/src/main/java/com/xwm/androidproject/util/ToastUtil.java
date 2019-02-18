package com.xwm.androidproject.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.xwm.androidproject.MyApplication;

/**
 * Toast消息辅助类
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 显示Toast短消息
     *
     * @param msg 内容
     */
    public static void showShort(String msg) {
        if (toast != null) {
            toast.cancel();
        }
        Context context = MyApplication.getContext();
        if (context != null && !TextUtils.isEmpty(msg)) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * 显示Toast短消息
     *
     * @param resId String id
     */
    public static void showShort(int resId) {
        if (toast != null) {
            toast.cancel();
        }
        Context context = MyApplication.getContext();
        if (context != null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    /**
     * 显示Toast长消息
     *
     * @param msg 内容
     */
    public static void showLong(String msg) {
        if (toast != null) {
            toast.cancel();
        }
        Context context = MyApplication.getContext();
        if (context != null && !TextUtils.isEmpty(msg)) {
            toast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * 显示Toast长消息
     *
     * @param resId String id
     */
    public static void showLong(int resId) {
        if (toast != null) {
            toast.cancel();
        }
        Context context = MyApplication.getContext();
        if (context != null) {
            toast = Toast.makeText(MyApplication.getContext(), resId, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
