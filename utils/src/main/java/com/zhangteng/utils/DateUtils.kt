package com.zhangteng.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 英文简写如：12
 */
const val FORMAT_H: String = "HH"

/**
 * 英文简写如：12:01
 */
const val FORMAT_M: String = "mm"

/**
 * 英文简写如：12:01
 */
const val FORMAT_HM: String = "HH:mm"

/**
 * 英文简写如：1-12
 */
const val FORMAT_MD: String = "MM-dd"

/**
 * 英文简写如：1-12 12:01
 */
const val FORMAT_MDHM: String = "MM-dd HH:mm"

/**
 * 英文简写如：2016
 */
const val FORMAT_Y: String = "yyyy"

/**
 * 英文简写（默认）如：2016-12
 */
const val FORMAT_YM: String = "yyyy-MM"

/**
 * 英文简写（默认）如：2016-12-01
 */
const val FORMAT_YMD: String = "yyyy-MM-dd"

/**
 * 英文全称  如：2016-12-01 23
 */
const val FORMAT_YMDH: String = "yyyy-MM-dd HH"

/**
 * 英文全称  如：2016-12-01 23:15
 */
const val FORMAT_YMDHM: String = "yyyy-MM-dd HH:mm"

/**
 * 英文全称  如：2016-12-01 23:15:06
 */
const val FORMAT_YMDHMS: String = "yyyy-MM-dd HH:mm:ss"

/**
 * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
 */
const val FORMAT_FULL: String = "yyyy-MM-dd HH:mm:ss.S"

/**
 * 中文简写  如：212月01日
 */
const val FORMAT_MD_CN: String = "MM月dd日"

/**
 * 中文简写  如：2016年12月01日
 */
const val FORMAT_YMD_CN: String = "yyyy年MM月dd日"

/**
 * 中文简写  如：2016年12月01日  12时
 */
const val FORMAT_YMDH_CN: String = "yyyy年MM月dd日 HH时"

/**
 * 中文简写  如：2016年12月01日  12时12分
 */
const val FORMAT_YMDHM_CN: String = "yyyy年MM月dd日 HH时mm分"

/**
 * 中文全称  如：2016年12月01日  23时15分06秒
 */
const val FORMAT_YMDHMS_CN: String = "yyyy年MM月dd日  HH时mm分ss秒"

/**
 * 精确到毫秒的完整中文时间
 */
const val FORMAT_FULL_CN: String = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒"

/**
 * 格式化时间 时间毫秒
 * @return 返回时间String
 */
@SuppressLint("SimpleDateFormat")
fun getTimeStr(time: Long, format: String = FORMAT_YMDHMS): String? {
    return SimpleDateFormat(format).format(time)
}

/**
 * 获得当前日期的字符串格式
 *
 * @param format 格式化的类型
 * @return 返回格式化之后的事件
 */
fun getCurDateStr(format: String = FORMAT_YMDHMS): String? {
    return Calendar.getInstance().date2Str(format)
}

/**
 * 获取距现在某一小时的时刻
 *
 * @param format 格式化时间的格式
 * @param h      距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
 * @return 获取距现在某一小时的时刻
 */
@SuppressLint("SimpleDateFormat")
fun getNextHour(format: String? = FORMAT_YMDHMS, h: Int): String? {
    val sdf = SimpleDateFormat(format)
    val date = Date()
    date.time = date.time + h * 60 * 60 * 1000
    return sdf.format(date)
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
    return when (getWeekNum()) {
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
 * 使用预设格式提取字符串日期
 *
 * @return 提取字符串的日期
 */
@SuppressLint("SimpleDateFormat")
fun String?.str2Date(pattern: String? = FORMAT_YMDHMS): Date? {
    val df = SimpleDateFormat(pattern)
    return try {
        if (this != null)
            df.parse(this)
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
 * 使用预设格式提取字符串日期
 *
 * @return 提取字符串的日期
 */
fun String?.str2Calendar(format: String? = FORMAT_YMDHMS): Calendar? {
    val date = str2Date(format) ?: return null
    val c = Calendar.getInstance()
    c.time = date
    return c
}

/**
 * 时间字符串转时间戳
 * @return 时间转换时间戳
 */
@SuppressLint("SimpleDateFormat")
fun String?.strToTime(pattern: String? = FORMAT_YMDHMS): Long {
    val dateFormat = SimpleDateFormat(pattern)
    var date = Date()
    try {
        date = dateFormat.parse(this!!)!!
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return date.time
}

/**
 * Calendar转时间字符串
 * @return 时间字符串
 */
fun Calendar?.date2Str(format: String = FORMAT_YMDHMS): String? {
    return if (this == null) {
        null
    } else time.date2Str(format)
}

/**
 * Date转时间字符串
 * @return 时间字符串
 */
@SuppressLint("SimpleDateFormat")
fun Date?.date2Str(format: String = FORMAT_YMDHMS): String? { // yyyy-MM-dd HH:mm:ss
    if (this == null) {
        return null
    }
    val sdf = SimpleDateFormat(format)
    return sdf.format(this)
}

/**
 * 在日期上增加数个整月
 *
 * @param n    要增加的月数
 * @return 增加数个整月
 */
fun Date?.addMonth(n: Int): Date? {
    if (this == null) {
        return null
    }
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.MONTH, n)
    return cal.time
}

/**
 * 在日期上增加天数
 *
 * @param n    要增加的天数
 * @return 增加之后的天数
 */
fun Date?.addDay(n: Int): Date? {
    if (this == null) {
        return null
    }
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DATE, n)
    return cal.time
}

/**
 * 功能描述：返回月
 *
 * @return 返回月份
 */
fun Date?.getMonth(): Int? {
    if (this == null) {
        return null
    }
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MONTH) + 1
}

/**
 * 功能描述：返回日
 *
 * @return 返回日份
 */
fun Date?.getDay(): Int? {
    if (this == null) {
        return null
    }
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

/**
 * 功能描述：返回小时
 *
 * @return 返回小时
 */
fun Date?.getHour(): Int? {
    if (this == null) {
        return null
    }
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.HOUR_OF_DAY)
}

/**
 * 功能描述：返回分
 *
 * @return 返回分钟
 */
fun Date?.getMinute(): Int? {
    if (this == null) {
        return null
    }
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MINUTE)
}

/**
 * 返回秒钟
 *
 * @return 返回秒钟
 */
fun Date?.getSecond(): Int? {
    if (this == null) {
        return null
    }
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.SECOND)
}

/**
 * 功能描述：返回毫
 *
 * @return 返回毫
 */
fun Date?.getMillis(): Long? {
    if (this == null) {
        return null
    }
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.getTimeInMillis()
}

/**
 * 按默认格式的字符串距离今天的天数
 *
 * @param date 日期字符串
 * @return 按默认格式的字符串距离今天的天数
 */
fun Date?.countDays(date: String?): Int? {
    if (this == null) {
        return null
    }
    val t = Calendar.getInstance().time.time
    val c = Calendar.getInstance()
    if (date != null)
        c.time = date.str2Date()!!
    val t1 = c.time.time
    return (t / 1000 - t1 / 1000).toInt() / 3600 / 24
}

/**
 * 按用户格式字符串距离今天的天数
 *
 * @param date   日期字符串
 * @param format 日期格式
 * @return 按用户格式字符串距离今天的天数
 */
fun Date?.countDays(date: String?, format: String?): Int? {
    if (this == null) {
        return null
    }
    val t = Calendar.getInstance().time.time
    val c = Calendar.getInstance()
    c.time = date.str2Date(format)!!
    val t1 = c.time.time
    return (t / 1000 - t1 / 1000).toInt() / 3600 / 24
}

/**
 * 获取时间字符串：上一年显示年月日时分、前天显示月日时分、昨天显示“昨天 时分”、大于一小时显示“小时前”、大于一分钟显示“分钟前”、其余显示“刚刚”
 * @return 返回时间String
 */
fun Long?.getPublishTime(): String? {
    if (this == null) {
        return "刚刚"
    }
    val current = System.currentTimeMillis()
    val today: Long = dayTimeInMillis()
    val yesterday = today - 24 * 3600 * 1000
    val begin = Date(this)
    val end = Date(current)
    val diff = end.time - begin.time
    return when {
        this < yearTimeInMillis() -> {
            getTimeStr(this, FORMAT_YMDHM)
        }
        this < yesterday -> {
            getTimeStr(this, FORMAT_MDHM)
        }
        this < today -> {
            String.format("昨天 %s", getTimeStr(this, FORMAT_HM))
        }
        this < current - 3600 * 1000 -> {
            val hours = diff % (1000 * 60 * 60 * 24) / (1000 * 60 * 60)
            hours.toString() + "小时前"
        }
        this < current - 60 * 1000 -> {
            val minutes = diff % (1000 * 60 * 60) / (1000 * 60)
            minutes.toString() + "分钟前"
        }
        else -> {
            "刚刚"
        }
    }
}

/**
 * 获取时间字符串：不满一分钟显示"%d秒"、不满一小时显示"%d分钟"、不满一天显示"%d小时"、满一天显示"%d天"
 * @return 返回时间String
 */
fun Long?.getRedTime(): String {
    if (this == null) {
        return "0秒"
    }
    val time = this.toLong() / 1000
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