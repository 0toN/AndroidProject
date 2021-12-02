package com.xwm.base.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.xwm.base.ext.getClazz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus

/**
 * @author Created by Adam on 2018-10-26
 */
@SuppressLint("Registered")
abstract class BaseActivity<VM : BaseViewModel<*>, DB : ViewDataBinding> : AppCompatActivity(),
    CoroutineScope by MainScope() {

    protected lateinit var mViewModel: VM
    protected lateinit var mDataBinding: DB

    private var mStartActivityTag: String? = null
    private var mStartActivityTime: Long = 0

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
     * 初始化LiveData数据观察者
     */
    protected abstract fun initDataObserver()

    protected abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mDataBinding.lifecycleOwner = this
        mViewModel = ViewModelProvider(this).get(getClazz(this))

        initVar()
        initView()
        initDataObserver()
        loadData()

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
        when {
            intent.component != null -> {
                // 显式跳转
                tag = intent.component!!.className
            }
            intent.action != null -> {
                // 隐式跳转
                tag = intent.action!!
            }
            else -> {
                // 其他方式
                return true
            }
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        //取消当前Activity的协程
        cancel()
        super.onDestroy()
    }
}