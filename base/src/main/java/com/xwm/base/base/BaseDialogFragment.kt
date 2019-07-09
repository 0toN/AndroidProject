package com.xwm.base.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.Window
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.trello.rxlifecycle3.components.support.RxAppCompatDialogFragment
import com.xwm.base.R
import com.xwm.base.util.LogUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author Created by Adam on 2018-08-21
 */
class BaseDialogFragment : RxAppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (regEvent() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = dialog.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setWindowAnimations(R.style.DialogFragmentAnimation)
        }
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    /**
     * 根据 tag 判断Dialog是否重复显示
     *
     * @param tag
     */
    protected fun isRepeatedShow(tag: String): Boolean {
        val result = tag == sShowTag && SystemClock.uptimeMillis() - sLastTime < 500
        sShowTag = tag
        sLastTime = SystemClock.uptimeMillis()
        return result
    }

    /**
     * bugfix：Activity 调用 onSaveInstanceState() 之后再调用 show() 或 dismiss() 方法，
     * 报 Can not perform this action after onSaveInstanceState异常
     */
    override fun show(manager: FragmentManager, tag: String) {
        try {
            if (!isRepeatedShow(tag)) {
                super.show(manager, tag)
            }
        } catch (exception: IllegalStateException) {
            LogUtil.e(exception)
        }

    }

    override fun show(transaction: FragmentTransaction, tag: String): Int {
        return if (!isRepeatedShow(tag)) {
            super.show(transaction, tag)
        } else -1
    }

    /**
     * 需要注册EventBus，则重写该方法 并返回 true
     */
    protected fun regEvent(): Boolean {
        return false
    }

    override fun onDestroy() {
        if (regEvent()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    companion object {
        private var sShowTag: String? = null
        private var sLastTime: Long = 0
    }
}
