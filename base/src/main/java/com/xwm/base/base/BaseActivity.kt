package com.xwm.base.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import org.greenrobot.eventbus.EventBus

/**
 * @author Created by Adam on 2018-10-26
 */
@SuppressLint("Registered")
open class BaseActivity : RxAppCompatActivity() {

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

    override fun onDestroy() {
        if (regEvent()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }
}
