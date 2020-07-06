package com.xwm.androidproject.data.net

/**
 * Created by xwm on 2020/5/22
 */
sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val errorMsg: String) : Result<Nothing>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data = $data]"
            is Failure -> "Failure[errorMsg = $errorMsg]"
            is Error -> "Error[exception = $exception]"
        }
    }
}