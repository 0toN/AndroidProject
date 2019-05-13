package com.xwm.androidproject.net

import android.util.ArrayMap
import com.xwm.androidproject.bean.TestBean
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by xwm on 2019-05-10
 */
interface API {
    @POST("/test")
    @FormUrlEncoded
    fun test(@FieldMap map: ArrayMap<String, Any>): Observable<TestBean>
}