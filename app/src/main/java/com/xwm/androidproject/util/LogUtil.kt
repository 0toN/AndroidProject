package com.xwm.androidproject.util

import android.util.Log

/**
 * @author Created by Adam on 2018-12-20
 */
class LogUtil private constructor() {

    init {
        throw IllegalStateException("you can't instantiate me!")
    }

    companion object {
        var isDebug = AppUtil.isApkInDebug

        fun v(content: String) {
            if (!isDebug) {
                return
            }
            log(Log.VERBOSE, content)
        }

        fun w(content: String) {
            if (!isDebug) {
                return
            }
            log(Log.WARN, content)
        }

        fun i(content: String) {
            if (!isDebug) {
                return
            }
            log(Log.INFO, content)
        }

        fun d(content: String) {
            if (!isDebug) {
                return
            }
            log(Log.DEBUG, content)
        }

        fun d(tag: String, content: String) {
            if (!isDebug) {
                return
            }
            log(Log.DEBUG, tag, content)
        }

        fun e(content: String) {
            if (!isDebug) {
                return
            }
            log(Log.ERROR, content)
        }

        fun e(tag: String, content: String) {
            if (!isDebug) {
                return
            }
            log(Log.ERROR, tag, content)
        }

        fun e(e: Exception) {
            e(Log.getStackTraceString(e))
        }

        fun e(tr: Throwable) {
            e(Log.getStackTraceString(tr))
        }

        private fun log(priority: Int, tag: String, log: String) {
            if (!isDebug) {
                return
            }
            Log.println(priority, tag, log)
        }

        private fun log(priority: Int, content: String) {
            if (!isDebug) {
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
}
