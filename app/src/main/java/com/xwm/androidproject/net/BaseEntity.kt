package com.xwm.androidproject.net

import com.google.gson.annotations.SerializedName
import com.xwm.androidproject.constants.HttpConfig

/**
 * @author Created by Adam
 */
open class BaseEntity {
    @SerializedName("code")
    var code: Int = 0
    @SerializedName("msg")
    var msg: String? = null

    val isSuccess: Boolean
        get() = code == HttpConfig.HTTP_CODE_REQUEST_SUCCESS
}
