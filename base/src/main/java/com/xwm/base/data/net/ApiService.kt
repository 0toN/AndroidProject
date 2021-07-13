package com.xwm.base.data.net

import com.xwm.base.data.bean.NameBean
import retrofit2.http.GET

/**
 * Created by xwm on 2019-05-10
 */
interface ApiService {

    @GET(API.GET_NAME)
    suspend fun getName(): BaseResponse<NameBean>
}