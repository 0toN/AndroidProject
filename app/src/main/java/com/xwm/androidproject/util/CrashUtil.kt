package com.xwm.androidproject.util

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresPermission
import com.xwm.androidproject.App
import java.io.*
import java.lang.Thread.UncaughtExceptionHandler
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/09/27
 * desc  : utils about crash
</pre> *
 */
class CrashUtil {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    interface OnCrashListener {
        fun onCrash(crashInfo: String, e: Throwable?)
    }

    companion object {
        private val TAG = "CrashUtil"

        private var appContext: Context? = null

        private var defaultDir: String? = null
        private var dir: String? = null
        private var versionName: String? = null
        private var versionCode: Int = 0

        private val FILE_SEP = System.getProperty("file.separator")
        @SuppressLint("SimpleDateFormat")
        private val FORMAT = SimpleDateFormat("MM-dd HH-mm-ss")

        private val DEFAULT_UNCAUGHT_EXCEPTION_HANDLER: UncaughtExceptionHandler?
        private val UNCAUGHT_EXCEPTION_HANDLER: UncaughtExceptionHandler

        private var sOnCrashListener: OnCrashListener? = null

        init {
            appContext = App.context
            try {
                val pi = appContext!!.packageManager
                        .getPackageInfo(appContext!!.packageName, 0)
                if (pi != null) {
                    versionName = pi.versionName
                    versionCode = pi.versionCode
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler()

            UNCAUGHT_EXCEPTION_HANDLER = UncaughtExceptionHandler { t, e ->
                if (e == null) {
                    if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e)
                        //这里可以将异常信息上传至服务器
                    } else {
                        android.os.Process.killProcess(android.os.Process.myPid())
                        System.exit(1)
                    }
                    return@UncaughtExceptionHandler
                }

                val time = FORMAT.format(Date(System.currentTimeMillis()))
                val sb = StringBuilder()
                val head = "************* Log Head ****************" +
                        "\nTime Of Crash      : " + time +
                        "\nDevice Manufacturer: " + Build.MANUFACTURER +
                        "\nDevice Model       : " + Build.MODEL +
                        "\nAndroid Version    : " + Build.VERSION.RELEASE +
                        "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                        "\nApp VersionName    : " + versionName +
                        "\nApp VersionCode    : " + versionCode +
                        "\n************* Log Head ****************\n\n"
                sb.append(head)
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                e.printStackTrace(pw)
                var cause: Throwable? = e.cause
                while (cause != null) {
                    cause.printStackTrace(pw)
                    cause = cause.cause
                }
                pw.flush()
                sb.append(sw.toString())
                val crashInfo = sb.toString()
                val fullPath = (if (dir == null) defaultDir else dir) + time + ".txt"
                if (createOrExistsFile(fullPath)) {
                    input2File(crashInfo, fullPath)
                } else {
                    LogUtil.e(TAG, "create $fullPath failed!")
                }

                if (sOnCrashListener != null) {
                    sOnCrashListener!!.onCrash(crashInfo, e)
                }

                DEFAULT_UNCAUGHT_EXCEPTION_HANDLER?.uncaughtException(t, e)
            }
        }

        /**
         * Initialization
         *
         * Must hold
         * `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`
         *
         * @param crashDir The directory of saving crash information.
         */
        @RequiresPermission(WRITE_EXTERNAL_STORAGE)
        fun init(crashDir: File) {
            init(crashDir.absolutePath, null)
        }

        /**
         * Initialization
         *
         * Must hold
         * `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`
         *
         * @param onCrashListener The crash listener.
         */
        @RequiresPermission(WRITE_EXTERNAL_STORAGE)
        fun init(onCrashListener: OnCrashListener) {
            init("", onCrashListener)
        }

        /**
         * Initialization
         *
         * Must hold
         * `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`
         *
         * @param crashDir        The directory of saving crash information.
         * @param onCrashListener The crash listener.
         */
        @RequiresPermission(WRITE_EXTERNAL_STORAGE)
        fun init(crashDir: File, onCrashListener: OnCrashListener) {
            init(crashDir.absolutePath, onCrashListener)
        }

        /**
         * Initialization
         *
         * Must hold
         * `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`
         *
         * @param crashDirPath    The directory's path of saving crash information.
         * @param onCrashListener The crash listener.
         */
        @RequiresPermission(WRITE_EXTERNAL_STORAGE)
        @JvmOverloads
        fun init(crashDirPath: String = "", onCrashListener: OnCrashListener? = null) {
            if (isSpace(crashDirPath)) {
                dir = null
            } else {
                dir = if (crashDirPath.endsWith(FILE_SEP!!)) crashDirPath else crashDirPath + FILE_SEP
            }
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() && appContext!!.externalCacheDir != null) {
                defaultDir = appContext!!.externalCacheDir.toString() + FILE_SEP + "crash" + FILE_SEP
            } else {
                defaultDir = appContext!!.cacheDir.toString() + FILE_SEP + "crash" + FILE_SEP
            }
            sOnCrashListener = onCrashListener
            Thread.setDefaultUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER)
        }

        ///////////////////////////////////////////////////////////////////////////
        // other utils methods
        ///////////////////////////////////////////////////////////////////////////

        private fun input2File(input: String, filePath: String) {
            val submit = Executors.newSingleThreadExecutor().submit(Callable {
                var bw: BufferedWriter? = null
                try {
                    bw = BufferedWriter(FileWriter(filePath, true))
                    bw.write(input)
                    return@Callable true
                } catch (e: IOException) {
                    e.printStackTrace()
                    return@Callable false
                } finally {
                    try {
                        bw?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            })
            try {
                if (submit.get()) {
                    return
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }

            LogUtil.e(TAG, "write crash info to $filePath failed!")
        }

        private fun createOrExistsFile(filePath: String): Boolean {
            val file = File(filePath)
            if (file.exists()) {
                return file.isFile
            }
            if (!createOrExistsDir(file.parentFile)) {
                return false
            }
            try {
                return file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

        }

        private fun createOrExistsDir(file: File?): Boolean {
            return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
        }

        private fun isSpace(s: String?): Boolean {
            if (s == null) {
                return true
            }
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        }
    }
}
/**
 * Initialization.
 *
 * Must hold
 * `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`
 */
/**
 * Initialization
 *
 * Must hold
 * `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`
 *
 * @param crashDirPath The directory's path of saving crash information.
 */
