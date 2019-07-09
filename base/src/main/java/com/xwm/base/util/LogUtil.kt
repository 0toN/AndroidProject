package com.xwm.base.util

import android.util.Log
import com.xwm.base.BuildConfig

/**
 * @author Created by Adam on 2018-12-20
 */

object LogUtil {

    fun v(content: String) {
        log(Log.VERBOSE, content)
    }

    fun w(content: String) {
        log(Log.WARN, content)
    }

    fun i(content: String) {
        log(Log.INFO, content)
    }

    fun d(content: String) {
        log(Log.DEBUG, content)
    }

    fun e(content: String) {
        log(Log.ERROR, content)
    }

    fun v(tag: String, content: String) {
        log(Log.VERBOSE, tag, content)
    }

    fun w(tag: String, content: String) {
        log(Log.WARN, tag, content)
    }

    fun i(tag: String, content: String) {
        log(Log.INFO, tag, content)
    }

    fun d(tag: String, content: String) {
        log(Log.DEBUG, tag, content)
    }

    fun e(tag: String, content: String) {
        log(Log.ERROR, tag, content)
    }

    fun e(e: Exception) {
        e(Log.getStackTraceString(e))
    }

    fun e(tr: Throwable) {
        e(Log.getStackTraceString(tr))
    }

    private fun log(priority: Int, tag: String, log: String) {
        if (!BuildConfig.DEBUG) {
            return
        }
        Log.println(priority, tag, log)
    }

    private fun log(priority: Int, content: String) {
        if (!BuildConfig.DEBUG) {
            return
        }
        Log.println(priority, tag, content)
    }

    private val tag: String
        get() {
            val classCanonicalName = LogUtil::class.java.canonicalName
            val thredCanonicalName = Thread::class.java.canonicalName
            val elements = Thread.currentThread().stackTrace
            var stackTraceElement = elements[0]
            var traceClassName: String
            for (element in elements) {
                traceClassName = element.className
                assert(classCanonicalName != null)
                assert(thredCanonicalName != null)
                if (!(traceClassName.startsWith(classCanonicalName!!)
                                || traceClassName.startsWith(thredCanonicalName!!)
                                || traceClassName.startsWith("dalvik.system.VMStack"))) {
                    stackTraceElement = element
                    break
                }
            }
            var invokedClass = stackTraceElement.className
            invokedClass = invokedClass.substring(invokedClass.lastIndexOf(".") + 1)
            val lineNumber = stackTraceElement.lineNumber
            return "$invokedClass-$lineNumber"
        }
}
