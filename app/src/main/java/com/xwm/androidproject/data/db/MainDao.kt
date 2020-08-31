package com.xwm.androidproject.data.db

import com.google.gson.Gson
import com.tencent.mmkv.MMKV

/**
 * Created by xwm on 2020/6/29
 */
class MainDao {

    fun getCachedName(): String? {
        val kv = MMKV.defaultMMKV()
        val name = kv.decodeString("name")
        return if (name != null) {
            Gson().fromJson(name, String::class.java)
        } else {
            null
        }
    }

    fun cacheName(name: String?) {
        if (name == null) return
        val kv = MMKV.defaultMMKV()
        kv.encode("name", Gson().toJson(name))
    }
}