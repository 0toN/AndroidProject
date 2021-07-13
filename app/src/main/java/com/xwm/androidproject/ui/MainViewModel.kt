package com.xwm.androidproject.ui

import androidx.lifecycle.MutableLiveData
import com.xwm.androidproject.data.MainRepository
import com.xwm.base.base.BaseViewModel

/**
 * Created by xwm on 2020/6/28
 */
class MainViewModel : BaseViewModel<MainRepository>() {

    var nicknameData = MutableLiveData<String>()

    init {
        nicknameData.value = "获取名字"
    }

    fun getNickname() {
        launch({
            nicknameData.value = mRepository.getNickname()
        })
    }
}