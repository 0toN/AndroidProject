package com.xwm.androidproject.base;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.xwm.androidproject.R;
import com.xwm.androidproject.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Created by Adam on 2018-08-21
 */
public class BaseDialogFragment extends DialogFragment {
    private static String sShowTag;
    private static long sLastTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (regEvent() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setWindowAnimations(R.style.DialogFragmentAnimation);
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 根据 tag 判断Dialog是否重复显示
     *
     * @param tag
     */
    protected boolean isRepeatedShow(String tag) {
        boolean result = tag.equals(sShowTag) && SystemClock.uptimeMillis() - sLastTime < 500;
        sShowTag = tag;
        sLastTime = SystemClock.uptimeMillis();
        return !result;
    }

    /**
     * bugfix：Activity 调用 onSaveInstanceState() 之后再调用 show() 或 dismiss() 方法，
     * 报 Can not perform this action after onSaveInstanceState异常
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            if (isRepeatedShow(tag)) {
                super.show(manager, tag);
            }
        } catch (IllegalStateException exception) {
            LogUtil.e(exception);
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (isRepeatedShow(tag)) {
            return super.show(transaction, tag);
        }
        return -1;
    }

    /**
     * 需要注册EventBus，则重写该方法 并返回 true
     */
    protected boolean regEvent() {
        return false;
    }

    @Override
    public void onDestroy() {
        if (regEvent()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
