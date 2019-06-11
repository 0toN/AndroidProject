package com.xwm.base.util

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.concurrent.Executors

class Utils private constructor() {

    companion object {

        internal val activityLifecycle = ActivityLifecycleImpl()
        private val UTIL_POOL = Executors.newFixedThreadPool(3)
        private val UTIL_HANDLER = Handler(Looper.getMainLooper())

        private var sApplication: Application? = null

        /**
         * Init utils.
         *
         * Init it in the class of Application.
         *
         * @param app application
         */
        fun init(app: Application?) {
            if (sApplication == null) {
                if (app == null) {
                    sApplication = applicationByReflect
                } else {
                    sApplication = app
                }
                sApplication!!.registerActivityLifecycleCallbacks(activityLifecycle)
            } else {
                if (app != null && app.javaClass != sApplication!!.javaClass) {
                    sApplication!!.unregisterActivityLifecycleCallbacks(activityLifecycle)
                    activityLifecycle.mActivityList.clear()
                    sApplication = app
                    sApplication!!.registerActivityLifecycleCallbacks(activityLifecycle)
                }
            }
        }

        /**
         * Return the context of Application object.
         *
         * @return the context of Application object
         */
        val app: Application
            get() {
                if (sApplication != null) {
                    return sApplication as Application
                }
                val app = applicationByReflect
                init(app)
                return app
            }

        internal val activityList: LinkedList<Activity>
            get() = activityLifecycle.mActivityList

        val topActivityOrApp: Context
            get() {
                if (isAppForeground) {
                    val topActivity = activityLifecycle.topActivity
                    return topActivity ?: app
                } else {
                    return app
                }
            }

        val isAppForeground: Boolean
            get() {
                val am = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val info = am.runningAppProcesses
                if (info == null || info.size == 0) {
                    return false
                }
                for (aInfo in info) {
                    if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        if (aInfo.processName == app.packageName) {
                            return true
                        }
                    }
                }
                return false
            }

        internal fun <T> doAsync(task: Task<T>): Task<T> {
            UTIL_POOL.execute(task)
            return task
        }

        fun runOnUiThread(runnable: Runnable) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                runnable.run()
            } else {
                Utils.UTIL_HANDLER.post(runnable)
            }
        }

        fun runOnUiThreadDelayed(runnable: Runnable, delayMillis: Long) {
            Utils.UTIL_HANDLER.postDelayed(runnable, delayMillis)
        }

        val currentProcessName: String
            get() {
                var name = currentProcessNameByFile
                if (!TextUtils.isEmpty(name)) {
                    return name
                }
                name = currentProcessNameByAms
                if (!TextUtils.isEmpty(name)) {
                    return name
                }
                name = currentProcessNameByReflect
                return name
            }

        ///////////////////////////////////////////////////////////////////////////
        // private method
        ///////////////////////////////////////////////////////////////////////////

        private val currentProcessNameByFile: String
            get() {
                try {
                    val file = File("/proc/" + android.os.Process.myPid() + "/" + "cmdline")
                    val mBufferedReader = BufferedReader(FileReader(file))
                    val processName = mBufferedReader.readLine().trim()
                    mBufferedReader.close()
                    return processName
                } catch (e: Exception) {
                    e.printStackTrace()
                    return ""
                }

            }

        private val currentProcessNameByAms: String
            get() {
                val am = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val info = am.runningAppProcesses
                if (info == null || info.size == 0) {
                    return ""
                }
                val pid = android.os.Process.myPid()
                for (aInfo in info) {
                    if (aInfo.pid == pid) {
                        if (aInfo.processName != null) {
                            return aInfo.processName
                        }
                    }
                }
                return ""
            }

        private val currentProcessNameByReflect: String
            get() {
                var processName = ""
                try {
                    val app = app
                    val loadedApkField = app.javaClass.getField("mLoadedApk")
                    loadedApkField.isAccessible = true
                    val loadedApk = loadedApkField.get(app)

                    val activityThreadField = loadedApk.javaClass.getDeclaredField("mActivityThread")
                    activityThreadField.isAccessible = true
                    val activityThread = activityThreadField.get(loadedApk)

                    val getProcessName = activityThread.javaClass.getDeclaredMethod("getProcessName")
                    processName = getProcessName.invoke(activityThread) as String
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return processName
            }

        private val applicationByReflect: Application
            get() {
                try {
                    @SuppressLint("PrivateApi")
                    val activityThread = Class.forName("android.app.ActivityThread")
                    val thread = activityThread.getMethod("currentActivityThread").invoke(null)
                    val app = activityThread.getMethod("getApplication").invoke(thread)
                            ?: throw NullPointerException("u should init first")
                    return app as Application
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }

                throw NullPointerException("u should init first")
            }

        /**
         * Set animators enabled.
         */
        private fun setAnimatorsEnabled() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && ValueAnimator.areAnimatorsEnabled()) {
                return
            }
            try {

                val sDurationScaleField = ValueAnimator::class.java.getDeclaredField("sDurationScale")
                sDurationScaleField.isAccessible = true
                val sDurationScale = sDurationScaleField.get(null) as Float
                if (sDurationScale == 0f) {
                    sDurationScaleField.set(null, 1f)
                    LogUtil.i("setAnimatorsEnabled: Animators are enabled now!")
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    internal class ActivityLifecycleImpl : ActivityLifecycleCallbacks {

        val mActivityList = LinkedList<Activity>()
        val mStatusListenerMap: MutableMap<Any, OnAppStatusChangedListener> = HashMap()
        val mDestroyedListenerMap: MutableMap<Activity, MutableSet<OnActivityDestroyedListener>> = HashMap()

        private var mForegroundCount = 0
        private var mConfigCount = 0
        private var mIsBackground = false

        var topActivity: Activity?
            get() {
                if (!mActivityList.isEmpty()) {
                    for (i in mActivityList.indices.reversed()) {
                        val activity = mActivityList[i]
                        if (activity.isFinishing ||
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
                            continue
                        }
                        return activity
                    }
                }
                val topActivityByReflect = topActivityByReflect
                if (topActivityByReflect != null) {
                    topActivity = topActivityByReflect
                }
                return topActivityByReflect
            }
            private set(activity) {
                if (mActivityList.contains(activity)) {
                    if (mActivityList.last != activity) {
                        mActivityList.remove(activity)
                        mActivityList.addLast(activity)
                    }
                } else {
                    mActivityList.addLast(activity)
                }
            }

        private val topActivityByReflect: Activity?
            get() {
                try {
                    @SuppressLint("PrivateApi")
                    val activityThreadClass = Class.forName("android.app.ActivityThread")
                    val currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread").invoke(null)
                    val mActivityListField = activityThreadClass.getDeclaredField("mActivityList")
                    mActivityListField.isAccessible = true
                    val activities = mActivityListField.get(currentActivityThreadMethod) as Map<*, *>
                    for (activityRecord in activities.values) {
                        val activityRecordClass = activityRecord!!.javaClass
                        val pausedField = activityRecordClass.getDeclaredField("paused")
                        pausedField.isAccessible = true
                        if (!pausedField.getBoolean(activityRecord)) {
                            val activityField = activityRecordClass.getDeclaredField("activity")
                            activityField.isAccessible = true
                            return activityField.get(activityRecord) as Activity
                        }
                    }
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                }
                return null
            }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            setAnimatorsEnabled()
            topActivity = activity
        }

        override fun onActivityStarted(activity: Activity) {
            if (!mIsBackground) {
                topActivity = activity
            }
            if (mConfigCount < 0) {
                ++mConfigCount
                updateAppConfig(activity)
            } else {
                ++mForegroundCount
            }
        }

        override fun onActivityResumed(activity: Activity) {
            topActivity = activity
            if (mIsBackground) {
                mIsBackground = false
                postStatus(true)
            }
        }

        override fun onActivityPaused(activity: Activity) {/**/
        }

        override fun onActivityStopped(activity: Activity) {
            if (activity.isChangingConfigurations) {
                --mConfigCount
            } else {
                --mForegroundCount
                if (mForegroundCount <= 0) {
                    mIsBackground = true
                    postStatus(false)
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {/**/
        }

        override fun onActivityDestroyed(activity: Activity) {
            mActivityList.remove(activity)
            consumeOnActivityDestroyedListener(activity)
            fixSoftInputLeaks(activity)
        }

        fun addOnAppStatusChangedListener(obj: Any, listener: OnAppStatusChangedListener) {
            mStatusListenerMap[obj] = listener
        }

        fun removeOnAppStatusChangedListener(obj: Any) {
            mStatusListenerMap.remove(obj)
        }

        fun removeOnActivityDestroyedListener(activity: Activity?) {
            mDestroyedListenerMap.remove(activity)
        }

        fun addOnActivityDestroyedListener(activity: Activity?, listener: OnActivityDestroyedListener?) {
            if (activity == null || listener == null) {
                return
            }
            val listeners: MutableSet<OnActivityDestroyedListener>?
            if (!mDestroyedListenerMap.containsKey(activity)) {
                listeners = HashSet()
                mDestroyedListenerMap[activity] = listeners
            } else {
                listeners = mDestroyedListenerMap[activity]
                if (listeners != null && listeners.contains(listener)) {
                    return
                }
            }
            listeners?.add(listener)
        }

        private fun updateAppConfig(activity: Activity) {
            val resources = app.resources
            val dm = resources.displayMetrics
            val config = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.locales = activity.resources.configuration.locales
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(activity.resources.configuration.locale)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                app.createConfigurationContext(config)
            } else {
                resources.updateConfiguration(config, dm)
            }
        }

        private fun postStatus(isForeground: Boolean) {
            if (mStatusListenerMap.isEmpty()) {
                return
            }
            for (onAppStatusChangedListener in mStatusListenerMap.values) {
                if (isForeground) {
                    onAppStatusChangedListener.onForeground()
                } else {
                    onAppStatusChangedListener.onBackground()
                }
            }
        }

        private fun consumeOnActivityDestroyedListener(activity: Activity) {
            val iterator = mDestroyedListenerMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                if (entry.key === activity) {
                    val value = entry.value
                    for (listener in value) {
                        listener.onActivityDestroyed(activity)
                    }
                    iterator.remove()
                }
            }
        }

        private fun fixSoftInputLeaks(activity: Activity?) {
            val imm = app.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val leakViews = arrayOf("mLastSrvView", "mCurRootView", "mServedView", "mNextServedView")
            for (leakView in leakViews) {
                try {
                    val leakViewField = InputMethodManager::class.java.getDeclaredField(leakView)
                            ?: continue
                    if (!leakViewField.isAccessible) {
                        leakViewField.isAccessible = true
                    }
                    val obj = leakViewField.get(imm) as? View ?: continue
                    if (obj.rootView === activity?.window?.decorView?.rootView) {
                        leakViewField.set(imm, null)
                    }
                } catch (ignore: Throwable) { /**/
                }

            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    abstract class Task<Result>(private val mCallback: Callback<Result>) : Runnable {

        @Volatile
        private var state = NEW

        val isDone: Boolean
            get() = state != NEW

        val isCanceled: Boolean
            get() = state == CANCELLED

        internal abstract fun doInBackground(): Result

        override fun run() {
            try {
                val t = doInBackground()

                if (state != NEW) {
                    return
                }
                state = COMPLETING
                UTIL_HANDLER.post { mCallback.onCall(t) }
            } catch (th: Throwable) {
                if (state != NEW) {
                    return
                }
                state = EXCEPTIONAL
            }

        }

        fun cancel() {
            state = CANCELLED
        }

        companion object {

            private val NEW = 0
            private val COMPLETING = 1
            private val CANCELLED = 2
            private val EXCEPTIONAL = 3
        }
    }

    interface Callback<T> {
        fun onCall(data: T)
    }

    interface OnAppStatusChangedListener {
        fun onForeground()

        fun onBackground()
    }

    interface OnActivityDestroyedListener {
        fun onActivityDestroyed(activity: Activity)
    }
}
