package com.xwm.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xwm.base.ext.getClazz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus

/**
 * @author Created by Adam on 2019-02-15
 */
abstract class BaseFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : Fragment(),
    CoroutineScope by MainScope() {

    protected lateinit var mViewModel: VM
    protected lateinit var mDataBinding: DB

    /**
     * 初始化变量
     */
    protected open fun initVar() {}

    /**
     * 初始化控件
     */
    protected abstract fun initView()

    /**
     * 加载数据
     */
    protected open fun loadData() {}

    /**
     * 引入布局
     */
    protected abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mDataBinding.lifecycleOwner = this
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(getClazz(this))

        initVar()
        initView()
        loadData()

        if (regEvent() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * 需要注册EventBus，则重写该方法 并返回 true
     */
    open fun regEvent(): Boolean {
        return false
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        cancel()
        super.onDestroy()
    }
}
