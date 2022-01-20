package com.zhangteng.utils

import android.content.Context
import android.widget.Toast

var isShowToast = true

/**
 * 短时间显示Toast
 *
 * @param message
 */
fun Context?.showShortToast(message: CharSequence?) {
    if (isShowToast) Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * 短时间显示Toast
 *
 * @param message
 */
fun Context?.showShortToast(message: Int) {
    if (isShowToast) Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * 长时间显示Toast
 *
 * @param message
 */
fun Context?.showLongToast(message: CharSequence?) {
    if (isShowToast) Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * 长时间显示Toast
 *
 * @param message
 */
fun Context?.showLongToast(message: Int) {
    if (isShowToast) Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * 自定义显示Toast时间
 *
 * @param message
 * @param duration
 */
fun Context?.showToast(message: CharSequence?, duration: Int) {
    if (isShowToast) Toast.makeText(this, message, duration).show()
}

/**
 * 自定义显示Toast时间
 *
 * @param message
 * @param duration
 */
fun Context?.showToast(message: Int, duration: Int) {
    if (isShowToast) Toast.makeText(this, message, duration).show()
}