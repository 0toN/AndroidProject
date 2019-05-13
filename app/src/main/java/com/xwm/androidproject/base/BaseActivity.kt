package com.xwm.androidproject.base

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.xwm.androidproject.callback.PermissionListener
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * @author Created by Adam on 2018-10-26
 */
@SuppressLint("Registered")
open class BaseActivity : RxAppCompatActivity() {
    private lateinit var mListener: PermissionListener

    private var mStartActivityTag: String? = null
    private var mStartActivityTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (regEvent() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * 需要注册EventBus，则重写该方法 并返回 true
     */
    protected fun regEvent(): Boolean {
        return false
    }

    /**
     * 防 Activity 多重跳转
     */
    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        if (startActivitySelfCheck(intent)) {
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult(intent, requestCode, options)
        }
    }

    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     *
     * @param intent 用于跳转的 Intent 对象
     * @return 检查通过返回true, 检查不通过返回false
     */
    protected fun startActivitySelfCheck(intent: Intent): Boolean {
        // 默认检查通过
        var result = true
        // 标记对象
        val tag: String
        if (intent.component != null) {
            // 显式跳转
            tag = intent.component.className
        } else if (intent.action != null) {
            // 隐式跳转
            tag = intent.action
        } else {
            // 其他方式
            return true
        }

        if (tag == mStartActivityTag && mStartActivityTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false
        }

        mStartActivityTag = tag
        mStartActivityTime = SystemClock.uptimeMillis()
        return result
    }

    /**
     * 检查和处理运行时权限，并将用户授权的结果通过PermissionListener进行回调。
     *
     * @param permissions 要检查和处理的运行时权限数组
     * @param listener    用于接收授权结果的监听器
     */
    protected fun handlePermissions(permissions: List<String>?, listener: PermissionListener) {
        if (permissions == null) {
            return
        }
        mListener = listener
        val requestPermissionList = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission)
            }
        }
        if (!requestPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, requestPermissionList.toTypedArray(), 1)
        } else {
            listener.onGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0) {
                val deniedPermissions = ArrayList<String>()
                for (i in grantResults.indices) {
                    val grantResult = grantResults[i]
                    val permission = permissions[i]
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permission)
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    mListener.onGranted()
                } else {
                    mListener.onDenied(deniedPermissions)
                }
            }
        }
    }

    override fun onDestroy() {
        if (regEvent()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }
}
