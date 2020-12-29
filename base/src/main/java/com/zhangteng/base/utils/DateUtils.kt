package com.zhangteng.base.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期操作工具类.
 */
object DateUtils {
    /**
     * 英文简写如：2016
     */
    var FORMAT_Y: String? = "yyyy"

    /**
     * 英文简写如：12
     */
    var FORMAT_H: String? = "HH"

    /**
     * 英文简写如：12:01
     */
    var FORMAT_M: String? = "mm"

    /**
     * 英文简写如：12:01
     */
    var FORMAT_HM: String? = "HH:mm"

    /**
     * 英文简写如：1-12 12:01
     */
    var FORMAT_MDHM: String? = "MM-dd HH:mm"

    /**
     * 英文简写（默认）如：2016-12-01
     */
    var FORMAT_YMD: String? = "yyyy-MM-dd"

    /**
     * 英文简写（默认）如：2016-12
     */
    var FORMAT_YM: String? = "yyyy-MM"

    /**
     * 英文全称  如：2016-12-01 23:15
     */
    var FORMAT_YMDHM: String? = "yyyy-MM-dd HH:mm"

    /**
     * 英文全称  如：2016-12-01 23:15:06
     */
    var FORMAT_YMDHMS: String? = "yyyy-MM-dd HH:mm:ss"

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    var FORMAT_FULL: String? = "yyyy-MM-dd HH:mm:ss.S"

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    var FORMAT_FULL_SN: String? = "yyyyMMddHHmmssS"

    /**
     * 中文简写  如：212月01日
     */
    var FORMAT_MD_CN: String? = "MM月dd日"

    /**
     * 中文简写  如：2016年12月01日
     */
    var FORMAT_YMD_CN: String? = "yyyy年MM月dd日"

    /**
     * 中文简写  如：2016年12月01日  12时
     */
    var FORMAT_YMDH_CN: String? = "yyyy年MM月dd日 HH时"

    /**
     * 中文简写  如：2016年12月01日  12时12分
     */
    var FORMAT_YMDHM_CN: String? = "yyyy年MM月dd日 HH时mm分"

    /**
     * 中文全称  如：2016年12月01日  23时15分06秒
     */
    var FORMAT_YMDHMS_CN: String? = "yyyy年MM月dd日  HH时mm分ss秒"

    /**
     * 精确到毫秒的完整中文时间
     */
    var FORMAT_FULL_CN: String? = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒"

    @JvmOverloads
    fun str2Date(str: String?, format: String? = null): Date? {
        var format = format
        if (str == null || str.length == 0) {
            return null
        }
        if (format == null || format.length == 0) {
            format = FORMAT_YMDHMS
        }
        var date: Date? = null
        try {
            val sdf = SimpleDateFormat(format)
            date = sdf.parse(str)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date
    }

    @JvmOverloads
    fun str2Calendar(str: String?, format: String? = null): Calendar? {
        val date = str2Date(str, format) ?: return null
        val c = Calendar.getInstance()
        c.time = date
        return c
    }

    @JvmOverloads
    fun date2Str(c: Calendar?, format: String? = null): String? {
        return if (c == null) {
            null
        } else date2Str(c.time, format)
    }

    @JvmOverloads
    fun date2Str(d: Date?, format: String? = null): String? { // yyyy-MM-dd HH:mm:ss
        var format = format
        if (d == null) {
            return null
        }
        if (format == null || format.length == 0) {
            format = FORMAT_YMDHMS
        }
        val sdf = SimpleDateFormat(format)
        return sdf.format(d)
    }

    /**
     * 获得当前日期的字符串格式
     *
     * @param format 格式化的类型
     * @return 返回格式化之后的事件
     */
    fun getCurDateStr(format: String?): String? {
        val c = Calendar.getInstance()
        return date2Str(c, format)
    }

    /**
     * @param time 时间
     * @return 返回时间String
     */
    // 格式到毫秒
    fun getTime(time: Long, format: String?): String? {
        return SimpleDateFormat(format).format(time)
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return 增加数个整月
     */
    fun addMonth(date: Date?, n: Int): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, n)
        return cal.time
    }

    //时间转换时间戳
    fun getStringToDate(dateString: String?, pattern: String?): Long {
        val dateFormat = SimpleDateFormat(pattern)
        var date = Date()
        try {
            date = dateFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time
    }

    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return 增加之后的天数
     */
    fun addDay(date: Date?, n: Int): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, n)
        return cal.time
    }

    /**
     * 获取距现在某一小时的时刻
     *
     * @param format 格式化时间的格式
     * @param h      距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
     * @return 获取距现在某一小时的时刻
     */
    fun getNextHour(format: String?, h: Int): String? {
        val sdf = SimpleDateFormat(format)
        val date = Date()
        date.time = date.time + h * 60 * 60 * 1000
        return sdf.format(date)
    }

    /**
     * 获取时间戳
     *
     * @return 获取时间戳
     */
    fun getTimeString(): String? {
        val df = SimpleDateFormat(FORMAT_FULL)
        val calendar = Calendar.getInstance()
        return df.format(calendar.time)
    }

    /**
     * 功能描述：返回月
     *
     * @param date Date 日期
     * @return 返回月份
     */
    fun getMonth(date: Date?): Int {
        val calendar = Calendar.getInstance()
        if (date != null)
            calendar.time = date
        return calendar.get(Calendar.MONTH) + 1
    }

    /**
     * 功能描述：返回日
     *
     * @param date Date 日期
     * @return 返回日份
     */
    fun getDay(date: Date?): Int {
        val calendar = Calendar.getInstance()
        if (date != null)
            calendar.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 功能描述：返回小时
     *
     * @param date 日期
     * @return 返回小时
     */
    fun getHour(date: Date?): Int {
        val calendar = Calendar.getInstance()
        if (date != null)
            calendar.time = date
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    fun getMinute(date: Date?): Int {
        val calendar = Calendar.getInstance()
        if (date != null)
            calendar.time = date
        return calendar.get(Calendar.MINUTE)
    }

    /**
     * 获得默认的 date pattern
     *
     * @return 默认的格式
     */
    fun getDatePattern(): String? {
        return FORMAT_YMDHMS
    }

    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    fun getSecond(date: Date?): Int {
        val calendar = Calendar.getInstance()
        if (date != null)
            calendar.time = date
        return calendar.get(Calendar.SECOND)
    }

    /**
     * 功能描述：返回毫
     *
     * @param date 日期
     * @return 返回毫
     */
    fun getMillis(date: Date?): Long {
        val calendar = Calendar.getInstance()
        if (date != null)
            calendar.time = date
        return calendar.getTimeInMillis()
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return 按默认格式的字符串距离今天的天数
     */
    fun countDays(date: String?): Int {
        val t = Calendar.getInstance().time.time
        val c = Calendar.getInstance()
        if (date != null)
            c.time = parse(date)
        val t1 = c.time.time
        return (t / 1000 - t1 / 1000).toInt() / 3600 / 24
    }
    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return 提取字符串日期
     */
    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return 提取字符串的日期
     */
    @SuppressLint("SimpleDateFormat")
    @JvmOverloads
    fun parse(strDate: String?, pattern: String? = getDatePattern()): Date? {
        val df = SimpleDateFormat(pattern)
        return try {
            if (strDate != null)
                df.parse(strDate)
            else
                null
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        } catch (e: NullPointerException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return 按用户格式字符串距离今天的天数
     */
    fun countDays(date: String?, format: String?): Int {
        val t = Calendar.getInstance().time.time
        val c = Calendar.getInstance()
        c.time = parse(date, format)
        val t1 = c.time.time
        return (t / 1000 - t1 / 1000).toInt() / 3600 / 24
    }

    /**
     * 获取当天0点时间戳
     */
    fun dayTimeInMillis(): Long {
        val calendar = Calendar.getInstance() // 获取当前日期
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        return calendar.timeInMillis
    }

    /**
     * 获取当月0点时间戳
     */
    fun monthTimeInMillis(): Long {
        val calendar = Calendar.getInstance() // 获取当前日期
        calendar.add(Calendar.YEAR, 0)
        calendar.add(Calendar.MONTH, 0)
        calendar[Calendar.DAY_OF_MONTH] = 1 // 设置为1号,当前日期既为本月第一天
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        return calendar.timeInMillis
    }

    /**
     * 获取当年0点时间戳
     */
    fun yearTimeInMillis(): Long {
        val calendar = Calendar.getInstance() // 获取当前日期
        calendar.add(Calendar.YEAR, 0)
        calendar.add(Calendar.DATE, 0)
        calendar.add(Calendar.MONTH, 0)
        calendar[Calendar.DAY_OF_YEAR] = 1
        calendar[Calendar.HOUR] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        return calendar.timeInMillis
    }

    /**
     * 获取当天星期几
     */
    fun getWeek(): String {
        val cal = Calendar.getInstance()
        val i = cal[Calendar.DAY_OF_WEEK]
        return when (i) {
            1 -> "星期日"
            2 -> "星期一"
            3 -> "星期二"
            4 -> "星期三"
            5 -> "星期四"
            6 -> "星期五"
            7 -> "星期六"
            else -> ""
        }
    }

    /**
     * 获取当天星期几
     */
    fun getWeekNum(): Int {
        val cal = Calendar.getInstance()
        return cal[Calendar.DAY_OF_WEEK]
    }

    /**
     * @param time 时间
     * @return 返回时间String
     */
    // 格式到毫秒
    fun getPublishTime(time: Long): String? {
        val current = System.currentTimeMillis()
        val today: Long = dayTimeInMillis()
        val yesterday = today - 24 * 3600 * 1000
        val begin = Date(time)
        val end = Date(current)
        val diff = end.time - begin.time
        return if (time < yearTimeInMillis()) {
            getTime(time, FORMAT_YMDHM)
        } else if (time < yesterday) {
            getTime(time, FORMAT_MDHM)
        } else if (time < today) {
            String.format("昨天 %s", getTime(time, FORMAT_HM))
        } else if (time < current - 3600 * 1000) {
            val hours = diff % (1000 * 60 * 60 * 24) / (1000 * 60 * 60)
            hours.toString() + "小时前"
        } else if (time < current - 60 * 1000) {
            val minutes = diff % (1000 * 60 * 60) / (1000 * 60)
            minutes.toString() + "分钟前"
        } else {
            "刚刚"
        }
    }

    fun getRedTime(times: String?): String {
        if ("" == times || times == null) {
            return "0秒"
        }
        val time = times.toInt() / 1000
        // 时间差
        if (time < 60) {
            return String.format("%d秒", time)
        }
        val sec = time / 60
        if (sec < 60) {
            return String.format("%d分钟", sec)
        }

        // 秒转小时
        val hours = time / 3600
        if (hours < 24) {
            return String.format("%d小时", hours)
        }
        //秒转天数
        val days = time / 3600 / 24
        return if (days < 30) {
            String.format("%d天", days)
        } else String.format("%d秒", time)
    }
}