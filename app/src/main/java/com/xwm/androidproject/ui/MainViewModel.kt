package com.xwm.androidproject.ui

import androidx.lifecycle.MutableLiveData
import com.xwm.androidproject.data.MainRepository
import com.xwm.base.base.BaseViewModel

/**
 * Created by xwm on 2020/6/28
 */
class MainViewModel(private val repository: MainRepository) : BaseViewModel() {

    var name = MutableLiveData<String>()

    init {
        name.value = "获取名字"
    }

    fun getName() {
        launch({
            name.value = repository.getName()
        }, {})
    }
}