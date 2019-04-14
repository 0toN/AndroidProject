package com.xwm.androidproject.callback;

import java.util.List;

/**
 * @author xwm
 * @since 2019/3/22
 */
public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
