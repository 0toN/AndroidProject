package com.xwm.base.base

import android.os.Bundle

/**
 * @author Created by Adam on 2019-02-20
 */
abstract class BaseLazyFragment : BaseFragment() {
    // 是否进行过懒加载
    protected var isLazyLoad: Boolean = false
    // Fragment 是否可见
    private var isFragmentVisible: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isFragmentVisible) {
            initLazyLoad()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isFragmentVisible = isVisibleToUser
        if (isVisibleToUser && view != null) {
            if (!isLazyLoad) {
                initLazyLoad()
            } else {
                // 从不可见到可见
                onRestart()
            }
        }
    }

    /**
     * 初始化懒加载
     */
    protected fun initLazyLoad() {
        if (!isLazyLoad) {
            isLazyLoad = true
            initFragment()
        }
    }

    //重置 LazyLoad 的状态
    protected fun resetLazyLoadStatus() {
        this.isLazyLoad = false
    }

    /**
     * 跟 Activity 的同名方法效果一样
     */
    protected fun onRestart() {
        // 从可见的状态变成不可见状态，再从不可见状态变成可见状态时回调
    }

    protected fun initFragment() {
        initVar()
        initView()
        loadData()
    }

    //初始化变量
    protected abstract fun initVar()

    //初始化控件
    protected abstract fun initView()

    //初始化数据
    protected abstract fun loadData()
}
