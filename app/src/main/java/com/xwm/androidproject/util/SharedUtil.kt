package com.xwm.androidproject.util

import android.preference.PreferenceManager
import com.xwm.androidproject.App

/**
 * SharedPreferences工具类，提供简单的封装接口，简化SharedPreferences的用法。
 *
 * @author xwm
 * @since 2019/3/16
 */
object SharedUtil {

    /**
     * 存储boolean类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    fun save(key: String, value: Boolean) {
        val editor = PreferenceManager.getDefaultSharedPreferences(
                App.context).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * 存储float类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    fun save(key: String, value: Float) {
        val editor = PreferenceManager.getDefaultSharedPreferences(
                App.context).edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    /**
     * 存储int类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    fun save(key: String, value: Int) {
        val editor = PreferenceManager.getDefaultSharedPreferences(
                App.context).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * 存储long类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    fun save(key: String, value: Long) {
        val editor = PreferenceManager.getDefaultSharedPreferences(
                App.context).edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * 存储String类型的键值对到SharedPreferences文件当中。
     *
     * @param key   存储的键
     * @param value 存储的值
     */
    fun save(key: String, value: String) {
        val editor = PreferenceManager.getDefaultSharedPreferences(
                App.context).edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的boolean类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return boolean类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: Boolean): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(App.context)
        return prefs.getBoolean(key, defValue)
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的float类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return float类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: Float): Float {
        val prefs = PreferenceManager.getDefaultSharedPreferences(App.context)
        return prefs.getFloat(key, defValue)
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的int类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return int类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: Int): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(App.context)
        return prefs.getInt(key, defValue)
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的long类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return long类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: Long): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(App.context)
        return prefs.getLong(key, defValue)
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的String类型的值。
     *
     * @param key      读取的键
     * @param defValue 如果读取不到值，返回的默认值
     * @return String类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: String): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(App.context)
        return prefs.getString(key, defValue)
    }

    /**
     * 判断SharedPreferences文件当中是否包含指定的键值。
     *
     * @param key 判断键是否存在
     * @return 键已存在返回true，否则返回false。
     */
    operator fun contains(key: String): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(App.context)
        return prefs.contains(key)
    }

    /**
     * 清理SharedPreferences文件当中传入键所对应的值。
     *
     * @param key 想要清除的键
     */
    fun clear(key: String) {
        val editor = PreferenceManager.getDefaultSharedPreferences(
                App.context).edit()
        editor.remove(key)
        editor.apply()
    }

    /**
     * 将SharedPreferences文件中存储的所有值清除。
     */
    fun clearAll() {
        val editor = PreferenceManager.getDefaultSharedPreferences(
                App.context).edit()
        editor.clear()
        editor.apply()
    }

}
