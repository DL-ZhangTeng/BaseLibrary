package com.zhangteng.utils

import android.text.TextUtils

/**
 * 为空判断
 * Created by Swing on 2019/7/10 0010.
 */
object NullUtils {
    fun isEmpty(list: List<*>?): Boolean {
        return null == list || list.isEmpty()
    }

    fun isEmpty(list: Map<*, *>?): Boolean {
        return null == list || list.isEmpty()
    }

    fun isEmpty(string: String?): Boolean {
        return null == string || "" == string
    }

    fun isStrongEmpty(string: String?): Boolean {
        return null == string || "" == string || "null" == string || "NULL" == string
    }

    fun <T> getNotNull(list: List<T>?): List<T> {
        return if (isEmpty(list)) {
            ArrayList()
        } else list!!
    }

    fun getNotNull(string: String?): String {
        return if (isEmpty(string)) {
            ""
        } else string!!
    }

    fun isTrue(i: Int): Boolean {
        return i != 0
    }

    fun isTrue(string: String): Boolean {
        return "1" == string
    }

    fun getBooleanInt(flag: Boolean): Int {
        return if (flag) 1 else 0
    }

    fun getBooleanString(flag: Boolean): String {
        return if (flag) "1" else "0"
    }

    /**
     * 判断字符串是否符合手机号码格式
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     * 电信号段: 133,149,153,170,173,177,180,181,189
     *
     * @param mobileNum 待检测的字符串
     */
    fun isMobileNo(mobileNum: String): Boolean {
        val telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$"
        return if (TextUtils.isEmpty(mobileNum)) {
            false
        } else {
            mobileNum.matches(Regex(telRegex))
        }
    }
}