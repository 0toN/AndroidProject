package com.xwm.base.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xwm.base.ext.getClazz
import com.xwm.base.util.LogUtil
import kotlinx.coroutines.launch

/**
 * Created by xwm on 2020/7/6
 */
open class BaseViewModel<R : BaseRepository<*>> : ViewModel() {

    protected val mRepository: R by lazy {
        (getClazz<R>(this))
            .getDeclaredConstructor()
            .newInstance()
    }

    fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) =
        viewModelScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                LogUtil.e(e)
                error(e)
            }
        }
}