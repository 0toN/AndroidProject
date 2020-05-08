package com.xwm.base.base

/**
 * @author Created by Adam on 2019-02-20
 */
abstract class BaseLazyFragment : BaseFragment() {

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
