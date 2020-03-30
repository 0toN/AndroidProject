package com.xwm.androidproject.net

import android.util.ArrayMap
import com.xwm.androidproject.bean.TestBean
import io.reactivex.Observable

/**
 * Created by xwm on 2019-05-12
 */
interface IRetrofitService {

    companion object {
        private val api by lazy {
            RetrofitFactory.instance.create(API::class.java)
        }
    }

    fun test(): Observable<TestBean> {
        val map = ArrayMap<String, Any>()
        map["key"] = "value"
        return api.test(map)
    }
}