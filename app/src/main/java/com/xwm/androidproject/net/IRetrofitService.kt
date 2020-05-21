package com.xwm.androidproject.net

import com.xwm.androidproject.bean.TestBean
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by xwm on 2019-05-10
 */
interface IRetrofitService {

    @GET(API.TEST)
    fun test(): Call<TestBean>
}