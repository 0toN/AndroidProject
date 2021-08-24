package com.xwm.base.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xwm.base.ext.getClazz
import com.xwm.base.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by xwm on 2020/7/6
 */
open class BaseViewModel<R : BaseRepository<*>> : ViewModel() {

    protected val mRepository: R by lazy {
        (getClazz<R>(this))
            .getDeclaredConstructor()
            .newInstance()
    }

    fun <T> launch(
        block: suspend () -> T,
        onSuccess: suspend (T) -> Unit = {},
        onError: suspend (Throwable) -> Unit = {}
    ) =
        viewModelScope.launch {
            kotlin.runCatching {
                withContext((Dispatchers.IO)) {
                    block()
                }
            }.onSuccess {
                onSuccess(it)
            }.onFailure {
                LogUtil.e(it)
                onError(it)
            }
        }
}