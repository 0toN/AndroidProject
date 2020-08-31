package com.xwm.base.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwm.base.R


/**
 * Created by xwm on 2019-07-01
 */
open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var mBehavior: BottomSheetBehavior<*>? = null
    var mBottomSheetBehaviorState: Int = BottomSheetBehavior.STATE_COLLAPSED

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(context!!, R.style.BottomSheetDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        setBottomSheetBehavior()
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

    private fun setBottomSheetBehavior() {
        if (mBehavior != null) {
            return
        }
        val parent = view?.parent as View
        //更改默认背景颜色为透明，解决设置白色圆角不生效的问题
        parent.setBackgroundColor(resources.getColor(android.R.color.transparent))
        val layoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
        mBehavior = layoutParams.behavior as BottomSheetBehavior
        mBehavior?.addBottomSheetCallback(mBottomSheetCallback)
    }

    private val mBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(@NonNull bottomSheet: View, @BottomSheetBehavior.State newState: Int) {
            mBottomSheetBehaviorState = newState
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAllowingStateLoss()
            }
        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
        }
    }
}