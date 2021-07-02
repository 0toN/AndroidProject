package com.xwm.base.data.net

/**
 * @author Created by xwm
 */
data class BaseResponse<out T>(
        val code: Int,
        var msg: String,
        val data: T
)

