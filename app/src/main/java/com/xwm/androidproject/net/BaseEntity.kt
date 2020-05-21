package com.xwm.androidproject.net

import com.google.gson.annotations.SerializedName
import com.xwm.androidproject.constants.Constants

/**
 * @author Created by xwm
 */
open class BaseEntity {
    @SerializedName("code")
    var code: Int = 0

    @SerializedName("msg")
    var msg: String? = null

    val isSuccess = code == Constants.HTTP_CODE_REQUEST_SUCCESS
}
