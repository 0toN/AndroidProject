package com.xwm.androidproject.data.db

/**
 * Created by xwm on 2020/6/29
 */
object MainDatabase {

    private lateinit var mainDao: MainDao

    fun getMainDao(): MainDao {
        if (!::mainDao.isInitialized) {
            mainDao = MainDao()
        }
        return mainDao
    }
}