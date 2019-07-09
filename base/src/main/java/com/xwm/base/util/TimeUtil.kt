package com.xwm.base.util

import com.xwm.base.constants.TimeConstants
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xwm on 2019-07-09
 */
class TimeUtil private constructor() {

    companion object {

        private val SDF_THREAD_LOCAL = ThreadLocal<SimpleDateFormat>()

        private val defaultFormat: SimpleDateFormat
            get() = getDateFormat("yyyy-MM-dd HH:mm:ss")

        private fun getDateFormat(pattern: String): SimpleDateFormat {
            var simpleDateFormat = SDF_THREAD_LOCAL.get()
            if (simpleDateFormat == null) {
                simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                SDF_THREAD_LOCAL.set(simpleDateFormat)
            } else {
                simpleDateFormat.applyPattern(pattern)
            }
            return simpleDateFormat
        }

        /**
         * Milliseconds to the formatted time string.
         *
         * @param millis  The milliseconds.
         * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
         * @return the formatted time string
         */
        fun millis2String(millis: Long, pattern: String): String {
            return millis2String(millis, getDateFormat(pattern))
        }

        /**
         * Milliseconds to the formatted time string.
         *
         * @param millis The milliseconds.
         * @param format The format.
         * @return the formatted time string
         */
        @JvmOverloads
        fun millis2String(millis: Long, format: DateFormat = defaultFormat): String {
            return format.format(Date(millis))
        }

        /**
         * Formatted time string to the milliseconds.
         *
         * @param time    The formatted time string.
         * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
         * @return the milliseconds
         */
        fun string2Millis(time: String, pattern: String): Long {
            return string2Millis(time, getDateFormat(pattern))
        }

        /**
         * Formatted time string to the milliseconds.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @return the milliseconds
         */
        @JvmOverloads
        fun string2Millis(time: String, format: DateFormat = defaultFormat): Long {
            try {
                return format.parse(time).time
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return -1
        }

        /**
         * Formatted time string to the date.
         *
         * @param time    The formatted time string.
         * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
         * @return the date
         */
        fun string2Date(time: String, pattern: String): Date? {
            return string2Date(time, getDateFormat(pattern))
        }

        /**
         * Formatted time string to the date.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @return the date
         */
        @JvmOverloads
        fun string2Date(time: String, format: DateFormat = defaultFormat): Date? {
            try {
                return format.parse(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        }

        /**
         * Date to the formatted time string.
         *
         * @param date    The date.
         * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
         * @return the formatted time string
         */
        fun date2String(date: Date, pattern: String): String {
            return getDateFormat(pattern).format(date)
        }

        /**
         * Date to the formatted time string.
         *
         * @param date   The date.
         * @param format The format.
         * @return the formatted time string
         */
        @JvmOverloads
        fun date2String(date: Date, format: DateFormat = defaultFormat): String {
            return format.format(date)
        }

        /**
         * Date to the milliseconds.
         *
         * @param date The date.
         * @return the milliseconds
         */
        fun date2Millis(date: Date): Long {
            return date.time
        }

        /**
         * Milliseconds to the date.
         *
         * @param millis The milliseconds.
         * @return the date
         */
        fun millis2Date(millis: Long): Date {
            return Date(millis)
        }

        /**
         * Return the time span, in unit.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time1 The first formatted time string.
         * @param time2 The second formatted time string.
         * @param unit  The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the time span, in unit
         */
        fun getTimeSpan(time1: String,
                        time2: String,
                        @TimeConstants.Unit unit: Int): Long {
            return getTimeSpan(time1, time2, defaultFormat, unit)
        }

        /**
         * Return the time span, in unit.
         *
         * @param time1  The first formatted time string.
         * @param time2  The second formatted time string.
         * @param format The format.
         * @param unit   The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the time span, in unit
         */
        fun getTimeSpan(time1: String,
                        time2: String,
                        format: DateFormat,
                        @TimeConstants.Unit unit: Int): Long {
            return millis2TimeSpan(string2Millis(time1, format) - string2Millis(time2, format), unit)
        }

        /**
         * Return the time span, in unit.
         *
         * @param date1 The first date.
         * @param date2 The second date.
         * @param unit  The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the time span, in unit
         */
        fun getTimeSpan(date1: Date,
                        date2: Date,
                        @TimeConstants.Unit unit: Int): Long {
            return millis2TimeSpan(date2Millis(date1) - date2Millis(date2), unit)
        }

        /**
         * Return the time span, in unit.
         *
         * @param millis1 The first milliseconds.
         * @param millis2 The second milliseconds.
         * @param unit    The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the time span, in unit
         */
        fun getTimeSpan(millis1: Long,
                        millis2: Long,
                        @TimeConstants.Unit unit: Int): Long {
            return millis2TimeSpan(millis1 - millis2, unit)
        }

        /**
         * Return the fit time span.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time1     The first formatted time string.
         * @param time2     The second formatted time string.
         * @param precision The precision of time span.
         *
         *  * precision = 0, return null
         *  * precision = 1, return 天
         *  * precision = 2, return 天, 小时
         *  * precision = 3, return 天, 小时, 分钟
         *  * precision = 4, return 天, 小时, 分钟, 秒
         *  * precision &gt;= 5，return 天, 小时, 分钟, 秒, 毫秒
         *
         * @return the fit time span
         */
        fun getFitTimeSpan(time1: String,
                           time2: String,
                           precision: Int): String? {
            val delta = string2Millis(time1, defaultFormat) - string2Millis(time2, defaultFormat)
            return millis2FitTimeSpan(delta, precision)
        }

        /**
         * Return the fit time span.
         *
         * @param time1     The first formatted time string.
         * @param time2     The second formatted time string.
         * @param format    The format.
         * @param precision The precision of time span.
         *
         *  * precision = 0, return null
         *  * precision = 1, return 天
         *  * precision = 2, return 天, 小时
         *  * precision = 3, return 天, 小时, 分钟
         *  * precision = 4, return 天, 小时, 分钟, 秒
         *  * precision &gt;= 5，return 天, 小时, 分钟, 秒, 毫秒
         *
         * @return the fit time span
         */
        fun getFitTimeSpan(time1: String,
                           time2: String,
                           format: DateFormat,
                           precision: Int): String? {
            val delta = string2Millis(time1, format) - string2Millis(time2, format)
            return millis2FitTimeSpan(delta, precision)
        }

        /**
         * Return the fit time span.
         *
         * @param date1     The first date.
         * @param date2     The second date.
         * @param precision The precision of time span.
         *
         *  * precision = 0, return null
         *  * precision = 1, return 天
         *  * precision = 2, return 天, 小时
         *  * precision = 3, return 天, 小时, 分钟
         *  * precision = 4, return 天, 小时, 分钟, 秒
         *  * precision &gt;= 5，return 天, 小时, 分钟, 秒, 毫秒
         *
         * @return the fit time span
         */
        fun getFitTimeSpan(date1: Date, date2: Date, precision: Int): String? {
            return millis2FitTimeSpan(date2Millis(date1) - date2Millis(date2), precision)
        }

        /**
         * Return the fit time span.
         *
         * @param millis1   The first milliseconds.
         * @param millis2   The second milliseconds.
         * @param precision The precision of time span.
         *
         *  * precision = 0, return null
         *  * precision = 1, return 天
         *  * precision = 2, return 天, 小时
         *  * precision = 3, return 天, 小时, 分钟
         *  * precision = 4, return 天, 小时, 分钟, 秒
         *  * precision &gt;= 5，return 天, 小时, 分钟, 秒, 毫秒
         *
         * @return the fit time span
         */
        fun getFitTimeSpan(millis1: Long,
                           millis2: Long,
                           precision: Int): String? {
            return millis2FitTimeSpan(millis1 - millis2, precision)
        }

        /**
         * Return the current time in milliseconds.
         *
         * @return the current time in milliseconds
         */
        val nowMills: Long
            get() = System.currentTimeMillis()

        /**
         * Return the current formatted time string.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @return the current formatted time string
         */
        val nowString: String
            get() = millis2String(System.currentTimeMillis(), defaultFormat)

        /**
         * Return the current formatted time string.
         *
         * @param format The format.
         * @return the current formatted time string
         */
        fun getNowString(format: DateFormat): String {
            return millis2String(System.currentTimeMillis(), format)
        }

        /**
         * Return the current date.
         *
         * @return the current date
         */
        val nowDate: Date
            get() = Date()

        /**
         * Return the time span by now, in unit.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time The formatted time string.
         * @param unit The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the time span by now, in unit
         */
        fun getTimeSpanByNow(time: String, @TimeConstants.Unit unit: Int): Long {
            return getTimeSpan(time, nowString, defaultFormat, unit)
        }

        /**
         * Return the time span by now, in unit.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @param unit   The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the time span by now, in unit
         */
        fun getTimeSpanByNow(time: String,
                             format: DateFormat,
                             @TimeConstants.Unit unit: Int): Long {
            return getTimeSpan(time, getNowString(format), format, unit)
        }

        /**
         * Return the time span by now, in unit.
         *
         * @param date The date.
         * @param unit The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the time span by now, in unit
         */
        fun getTimeSpanByNow(date: Date, @TimeConstants.Unit unit: Int): Long {
            return getTimeSpan(date, Date(), unit)
        }

        /**
         * Return the time span by now, in unit.
         *
         * @param millis The milliseconds.
         * @param unit   The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the time span by now, in unit
         */
        fun getTimeSpanByNow(millis: Long, @TimeConstants.Unit unit: Int): Long {
            return getTimeSpan(millis, System.currentTimeMillis(), unit)
        }

        /**
         * Return the fit time span by now.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time      The formatted time string.
         * @param precision The precision of time span.
         *
         *  * precision = 0，返回 null
         *  * precision = 1，返回天
         *  * precision = 2，返回天和小时
         *  * precision = 3，返回天、小时和分钟
         *  * precision = 4，返回天、小时、分钟和秒
         *  * precision &gt;= 5，返回天、小时、分钟、秒和毫秒
         *
         * @return the fit time span by now
         */
        fun getFitTimeSpanByNow(time: String, precision: Int): String? {
            return getFitTimeSpan(time, nowString, defaultFormat, precision)
        }

        /**
         * Return the fit time span by now.
         *
         * @param time      The formatted time string.
         * @param format    The format.
         * @param precision The precision of time span.
         *
         *  * precision = 0，返回 null
         *  * precision = 1，返回天
         *  * precision = 2，返回天和小时
         *  * precision = 3，返回天、小时和分钟
         *  * precision = 4，返回天、小时、分钟和秒
         *  * precision &gt;= 5，返回天、小时、分钟、秒和毫秒
         *
         * @return the fit time span by now
         */
        fun getFitTimeSpanByNow(time: String,
                                format: DateFormat,
                                precision: Int): String? {
            return getFitTimeSpan(time, getNowString(format), format, precision)
        }

        /**
         * Return the fit time span by now.
         *
         * @param date      The date.
         * @param precision The precision of time span.
         *
         *  * precision = 0，返回 null
         *  * precision = 1，返回天
         *  * precision = 2，返回天和小时
         *  * precision = 3，返回天、小时和分钟
         *  * precision = 4，返回天、小时、分钟和秒
         *  * precision &gt;= 5，返回天、小时、分钟、秒和毫秒
         *
         * @return the fit time span by now
         */
        fun getFitTimeSpanByNow(date: Date, precision: Int): String? {
            return getFitTimeSpan(date, nowDate, precision)
        }

        /**
         * Return the fit time span by now.
         *
         * @param millis    The milliseconds.
         * @param precision The precision of time span.
         *
         *  * precision = 0，返回 null
         *  * precision = 1，返回天
         *  * precision = 2，返回天和小时
         *  * precision = 3，返回天、小时和分钟
         *  * precision = 4，返回天、小时、分钟和秒
         *  * precision &gt;= 5，返回天、小时、分钟、秒和毫秒
         *
         * @return the fit time span by now
         */
        fun getFitTimeSpanByNow(millis: Long, precision: Int): String? {
            return getFitTimeSpan(millis, System.currentTimeMillis(), precision)
        }

        /**
         * Return the friendly time span by now.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @return the friendly time span by now
         *
         *  * 如果小于 1 秒钟内，显示刚刚
         *  * 如果在 1 分钟内，显示 XXX秒前
         *  * 如果在 1 小时内，显示 XXX分钟前
         *  * 如果在 1 小时外的今天内，显示今天15:32
         *  * 如果是昨天的，显示昨天15:32
         *  * 其余显示，2016-10-15
         *  * 时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007
         *
         */
        @JvmOverloads
        fun getFriendlyTimeSpanByNow(time: String,
                                     format: DateFormat = defaultFormat): String {
            return getFriendlyTimeSpanByNow(string2Millis(time, format))
        }

        /**
         * Return the friendly time span by now.
         *
         * @param date The date.
         * @return the friendly time span by now
         *
         *  * 如果小于 1 秒钟内，显示刚刚
         *  * 如果在 1 分钟内，显示 XXX秒前
         *  * 如果在 1 小时内，显示 XXX分钟前
         *  * 如果在 1 小时外的今天内，显示今天15:32
         *  * 如果是昨天的，显示昨天15:32
         *  * 其余显示，2016-10-15
         *  * 时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007
         *
         */
        fun getFriendlyTimeSpanByNow(date: Date): String {
            return getFriendlyTimeSpanByNow(date.time)
        }

        /**
         * Return the friendly time span by now.
         *
         * @param millis The milliseconds.
         * @return the friendly time span by now
         *
         *  * 如果小于 1 秒钟内，显示刚刚
         *  * 如果在 1 分钟内，显示 XXX秒前
         *  * 如果在 1 小时内，显示 XXX分钟前
         *  * 如果在 1 小时外的今天内，显示今天15:32
         *  * 如果是昨天的，显示昨天15:32
         *  * 其余显示，2016-10-15
         *  * 时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007
         *
         */
        fun getFriendlyTimeSpanByNow(millis: Long): String {
            val now = System.currentTimeMillis()
            val span = now - millis
            if (span < 0) {
                // U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
                return String.format("%tc", millis)
            }
            if (span < 1000) {
                return "刚刚"
            } else if (span < TimeConstants.MIN) {
                return String.format(Locale.getDefault(), "%d秒前", span / TimeConstants.SEC)
            } else if (span < TimeConstants.HOUR) {
                return String.format(Locale.getDefault(), "%d分钟前", span / TimeConstants.MIN)
            }
            // 获取当天 00:00
            val wee = weeOfToday
            return if (millis >= wee) {
                String.format("今天%tR", millis)
            } else if (millis >= wee - TimeConstants.DAY) {
                String.format("昨天%tR", millis)
            } else {
                String.format("%tF", millis)
            }
        }

        private val weeOfToday: Long
            get() {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.MILLISECOND, 0)
                return cal.timeInMillis
            }

        /**
         * Return the milliseconds differ time span.
         *
         * @param millis   The milliseconds.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the milliseconds differ time span
         */
        fun getMillis(millis: Long,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): Long {
            return millis + timeSpan2Millis(timeSpan, unit)
        }

        /**
         * Return the milliseconds differ time span.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time     The formatted time string.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the milliseconds differ time span
         */
        fun getMillis(time: String,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): Long {
            return getMillis(time, defaultFormat, timeSpan, unit)
        }

        /**
         * Return the milliseconds differ time span.
         *
         * @param time     The formatted time string.
         * @param format   The format.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the milliseconds differ time span.
         */
        fun getMillis(time: String,
                      format: DateFormat,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): Long {
            return string2Millis(time, format) + timeSpan2Millis(timeSpan, unit)
        }

        /**
         * Return the milliseconds differ time span.
         *
         * @param date     The date.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the milliseconds differ time span.
         */
        fun getMillis(date: Date,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): Long {
            return date2Millis(date) + timeSpan2Millis(timeSpan, unit)
        }

        /**
         * Return the formatted time string differ time span.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param millis   The milliseconds.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the formatted time string differ time span
         */
        fun getString(millis: Long,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): String {
            return getString(millis, defaultFormat, timeSpan, unit)
        }

        /**
         * Return the formatted time string differ time span.
         *
         * @param millis   The milliseconds.
         * @param format   The format.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the formatted time string differ time span
         */
        fun getString(millis: Long,
                      format: DateFormat,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): String {
            return millis2String(millis + timeSpan2Millis(timeSpan, unit), format)
        }

        /**
         * Return the formatted time string differ time span.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time     The formatted time string.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the formatted time string differ time span
         */
        fun getString(time: String,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): String {
            return getString(time, defaultFormat, timeSpan, unit)
        }

        /**
         * Return the formatted time string differ time span.
         *
         * @param time     The formatted time string.
         * @param format   The format.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the formatted time string differ time span
         */
        fun getString(time: String,
                      format: DateFormat,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): String {
            return millis2String(string2Millis(time, format) + timeSpan2Millis(timeSpan, unit), format)
        }

        /**
         * Return the formatted time string differ time span.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param date     The date.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the formatted time string differ time span
         */
        fun getString(date: Date,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): String {
            return getString(date, defaultFormat, timeSpan, unit)
        }

        /**
         * Return the formatted time string differ time span.
         *
         * @param date     The date.
         * @param format   The format.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the formatted time string differ time span
         */
        fun getString(date: Date,
                      format: DateFormat,
                      timeSpan: Long,
                      @TimeConstants.Unit unit: Int): String {
            return millis2String(date2Millis(date) + timeSpan2Millis(timeSpan, unit), format)
        }

        /**
         * Return the date differ time span.
         *
         * @param millis   The milliseconds.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the date differ time span
         */
        fun getDate(millis: Long,
                    timeSpan: Long,
                    @TimeConstants.Unit unit: Int): Date {
            return millis2Date(millis + timeSpan2Millis(timeSpan, unit))
        }

        /**
         * Return the date differ time span.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time     The formatted time string.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the date differ time span
         */
        fun getDate(time: String,
                    timeSpan: Long,
                    @TimeConstants.Unit unit: Int): Date {
            return getDate(time, defaultFormat, timeSpan, unit)
        }

        /**
         * Return the date differ time span.
         *
         * @param time     The formatted time string.
         * @param format   The format.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the date differ time span
         */
        fun getDate(time: String,
                    format: DateFormat,
                    timeSpan: Long,
                    @TimeConstants.Unit unit: Int): Date {
            return millis2Date(string2Millis(time, format) + timeSpan2Millis(timeSpan, unit))
        }

        /**
         * Return the date differ time span.
         *
         * @param date     The date.
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the date differ time span
         */
        fun getDate(date: Date,
                    timeSpan: Long,
                    @TimeConstants.Unit unit: Int): Date {
            return millis2Date(date2Millis(date) + timeSpan2Millis(timeSpan, unit))
        }

        /**
         * Return the milliseconds differ time span by now.
         *
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the milliseconds differ time span by now
         */
        fun getMillisByNow(timeSpan: Long, @TimeConstants.Unit unit: Int): Long {
            return getMillis(nowMills, timeSpan, unit)
        }

        /**
         * Return the formatted time string differ time span by now.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the formatted time string differ time span by now
         */
        fun getStringByNow(timeSpan: Long, @TimeConstants.Unit unit: Int): String {
            return getStringByNow(timeSpan, defaultFormat, unit)
        }

        /**
         * Return the formatted time string differ time span by now.
         *
         * @param timeSpan The time span.
         * @param format   The format.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the formatted time string differ time span by now
         */
        fun getStringByNow(timeSpan: Long,
                           format: DateFormat,
                           @TimeConstants.Unit unit: Int): String {
            return getString(nowMills, format, timeSpan, unit)
        }

        /**
         * Return the date differ time span by now.
         *
         * @param timeSpan The time span.
         * @param unit     The unit of time span.
         *
         *  * [TimeConstants.MSEC]
         *  * [TimeConstants.SEC]
         *  * [TimeConstants.MIN]
         *  * [TimeConstants.HOUR]
         *  * [TimeConstants.DAY]
         *
         * @return the date differ time span by now
         */
        fun getDateByNow(timeSpan: Long, @TimeConstants.Unit unit: Int): Date {
            return getDate(nowMills, timeSpan, unit)
        }

        /**
         * Return whether it is today.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time The formatted time string.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isToday(time: String): Boolean {
            return isToday(string2Millis(time, defaultFormat))
        }

        /**
         * Return whether it is today.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isToday(time: String, format: DateFormat): Boolean {
            return isToday(string2Millis(time, format))
        }

        /**
         * Return whether it is today.
         *
         * @param date The date.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isToday(date: Date): Boolean {
            return isToday(date.time)
        }

        /**
         * Return whether it is today.
         *
         * @param millis The milliseconds.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isToday(millis: Long): Boolean {
            val wee = weeOfToday
            return millis >= wee && millis < wee + TimeConstants.DAY
        }

        /**
         * Return whether it is leap year.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time The formatted time string.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isLeapYear(time: String): Boolean {
            return isLeapYear(string2Date(time, defaultFormat))
        }

        /**
         * Return whether it is leap year.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isLeapYear(time: String, format: DateFormat): Boolean {
            return isLeapYear(string2Date(time, format))
        }

        /**
         * Return whether it is leap year.
         *
         * @param date The date.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isLeapYear(date: Date?): Boolean {
            val cal = Calendar.getInstance()
            cal.time = date
            val year = cal.get(Calendar.YEAR)
            return isLeapYear(year)
        }

        /**
         * Return whether it is leap year.
         *
         * @param millis The milliseconds.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isLeapYear(millis: Long): Boolean {
            return isLeapYear(millis2Date(millis))
        }

        /**
         * Return whether it is leap year.
         *
         * @param year The year.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isLeapYear(year: Int): Boolean {
            return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
        }

        /**
         * Return the day of week in Chinese.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time The formatted time string.
         * @return the day of week in Chinese
         */
        fun getChineseWeek(time: String): String {
            return getChineseWeek(string2Date(time, defaultFormat))
        }

        /**
         * Return the day of week in Chinese.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @return the day of week in Chinese
         */
        fun getChineseWeek(time: String, format: DateFormat): String {
            return getChineseWeek(string2Date(time, format))
        }

        /**
         * Return the day of week in Chinese.
         *
         * @param date The date.
         * @return the day of week in Chinese
         */
        fun getChineseWeek(date: Date?): String {
            return SimpleDateFormat("E", Locale.CHINA).format(date)
        }

        /**
         * Return the day of week in Chinese.
         *
         * @param millis The milliseconds.
         * @return the day of week in Chinese
         */
        fun getChineseWeek(millis: Long): String {
            return getChineseWeek(Date(millis))
        }

        /**
         * Return the day of week in US.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time The formatted time string.
         * @return the day of week in US
         */
        fun getUSWeek(time: String): String {
            return getUSWeek(string2Date(time, defaultFormat))
        }

        /**
         * Return the day of week in US.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @return the day of week in US
         */
        fun getUSWeek(time: String, format: DateFormat): String {
            return getUSWeek(string2Date(time, format))
        }

        /**
         * Return the day of week in US.
         *
         * @param date The date.
         * @return the day of week in US
         */
        fun getUSWeek(date: Date?): String {
            return SimpleDateFormat("EEEE", Locale.US).format(date)
        }

        /**
         * Return the day of week in US.
         *
         * @param millis The milliseconds.
         * @return the day of week in US
         */
        fun getUSWeek(millis: Long): String {
            return getUSWeek(Date(millis))
        }

        /**
         * Returns the value of the given calendar field.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time  The formatted time string.
         * @param field The given calendar field.
         *
         *  * [Calendar.ERA]
         *  * [Calendar.YEAR]
         *  * [Calendar.MONTH]
         *  * ...
         *  * [Calendar.DST_OFFSET]
         *
         * @return the value of the given calendar field
         */
        fun getValueByCalendarField(time: String, field: Int): Int {
            return getValueByCalendarField(string2Date(time, defaultFormat), field)
        }

        /**
         * Returns the value of the given calendar field.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @param field  The given calendar field.
         *
         *  * [Calendar.ERA]
         *  * [Calendar.YEAR]
         *  * [Calendar.MONTH]
         *  * ...
         *  * [Calendar.DST_OFFSET]
         *
         * @return the value of the given calendar field
         */
        fun getValueByCalendarField(time: String,
                                    format: DateFormat,
                                    field: Int): Int {
            return getValueByCalendarField(string2Date(time, format), field)
        }

        /**
         * Returns the value of the given calendar field.
         *
         * @param date  The date.
         * @param field The given calendar field.
         *
         *  * [Calendar.ERA]
         *  * [Calendar.YEAR]
         *  * [Calendar.MONTH]
         *  * ...
         *  * [Calendar.DST_OFFSET]
         *
         * @return the value of the given calendar field
         */
        fun getValueByCalendarField(date: Date?, field: Int): Int {
            val cal = Calendar.getInstance()
            cal.time = date
            return cal.get(field)
        }

        /**
         * Returns the value of the given calendar field.
         *
         * @param millis The milliseconds.
         * @param field  The given calendar field.
         *
         *  * [Calendar.ERA]
         *  * [Calendar.YEAR]
         *  * [Calendar.MONTH]
         *  * ...
         *  * [Calendar.DST_OFFSET]
         *
         * @return the value of the given calendar field
         */
        fun getValueByCalendarField(millis: Long, field: Int): Int {
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis
            return cal.get(field)
        }

        private val CHINESE_ZODIAC = arrayOf("猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊")

        /**
         * Return the Chinese zodiac.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time The formatted time string.
         * @return the Chinese zodiac
         */
        fun getChineseZodiac(time: String): String {
            return getChineseZodiac(string2Date(time, defaultFormat))
        }

        /**
         * Return the Chinese zodiac.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @return the Chinese zodiac
         */
        fun getChineseZodiac(time: String, format: DateFormat): String {
            return getChineseZodiac(string2Date(time, format))
        }

        /**
         * Return the Chinese zodiac.
         *
         * @param date The date.
         * @return the Chinese zodiac
         */
        fun getChineseZodiac(date: Date?): String {
            val cal = Calendar.getInstance()
            cal.time = date
            return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12]
        }

        /**
         * Return the Chinese zodiac.
         *
         * @param millis The milliseconds.
         * @return the Chinese zodiac
         */
        fun getChineseZodiac(millis: Long): String {
            return getChineseZodiac(millis2Date(millis))
        }

        /**
         * Return the Chinese zodiac.
         *
         * @param year The year.
         * @return the Chinese zodiac
         */
        fun getChineseZodiac(year: Int): String {
            return CHINESE_ZODIAC[year % 12]
        }

        private val ZODIAC_FLAGS = intArrayOf(20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22)
        private val ZODIAC = arrayOf("水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座")

        /**
         * Return the zodiac.
         *
         * The pattern is `yyyy-MM-dd HH:mm:ss`.
         *
         * @param time The formatted time string.
         * @return the zodiac
         */
        fun getZodiac(time: String): String {
            return getZodiac(string2Date(time, defaultFormat))
        }

        /**
         * Return the zodiac.
         *
         * @param time   The formatted time string.
         * @param format The format.
         * @return the zodiac
         */
        fun getZodiac(time: String, format: DateFormat): String {
            return getZodiac(string2Date(time, format))
        }

        /**
         * Return the zodiac.
         *
         * @param date The date.
         * @return the zodiac
         */
        fun getZodiac(date: Date?): String {
            val cal = Calendar.getInstance()
            cal.time = date
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            return getZodiac(month, day)
        }

        /**
         * Return the zodiac.
         *
         * @param millis The milliseconds.
         * @return the zodiac
         */
        fun getZodiac(millis: Long): String {
            return getZodiac(millis2Date(millis))
        }

        /**
         * Return the zodiac.
         *
         * @param month The month.
         * @param day   The day.
         * @return the zodiac
         */
        fun getZodiac(month: Int, day: Int): String {
            return ZODIAC[if (day >= ZODIAC_FLAGS[month - 1])
                month - 1
            else
                (month + 10) % 12]
        }

        private fun timeSpan2Millis(timeSpan: Long, @TimeConstants.Unit unit: Int): Long {
            return timeSpan * unit
        }

        private fun millis2TimeSpan(millis: Long, @TimeConstants.Unit unit: Int): Long {
            return millis / unit
        }

        private fun millis2FitTimeSpan(millis: Long, precision: Int): String? {
            var sMillis = millis
            var sPrecision = precision
            if (sPrecision <= 0) {
                return null
            }
            sPrecision = Math.min(sPrecision, 5)
            val units = arrayOf("天", "小时", "分钟", "秒", "毫秒")
            if (sMillis == 0L) {
                return 0.toString() + units[sPrecision - 1]
            }
            val sb = StringBuilder()
            if (sMillis < 0) {
                sb.append("-")
                sMillis = -sMillis
            }
            val unitLen = intArrayOf(86400000, 3600000, 60000, 1000, 1)
            for (i in 0 until sPrecision) {
                if (sMillis >= unitLen[i]) {
                    val mode = sMillis / unitLen[i]
                    sMillis -= mode * unitLen[i]
                    sb.append(mode).append(units[i])
                }
            }
            return sb.toString()
        }
    }
}