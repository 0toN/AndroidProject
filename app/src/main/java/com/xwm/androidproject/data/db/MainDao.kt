package com.xwm.androidproject.data.db

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.xwm.base.util.Utils

/**
 * Created by xwm on 2020/6/29
 */
class MainDao {

    fun getCachedName(): String? {
        val name = PreferenceManager.getDefaultSharedPreferences(Utils.app).getString("name", null)
        if (name != null) {
            return Gson().fromJson(name, String::class.java)
        }
        return null
    }

    fun cacheName(name: String?) {
        if (name == null) return
        PreferenceManager.getDefaultSharedPreferences(Utils.app).edit {
            putString("name", Gson().toJson(name))
        }
    }

    private fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        action(editor)
        editor.apply()
    }
}