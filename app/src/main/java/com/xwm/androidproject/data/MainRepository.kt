package com.xwm.androidproject.data

import com.xwm.base.base.BaseRepository
import com.xwm.base.data.net.Result
import com.xwm.base.util.ToastUtil

/**
 * Created by xwm on 2020/6/29
 */
class MainRepository : BaseRepository<MainDao>() {

    suspend fun getNickname(): String? {
        var name = mDao.getCachedName()
        if (name == null) {
            val result = mNetwork.getName()
            when (result) {
                is Result.Success -> {
                    name = result.data.name
                    mDao.cacheName(name)
                }
                is Result.Failure -> {
                    ToastUtil.showShort(result.errorMsg)
                }
            }
        }
        return name
    }
}