package com.xwm.androidproject.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xwm.androidproject.App
import com.xwm.base.base.BaseViewModel


/**
 * 在Activity中得到Application上下文的ViewModel
 */
inline fun <reified VM : BaseViewModel<*>> AppCompatActivity.getAppViewModel(): VM {
    return (application as App).getAppViewModelProvider().get(VM::class.java)
}

/**
 * 在Fragment中得到Application上下文的ViewModel
 * 提示，在fragment中调用该方法时，请在该Fragment onCreate以后调用或者请用by lazy方式懒加载初始化调用，不然会提示requireActivity没有导致错误
 */
inline fun <reified VM : BaseViewModel<*>> Fragment.getAppViewModel(): VM {
    return (requireActivity().application as App).getAppViewModelProvider().get(VM::class.java)
}





