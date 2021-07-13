package com.xwm.base.data.net

import com.xwm.base.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * Created by xwm on 2019-05-12
 */
class Network {

    suspend fun getName() = execute { api.getName() }

    private suspend fun <T> execute(
        block: suspend CoroutineScope.() -> BaseResponse<T>
    ): Result<T> {
        return coroutineScope {
            val result: Result<T>
            withContext(Dispatchers.IO) {
                val response = block()
                if (response.code == Constants.HTTP_CODE_REQUEST_SUCCESS) {
                    result = Result.Success(response.data)
                } else {
                    result = Result.Failure(response.msg)
                }
            }
            return@coroutineScope result
        }
    }
}