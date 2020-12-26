package com.xwm.base.base

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwm.base.R
import com.xwm.base.util.LogUtil


/**
 * Created by xwm on 2019-07-01
 */
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var mBehavior: BottomSheetBehavior<View>? = null
    var mBottomSheetBehaviorState: Int = BottomSheetBehavior.STATE_COLLAPSED
    protected lateinit var mRootView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        initBottomSheetBehavior()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(setLayoutId(), container, false)
        return mRootView
    }

    fun setPeekHeight(peekHeightPx: Int) {
        if (peekHeightPx <= 0) {
            return
        }
        mBehavior?.peekHeight = peekHeightPx
    }

    fun setMaxHeight(maxHeightPx: Int) {
        if (maxHeightPx <= 0) {
            return
        }
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, maxHeightPx)
        dialog?.window?.setGravity(Gravity.BOTTOM)
    }

    private fun initBottomSheetBehavior() {
        if (mBehavior != null) {
            return
        }
        //更改默认背景颜色为透明，解决设置白色圆角不生效的问题
        val parent = view?.parent as View
        parent.setBackgroundColor(resources.getColor(android.R.color.transparent))

        val dialog = dialog as BottomSheetDialog
        val bottomSheet = dialog.delegate.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        bottomSheet?.let {
            mBehavior = BottomSheetBehavior.from(it)
            mBehavior?.addBottomSheetCallback(mBottomSheetCallback)
        }
    }

    protected val mBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(@NonNull bottomSheet: View, @BottomSheetBehavior.State newState: Int) {
            mBottomSheetBehaviorState = newState
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAllowingStateLoss()
            }
        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
        }
    }

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

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (!isRepeatedShow(tag)) {
                super.show(manager, tag)
            }
        } catch (exception: IllegalStateException) {
            LogUtil.e(exception)
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return if (!isRepeatedShow(tag)) {
            super.show(transaction, tag)
        } else -1
    }

    companion object {
        private var sShowTag: String? = null
        private var sLastTime: Long = 0
    }

    protected abstract fun setLayoutId(): Int
}