package com.zhangteng.utils

import java.util.regex.Pattern

/**
 * 验证Email
 *
 * email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
 * @return 验证成功返回true，验证失败返回false ^ ：匹配输入的开始位置。 \：将下一个字符标记为特殊字符或字面值。
 * ：匹配前一个字符零次或几次。 + ：匹配前一个字符一次或多次。 (pattern) 与模式匹配并记住匹配。 x|y：匹配 x 或
 * y。 [a-z] ：表示某个范围内的字符。与指定区间内的任何字符匹配。 \w ：与任何单词字符匹配，包括下划线。
 *
 *
 * {n,m} 最少匹配 n 次且最多匹配 m 次 $ ：匹配输入的结尾。
 */
fun String?.checkEmail(): Boolean {
    val regex = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$"
    return Pattern.matches(regex, this)
}

/**
 * 验证身份证号码
 *
 * 居民身份证号码15位或18位，最后一位可能是数字或字母
 * @return 验证成功返回true，验证失败返回false
 */
fun String?.checkIdCard(): Boolean {
    val regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}"
    return Pattern.matches(regex, this)
}

/**
 * 验证整数（正整数和负整数）
 *
 * 一位或多位0-9之间的整数
 * @return 验证成功返回true，验证失败返回false
 */
fun String?.checkDigit(): Boolean {
    val regex = "\\-?[1-9]\\d+"
    return Pattern.matches(regex, this)
}

/**
 * 验证整数和浮点数（正负整数和正负浮点数）
 *
 * 一位或多位0-9之间的浮点数，如：1.23，233.30
 * @return 验证成功返回true，验证失败返回false
 */
fun String?.checkDecimals(): Boolean {
    val regex = "\\-?[1-9]\\d+(\\.\\d+)?"
    return Pattern.matches(regex, this)
}

/**
 * 验证空白字符
 *
 * 空白字符，包括：空格、\t、\n、\r、\f、\x0B
 * @return 验证成功返回true，验证失败返回false
 */
fun String?.checkBlankSpace(): Boolean {
    val regex = "\\s+"
    return Pattern.matches(regex, this)
}

/**
 * 验证中文
 *
 * @return 验证成功返回true，验证失败返回false
 */
fun String?.checkChinese(): Boolean {
    val regex = "^[\u4E00-\u9FA5]+$"
    return Pattern.matches(regex, this)
}

/**
 * 验证日期（年月日）
 *
 * 格式：1992-09-03，或1992.09.03
 * @return 验证成功返回true，验证失败返回false
 */
fun String?.checkBirthday(): Boolean {
    val regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}"
    return Pattern.matches(regex, this)
}

/**
 * 验证URL地址
 *
 * 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或
 * http://www.csdn.net:80
 * @return 验证成功返回true，验证失败返回false
 */
fun String?.checkURL(): Boolean {
    val regex =
        "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?"
    return Pattern.matches(regex, this)
}

/**
 * 匹配中国邮政编码
 *
 * @return 验证成功返回true，验证失败返回false
 */
fun String?.checkPostcode(): Boolean {
    val regex = "[1-9]\\d{5}"
    return Pattern.matches(regex, this)
}

/**
 * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
 *
 * @return 验证成功返回true，验证失败返回false
 */
fun String?.checkIpAddress(): Boolean {
    val regex =
        "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))"
    return Pattern.matches(regex, this)
}

fun String?.checkNickname(): Boolean {
    val regex = "^[a-zA-Z0-9\u4E00-\u9FA5_]+$"
    return Pattern.matches(regex, this)
}

fun String?.hasCrossSciptRiskInAddress(): Boolean {
    var str = this
    val regx = "[`~!@#$%^&*+=|{}':;',\\[\\].<>~！@#￥%……&*——+|{}【】‘；：”“’。，、？-]"
    if (str != null) {
        str = str.trim { it <= ' ' }
        val p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
        val m = p.matcher(str)
        return m.find()
    }
    return false
}