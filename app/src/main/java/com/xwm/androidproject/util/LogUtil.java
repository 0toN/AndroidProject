package com.xwm.androidproject.util;

import android.util.Log;

/**
 * @author Created by Adam on 2018-12-20
 */
public class LogUtil {
    private static boolean debug = AppUtil.isApkInDebug();

    private LogUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        LogUtil.debug = debug;
    }

    public static void v(String content) {
        if (!debug) {
            return;
        }
        log(Log.VERBOSE, content);
    }

    public static void w(String content) {
        if (!debug) {
            return;
        }
        log(Log.WARN, content);
    }

    public static void i(String content) {
        if (!debug) {
            return;
        }
        log(Log.INFO, content);
    }

    public static void d(String content) {
        if (!debug) {
            return;
        }
        log(Log.DEBUG, content);
    }

    public static void d(String tag, String content) {
        if (!debug) {
            return;
        }
        log(Log.DEBUG, tag, content);
    }

    public static void e(String content) {
        if (!debug) {
            return;
        }
        log(Log.ERROR, content);
    }

    public static void e(String tag, String content) {
        if (!debug) {
            return;
        }
        log(Log.ERROR, tag, content);
    }

    public static void e(Exception e) {
        e(Log.getStackTraceString(e));
    }

    public static void e(Throwable tr) {
        e(Log.getStackTraceString(tr));
    }

    private static void log(int priority, String tag, String log) {
        if (!debug) {
            return;
        }
        Log.println(priority, tag, log);
    }

    private static void log(int priority, String content) {
        if (!debug) {
            return;
        }
        Log.println(priority, getTag(), content);
    }

    private static String getTag() {
        String classCanonicalName = LogUtil.class.getCanonicalName();
        String thredCanonicalName = Thread.class.getCanonicalName();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = elements[0];
        String traceClassName;
        for (StackTraceElement element : elements) {
            traceClassName = element.getClassName();
            assert classCanonicalName != null;
            assert thredCanonicalName != null;
            if (!(traceClassName.startsWith(classCanonicalName)
                    || traceClassName.startsWith(thredCanonicalName)
                    || traceClassName.startsWith("dalvik.system.VMStack"))) {
                stackTraceElement = element;
                break;
            }
        }
        String invokedClass = stackTraceElement.getClassName();
        invokedClass = invokedClass.substring(invokedClass.lastIndexOf(".") + 1);
        int lineNumber = stackTraceElement.getLineNumber();
        return invokedClass + "-" + lineNumber;
    }
}
