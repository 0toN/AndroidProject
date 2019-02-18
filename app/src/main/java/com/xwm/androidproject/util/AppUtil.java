package com.xwm.androidproject.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xwm.androidproject.MyApplication;

public class AppUtil {

    /**
     * Return whether the app is installed.
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(@NonNull Context context, @NonNull final String packageName) {
        PackageManager packageManager = context.getPackageManager();
        return !TextUtils.isEmpty(packageName) && packageManager.getLaunchIntentForPackage(packageName) != null;
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug() {
        Context context = MyApplication.getContext();
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

}
