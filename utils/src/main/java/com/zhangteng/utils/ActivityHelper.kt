package com.zhangteng.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * description 跳转到下个页面
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
inline fun <reified T : Activity> Activity?.jumpToActivity(code: Int = 1) {
    if (this == null) return
    val intent = Intent()
    intent.setClass(this, T::class.java)
    startActivity(intent)
    setAnim(code)
}

/**
 * description 使用单个参数跳转到下个页面
 * @param key 键
 * @param value 值 自动判断类型
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
inline fun <reified T : Activity> Activity?.jumpToActivityWithParam(
    key: String?,
    value: Any?,
    code: Int = 1
) {
    if (this == null) return
    val intent = Intent()
    intent.setClass(this, T::class.java)
    when (value) {
        is Byte -> {
            intent.putExtra(key, value)
        }
        is Byte? -> {
            intent.putExtra(key, value)
        }
        is Short -> {
            intent.putExtra(key, value)
        }
        is Short? -> {
            intent.putExtra(key, value)
        }
        is Int -> {
            intent.putExtra(key, value)
        }
        is Int? -> {
            intent.putExtra(key, value)
        }
        is Long -> {
            intent.putExtra(key, value)
        }
        is Long? -> {
            intent.putExtra(key, value)
        }
        is Float -> {
            intent.putExtra(key, value)
        }
        is Float? -> {
            intent.putExtra(key, value)
        }
        is Double -> {
            intent.putExtra(key, value)
        }
        is Double? -> {
            intent.putExtra(key, value)
        }
        is Boolean -> {
            intent.putExtra(key, value)
        }
        is Boolean? -> {
            intent.putExtra(key, value)
        }
        is Char -> {
            intent.putExtra(key, value)
        }
        is Char? -> {
            intent.putExtra(key, value)
        }
        is CharSequence -> {
            intent.putExtra(key, value)
        }
        is CharSequence? -> {
            intent.putExtra(key, value)
        }
        is String -> {
            intent.putExtra(key, value)
        }
        is String? -> {
            intent.putExtra(key, value)
        }
        is Serializable -> {
            intent.putExtra(key, value)
        }
        is Serializable? -> {
            intent.putExtra(key, value)
        }
        is Parcelable -> {
            intent.putExtra(key, value)
        }
        is Parcelable? -> {
            intent.putExtra(key, value)
        }
        is ByteArray -> {
            intent.putExtra(key, value)
        }
        is ByteArray? -> {
            intent.putExtra(key, value)
        }
        is ShortArray -> {
            intent.putExtra(key, value)
        }
        is ShortArray? -> {
            intent.putExtra(key, value)
        }
        is IntArray -> {
            intent.putExtra(key, value)
        }
        is IntArray? -> {
            intent.putExtra(key, value)
        }
        is LongArray -> {
            intent.putExtra(key, value)
        }
        is LongArray? -> {
            intent.putExtra(key, value)
        }
        is FloatArray -> {
            intent.putExtra(key, value)
        }
        is FloatArray? -> {
            intent.putExtra(key, value)
        }
        is DoubleArray -> {
            intent.putExtra(key, value)
        }
        is DoubleArray? -> {
            intent.putExtra(key, value)
        }
        is BooleanArray -> {
            intent.putExtra(key, value)
        }
        is BooleanArray? -> {
            intent.putExtra(key, value)
        }
        is CharArray -> {
            intent.putExtra(key, value)
        }
        is CharArray? -> {
            intent.putExtra(key, value)
        }
        is Array<*> -> {
            intent.putExtra(key, value)
        }
        is Array<*>? -> {
            intent.putExtra(key, value)
        }
    }
    startActivity(intent)
    setAnim(code)
}

/**
 * description 使用Bundle跳转到下个页面
 * @param bundle Bundle参数集
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
inline fun <reified T : Activity> Activity?.jumpToActivityWithBundle(
    bundle: Bundle,
    code: Int = 1
) {
    if (this == null) return
    val intent = Intent()
    intent.setClass(this, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
    setAnim(code)
}

/**
 * description 跳转到下个页面并获取返回结果
 * @param requestCode 请求Code
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
inline fun <reified T : Activity> Activity?.jumpToActivityForResult(
    requestCode: Int,
    code: Int = 1
) {
    if (this == null) return
    val intent = Intent()
    intent.setClass(this, T::class.java)
    startActivityForResult(intent, requestCode)
    setAnim(code)
}

/**
 * description 使用单个参数跳转到下个页面并获取返回结果
 * @param key 键
 * @param value 值 自动判断类型
 * @param requestCode 请求Code
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
inline fun <reified T : Activity> Activity?.jumpToActivityForResult(
    key: String?,
    value: Any?,
    requestCode: Int,
    code: Int = 1
) {
    if (this == null) return
    val intent = Intent()
    intent.setClass(this, T::class.java)
    when (value) {
        is Byte -> {
            intent.putExtra(key, value)
        }
        is Byte? -> {
            intent.putExtra(key, value)
        }
        is Short -> {
            intent.putExtra(key, value)
        }
        is Short? -> {
            intent.putExtra(key, value)
        }
        is Int -> {
            intent.putExtra(key, value)
        }
        is Int? -> {
            intent.putExtra(key, value)
        }
        is Long -> {
            intent.putExtra(key, value)
        }
        is Long? -> {
            intent.putExtra(key, value)
        }
        is Float -> {
            intent.putExtra(key, value)
        }
        is Float? -> {
            intent.putExtra(key, value)
        }
        is Double -> {
            intent.putExtra(key, value)
        }
        is Double? -> {
            intent.putExtra(key, value)
        }
        is Boolean -> {
            intent.putExtra(key, value)
        }
        is Boolean? -> {
            intent.putExtra(key, value)
        }
        is Char -> {
            intent.putExtra(key, value)
        }
        is Char? -> {
            intent.putExtra(key, value)
        }
        is CharSequence -> {
            intent.putExtra(key, value)
        }
        is CharSequence? -> {
            intent.putExtra(key, value)
        }
        is String -> {
            intent.putExtra(key, value)
        }
        is String? -> {
            intent.putExtra(key, value)
        }
        is Serializable -> {
            intent.putExtra(key, value)
        }
        is Serializable? -> {
            intent.putExtra(key, value)
        }
        is Parcelable -> {
            intent.putExtra(key, value)
        }
        is Parcelable? -> {
            intent.putExtra(key, value)
        }
        is ByteArray -> {
            intent.putExtra(key, value)
        }
        is ByteArray? -> {
            intent.putExtra(key, value)
        }
        is ShortArray -> {
            intent.putExtra(key, value)
        }
        is ShortArray? -> {
            intent.putExtra(key, value)
        }
        is IntArray -> {
            intent.putExtra(key, value)
        }
        is IntArray? -> {
            intent.putExtra(key, value)
        }
        is LongArray -> {
            intent.putExtra(key, value)
        }
        is LongArray? -> {
            intent.putExtra(key, value)
        }
        is FloatArray -> {
            intent.putExtra(key, value)
        }
        is FloatArray? -> {
            intent.putExtra(key, value)
        }
        is DoubleArray -> {
            intent.putExtra(key, value)
        }
        is DoubleArray? -> {
            intent.putExtra(key, value)
        }
        is BooleanArray -> {
            intent.putExtra(key, value)
        }
        is BooleanArray? -> {
            intent.putExtra(key, value)
        }
        is CharArray -> {
            intent.putExtra(key, value)
        }
        is CharArray? -> {
            intent.putExtra(key, value)
        }
        is Array<*> -> {
            intent.putExtra(key, value)
        }
        is Array<*>? -> {
            intent.putExtra(key, value)
        }
    }
    startActivityForResult(intent, requestCode)
    setAnim(code)
}

/**
 * description 使用Bundle跳转到下个页面并获取返回结果
 * @param bundle Bundle参数集
 * @param requestCode 请求Code
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
inline fun <reified T : Activity> Activity?.jumpToActivityForResult(
    bundle: Bundle,
    requestCode: Int,
    code: Int = 1
) {
    if (this == null) return
    val intent = Intent()
    intent.setClass(this, T::class.java)
    intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
    setAnim(code)
}

/**
 * description 跳转到下个页面并关闭当前页面
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
inline fun <reified T : Activity> Activity?.jumpActivity(code: Int = 1) {
    if (this == null) return
    val intent = Intent()
    intent.setClass(this, T::class.java)
    startActivity(intent)
    setAnim(code)
    finish()
}

/**
 * description 使用单个参数跳转到下个页面并关闭当前页面
 * @param key 键
 * @param value 值，自动判断类型
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
inline fun <reified T : Activity> Activity?.jumpActivityWithParam(
    key: String?,
    value: Any?,
    code: Int = 1
) {
    if (this == null) return
    val intent = Intent()
    intent.setClass(this, T::class.java)
    when (value) {
        is Byte -> {
            intent.putExtra(key, value)
        }
        is Byte? -> {
            intent.putExtra(key, value)
        }
        is Short -> {
            intent.putExtra(key, value)
        }
        is Short? -> {
            intent.putExtra(key, value)
        }
        is Int -> {
            intent.putExtra(key, value)
        }
        is Int? -> {
            intent.putExtra(key, value)
        }
        is Long -> {
            intent.putExtra(key, value)
        }
        is Long? -> {
            intent.putExtra(key, value)
        }
        is Float -> {
            intent.putExtra(key, value)
        }
        is Float? -> {
            intent.putExtra(key, value)
        }
        is Double -> {
            intent.putExtra(key, value)
        }
        is Double? -> {
            intent.putExtra(key, value)
        }
        is Boolean -> {
            intent.putExtra(key, value)
        }
        is Boolean? -> {
            intent.putExtra(key, value)
        }
        is Char -> {
            intent.putExtra(key, value)
        }
        is Char? -> {
            intent.putExtra(key, value)
        }
        is CharSequence -> {
            intent.putExtra(key, value)
        }
        is CharSequence? -> {
            intent.putExtra(key, value)
        }
        is String -> {
            intent.putExtra(key, value)
        }
        is String? -> {
            intent.putExtra(key, value)
        }
        is Serializable -> {
            intent.putExtra(key, value)
        }
        is Serializable? -> {
            intent.putExtra(key, value)
        }
        is Parcelable -> {
            intent.putExtra(key, value)
        }
        is Parcelable? -> {
            intent.putExtra(key, value)
        }
        is ByteArray -> {
            intent.putExtra(key, value)
        }
        is ByteArray? -> {
            intent.putExtra(key, value)
        }
        is ShortArray -> {
            intent.putExtra(key, value)
        }
        is ShortArray? -> {
            intent.putExtra(key, value)
        }
        is IntArray -> {
            intent.putExtra(key, value)
        }
        is IntArray? -> {
            intent.putExtra(key, value)
        }
        is LongArray -> {
            intent.putExtra(key, value)
        }
        is LongArray? -> {
            intent.putExtra(key, value)
        }
        is FloatArray -> {
            intent.putExtra(key, value)
        }
        is FloatArray? -> {
            intent.putExtra(key, value)
        }
        is DoubleArray -> {
            intent.putExtra(key, value)
        }
        is DoubleArray? -> {
            intent.putExtra(key, value)
        }
        is BooleanArray -> {
            intent.putExtra(key, value)
        }
        is BooleanArray? -> {
            intent.putExtra(key, value)
        }
        is CharArray -> {
            intent.putExtra(key, value)
        }
        is CharArray? -> {
            intent.putExtra(key, value)
        }
        is Array<*> -> {
            intent.putExtra(key, value)
        }
        is Array<*>? -> {
            intent.putExtra(key, value)
        }
    }
    startActivity(intent)
    setAnim(code)
    finish()
}

/**
 * description 使用Bundle跳转到下个页面并关闭当前页面
 * @param bundle Bundle参数集
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
inline fun <reified T : Activity> Activity?.jumpActivityWithBundle(bundle: Bundle, code: Int = 1) {
    if (this == null) return
    val intent = Intent()
    intent.setClass(this, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
    setAnim(code)
    finish()
}

/**
 * description 设置activity进入退出动画
 * @param code 1：页面进入，x轴100%->0，页面退出，x轴0->-100%；2页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
fun Activity?.setAnim(code: Int = 1) {
    if (code == 1) {
        setShowAnim()
    } else if (code == 2) {
        setCloseAnim()
    }
}

/**
 * description 跳转进activity时的动画：页面进入，x轴100%->0，页面退出，x轴0->-100%
 */
fun Activity?.setShowAnim() {
    if (this == null) return
    overridePendingTransition(
        getEnterAnim(),
        getExitAnim()
    )
}

/**
 * description 退出activity时的动画：页面进入，x轴-100%->0，页面退出，x轴0->100%
 */
fun Activity?.setCloseAnim() {
    if (this == null) return
    overridePendingTransition(
        getPopEnterAnim(),
        getPopExitAnim()
    )
}

/**
 * 入栈动画：页面进入，x轴100%->0
 */
fun getEnterAnim(): Int {
    return R.anim.self_enter_anim
}

/**
 * 入栈动画：页面退出，x轴0->-100%
 */
fun getExitAnim(): Int {
    return R.anim.self_exit_anim
}

/**
 * 出栈动画：页面进入，x轴-100%->0
 */
fun getPopEnterAnim(): Int {
    return R.anim.self_pop_enter_anim
}

/**
 * 出栈动画：页面退出，x轴0->100%
 */
fun getPopExitAnim(): Int {
    return R.anim.self_pop_exit_anim
}