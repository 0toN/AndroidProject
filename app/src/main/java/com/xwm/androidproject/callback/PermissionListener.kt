package com.xwm.androidproject.callback

/**
 * @author xwm
 * @since 2019/3/22
 */
interface PermissionListener {
    fun onGranted()

    fun onDenied(deniedPermissions: List<String>)
}
