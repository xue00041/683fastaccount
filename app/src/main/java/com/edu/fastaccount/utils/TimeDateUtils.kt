package com.edu.fastaccount.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间工具类
 */
object TimeDateUtils {
    // String类型时间的几种格式
    const val FORMAT_TYPE_1 = "yyyyMMdd"
    const val FORMAT_TYPE_5 = "yyyy-MM-dd"
    const val FORMAT_TYPE_2 = "MM Months dd days hh:mm"
    const val FORMAT_TYPE_3 = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_TYPE_4 = "yyyy years MM months dd days HH hours mm mins ss secs"

    /**
     * 获取当前时间戳
     */
    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取当前日期和时间
     */
    fun getCurrentDateStr(formatType: String?): String {
        val date = Date()
        val sdf = SimpleDateFormat(formatType)
        return sdf.format(date)
    }

    /**
     * 时间转换 Date ——> long
     */
    fun date2Long(date: Date): Long {
        return date.time
    }

    /**
     * 时间转换 Date ——> String
     */
    @JvmStatic
    fun date2String(date: Date?, formatType: String?): String {
        return SimpleDateFormat(formatType).format(date)
    }

    /**
     * 时间转换 long ——> Date
     */
    fun long2Date(time: Long, formatType: String?): Date? {
        val oldDate = Date(time)
        val dateStr = date2String(oldDate, formatType)
        return string2Date(dateStr, formatType)
    }

    /**
     * 时间转换 long ——> String
     */
    fun long2String(time: Long, formatType: String?): String {
        val date = long2Date(time, formatType)
        return date2String(date, formatType)
    }

    /**
     * 时间转换 String ——> Date
     */
    fun string2Date(strTime: String?, formatType: String?): Date? {
        val format = SimpleDateFormat(formatType)
        var date: Date? = null
        try {
            date = format.parse(strTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    /**
     * 时间转换 String ——> long
     */
    fun string2Long(strTime: String?, formatType: String?): Long {
        val date = string2Date(strTime, formatType)
        return date?.let { date2Long(it) } ?: 0
    }
}