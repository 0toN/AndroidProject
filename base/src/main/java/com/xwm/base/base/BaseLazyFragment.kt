package com.xwm.base.base

import androidx.databinding.ViewDataBinding

/**
 * @author Created by xwm on 2019-02-20
 */
abstract class BaseLazyFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> :
    BaseFragment<VM, DB>() {

    // Fragment 是否可见
    private var isFragmentVisible: Boolean = false
    protected var isFirstLoad: Boolean = true

    override fun onPause() {
        super.onPause()
        isFragmentVisible = false
    }

    override fun onResume() {
        super.onResume()
        isFragmentVisible = true
        if (isFirstLoad) {
            isFirstLoad = false
            lazyLoadData()
        }
    }

    //懒加载数据
    abstract fun lazyLoadData()
}
