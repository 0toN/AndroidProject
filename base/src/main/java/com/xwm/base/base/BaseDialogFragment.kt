package com.xwm.base.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.xwm.base.R
import com.xwm.base.util.LogUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author Created by Adam on 2018-08-21
 */
abstract class BaseDialogFragment : AppCompatDialogFragment() {

    private var mOnDismissListener: DialogInterface.OnDismissListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (regEvent() &&
            !EventBus.getDefault().isRegistered(this) &&
            EventBus.getDefault().hasSubscriberForEvent(javaClass)
        ) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setWindowAnimations(getWindowAnimations())
            // 宽度全屏
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            attributes.gravity = getGravity()
        }
    }

    override fun onResume() {
        super.onResume()
        if (showKeyboard()) {
            view?.postDelayed({
                val inManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            }, 500)
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    protected abstract fun getLayoutId(): Int

    /**
     * 根据 tag 判断Dialog是否重复显示
     *
     * @param tag
     */
    protected fun isRepeatedShow(tag: String?): Boolean {
        val result = tag == sShowTag && SystemClock.uptimeMillis() - sLastTime < 500
        sShowTag = tag
        sLastTime = SystemClock.uptimeMillis()
        return result
    }

    /**
     * bugfix：Activity 调用 onSaveInstanceState() 之后再调用 show() 或 dismiss() 方法，
     * 报 Can not perform this action after onSaveInstanceState异常
     */
    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (!isRepeatedShow(tag)) {
                super.show(manager, tag)
            }
        } catch (exception: IllegalStateException) {
            LogUtil.e(exception)
            manager.beginTransaction()
                .add(this, tag)
                .commitAllowingStateLoss()
        }

    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return if (!isRepeatedShow(tag)) {
            super.show(transaction, tag)
        } else -1
    }

    open fun getWindowAnimations(): Int {
        return R.style.CustomDialogFragmentAnimation
    }

    open fun getGravity(): Int {
        return Gravity.NO_GRAVITY
    }

    open fun showKeyboard(): Boolean {
        return false
    }

    /**
     * 需要注册EventBus，则重写该方法 并返回 true
     */
    protected fun regEvent(): Boolean {
        return false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mOnDismissListener?.onDismiss(dialog)
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener) {
        this.mOnDismissListener = listener
    }

    companion object {
        private var sShowTag: String? = null
        private var sLastTime: Long = 0
    }
}
