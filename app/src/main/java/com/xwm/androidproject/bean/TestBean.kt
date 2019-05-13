package com.xwm.androidproject.bean

import com.xwm.androidproject.net.BaseEntity

/**
 * Created by xwm on 2019-05-12
 */
data class TestBean(val data: ResultBean) : BaseEntity()

data class ResultBean(val id: Int, val name: String)