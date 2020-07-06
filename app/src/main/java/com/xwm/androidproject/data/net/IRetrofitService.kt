package com.xwm.androidproject.data.net

import com.xwm.androidproject.data.bean.NameBean
import retrofit2.http.GET

/**
 * Created by xwm on 2019-05-10
 */
interface IRetrofitService {

    @GET(API.GET_NAME)
    suspend fun getName(): BaseResponse<NameBean>
}