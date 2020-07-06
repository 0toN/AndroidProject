package com.xwm.androidproject.data

import com.xwm.androidproject.data.db.MainDao
import com.xwm.androidproject.data.net.Network
import com.xwm.androidproject.data.net.Result
import com.xwm.base.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by xwm on 2020/6/29
 */
class MainRepository private constructor(private val mainDao: MainDao, private val network: Network) {

    suspend fun getName(): String? {
        var name = mainDao.getCachedName()
        if (name == null) {
            withContext(Dispatchers.IO) {
                val result = network.getName()
                when (result) {
                    is Result.Success -> {
                        name = result.data.name
                        mainDao.cacheName(name)
                    }
                    is Result.Failure -> {
                        ToastUtil.showShort(result.errorMsg)
                    }
                }
            }
        }
        return name
    }

    companion object {

        private lateinit var instance: MainRepository

        fun getInstance(weatherDao: MainDao, network: Network): MainRepository {
            if (!::instance.isInitialized) {
                synchronized(MainRepository::class.java) {
                    if (!::instance.isInitialized) {
                        instance = MainRepository(weatherDao, network)
                    }
                }
            }
            return instance
        }
    }
}