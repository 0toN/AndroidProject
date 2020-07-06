package com.xwm.androidproject.data.net

import com.xwm.androidproject.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * Created by xwm on 2019-05-12
 */
class Network private constructor() {

    private val api by lazy {
        RetrofitFactory.instance.create(IRetrofitService::class.java)
    }

    suspend fun getName() = execute { api.getName() }

    private suspend fun <T> execute(block: suspend CoroutineScope.() -> BaseResponse<T>
    ): Result<T> {
        return coroutineScope {
            val result: Result<T>
            val response = block()
            if (response.code == Constants.HTTP_CODE_REQUEST_SUCCESS) {
                result = Result.Success(response.data)
            } else {
                result = Result.Failure(response.msg)
            }
            return@coroutineScope result
        }
    }

    companion object {
        private const val TAG = "Repository"

        private lateinit var instance: Network

        fun getInstance(): Network {
            if (!::instance.isInitialized) {
                synchronized(Network::class.java) {
                    if (!::instance.isInitialized) {
                        instance = Network()
                    }
                }
            }
            return instance
        }
    }
}