package com.xwm.androidproject.util

import com.xwm.androidproject.data.MainRepository
import com.xwm.androidproject.data.db.MainDatabase
import com.xwm.androidproject.data.net.Network
import com.xwm.androidproject.ui.MainModelFactory

object InjectorUtil {

    private fun getMainRepository() = MainRepository.getInstance(MainDatabase.getMainDao(), Network.getInstance())

    fun getMainModelFactory() = MainModelFactory(getMainRepository())
}