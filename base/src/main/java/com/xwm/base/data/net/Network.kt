package com.xwm.base.data.net

import com.xwm.base.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * Created by xwm on 2019-05-12
 */
class Network {

    private val api by lazy {
        RetrofitFactory.instance.create(IRetrofitService::class.java)
    }

    suspend fun getName() = execute { api.getName() }

    private suspend fun <T> execute(
        block: suspend CoroutineScope.() -> BaseResponse<T>
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

        private lateinit var instance: Network

        fun getInstance(): Network {
            if (!Companion::instance.isInitialized) {
                synchronized(Network::class.java) {
                    if (!Companion::instance.isInitialized) {
                        instance = Network()
                    }
                }
            }
            return instance
        }
    }
}