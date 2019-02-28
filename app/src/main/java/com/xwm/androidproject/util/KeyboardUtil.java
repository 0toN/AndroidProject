package com.xwm.androidproject.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class KeyboardUtil {

    public static void showSoftInput(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public static void hideSoftInput(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void toggleSoftInput(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, 0);
        }
    }
}
