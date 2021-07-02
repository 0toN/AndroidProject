package com.xwm.base.base

import com.xwm.base.data.net.Network
import com.xwm.base.ext.getClazz

/**
 * Created by xwm on 2021/7/2
 */
open class BaseRepository<D : BaseDao> {

    protected val mDao: D by lazy {
        (getClazz<D>(this))
            .getDeclaredConstructor()
            .newInstance()
    }

    protected val mNetwork by lazy {
        Network()
    }
}