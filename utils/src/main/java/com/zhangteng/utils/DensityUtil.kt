package com.zhangteng.utils

import android.content.Context
import android.util.TypedValue

/**
 * dp转px
 *
 * @param dpVal
 * @return
 */
fun Context?.dp2px(dpVal: Float): Int {
    return if (this != null) {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal,
            resources.displayMetrics
        ).toInt()
    } else -1
}

/**
 * sp转px
 *
 * @param spVal
 * @return
 */
fun Context?.sp2px(spVal: Float): Int {
    return if (this != null) {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal,
            resources.displayMetrics
        ).toInt()
    } else -1
}

/**
 * px转dp
 *
 * @param pxVal
 * @return
 */
fun Context?.px2dp(pxVal: Float): Float {
    if (this == null) return -1f
    val scale = resources.displayMetrics.density
    return pxVal / scale
}

/**
 * px转sp
 *
 * @param pxVal
 * @return
 */
fun Context?.px2sp(pxVal: Float): Float {
    if (this == null) return -1f
    return pxVal / resources.displayMetrics.scaledDensity
}

/**
 * 获取Density
 */
fun Context?.getDensity(): Float {
    if (this == null) return -1f
    return resources.displayMetrics.density
}

/**
 * 获取屏幕宽度
 */
fun Context?.getScreenWidth(): Int {
    if (this == null) return -1
    return resources.displayMetrics.widthPixels
}

/**
 * 获取屏幕高度
 */
fun Context?.getScreenHeight(): Int {
    if (this == null) return -1
    return resources.displayMetrics.heightPixels
}