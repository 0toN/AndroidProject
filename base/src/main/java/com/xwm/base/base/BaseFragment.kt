package com.xwm.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import org.greenrobot.eventbus.EventBus

/**
 * @author Created by Adam on 2019-02-15
 */
abstract class BaseFragment : RxFragment() {
    protected var mActivity: RxAppCompatActivity? = null
    // 根布局
    protected var mRootView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as RxAppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (regEvent() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null && setLayoutId() > 0) {
            mRootView = inflater.inflate(setLayoutId(), container, false)
        }
        return mRootView
    }

    //引入布局
    protected abstract fun setLayoutId(): Int

    /**
     * 需要注册EventBus，则重写该方法 并返回 true
     */
    protected fun regEvent(): Boolean {
        return false
    }

    override fun onDetach() {
        super.onDetach()
        // 解决java.lang.IllegalStateException: Activity has been destroyed 的错误
        try {
            val childFragmentManager = Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFragmentManager.isAccessible = true
            childFragmentManager.set(this, null)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

    }

    override fun onDestroy() {
        if (regEvent()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }
}
