package com.xwm.androidproject.net

import com.xwm.base.util.LogUtil
import com.xwm.base.util.ToastUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by xwm on 2019-05-12
 */
class Network private constructor() {

    private val api by lazy {
        RetrofitFactory.instance.create(IRetrofitService::class.java)
    }

    suspend fun test() = api.test().await()

    private suspend fun <T : BaseEntity> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body == null) {
                        continuation.resumeWithException(RuntimeException("response body is null: $response"))
                        return
                    }
                    if (body.isSuccess) {
                        continuation.resume(body)
                    } else {
                        val errorCode = body.code
                        when (errorCode) {
//                            handle errorCode
                            404 -> {
                                ToastUtil.showShort("请求地址不存在")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    LogUtil.e(TAG, "onFailure: $t")
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    companion object {
        private const val TAG = "Network"

        val instance: Network = Network()
    }
}