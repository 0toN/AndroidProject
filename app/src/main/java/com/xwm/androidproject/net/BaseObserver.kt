package com.xwm.androidproject.net

import com.xwm.base.util.LogUtil
import com.xwm.base.util.ToastUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author Created by Adam on 2018-08-31
 */
abstract class BaseObserver<T : BaseEntity> : Observer<T> {

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(response: T) {
        if (response.isSuccess) {
            onSuccess(response)
            return
        }
        val errorCode = response.code
        when (errorCode) {
            //handle errorCode
        }
    }

    override fun onError(e: Throwable) {
        LogUtil.e(TAG, "onError: " + e.toString())
    }

    override fun onComplete() {
        LogUtil.d(TAG, "onComplete")
    }

    protected abstract fun onSuccess(response: T)

    protected fun onFailure(response: T, msg: String) {
        ToastUtil.showLong(msg)
    }

    protected fun onFailure(errorCode: Int, msg: String) {
        ToastUtil.showLong(msg)
    }

    companion object {
        private const val TAG = "BaseObserver"
    }
}
