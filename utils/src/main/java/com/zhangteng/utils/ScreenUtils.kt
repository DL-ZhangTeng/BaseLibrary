package com.zhangteng.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect

/**
 * 获取当前屏幕截图，包含状态栏
 *
 * @return
 */
fun Activity?.snapShotWithStatusBar(): Bitmap? {
    this ?: return null
    val view = window.decorView
    view.isDrawingCacheEnabled = true
    view.buildDrawingCache()
    val bmp = view.drawingCache
    val width = getScreenWidth()
    val height = getScreenHeight()
    var bp: Bitmap? = null
    bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
    view.destroyDrawingCache()
    return bp
}

/**
 * 获取当前屏幕截图，不包含状态栏
 *
 * @return
 */
fun Activity?.snapShotWithoutStatusBar(): Bitmap? {
    this ?: return null
    val view = window.decorView
    view.isDrawingCacheEnabled = true
    view.buildDrawingCache()
    val bmp = view.drawingCache
    val frame = Rect()
    window.decorView.getWindowVisibleDisplayFrame(frame)
    val statusBarHeight = frame.top
    val width = getScreenWidth()
    val height = getScreenHeight()
    var bp: Bitmap? = null
    bp = Bitmap.createBitmap(
        bmp, 0, statusBarHeight, width, height
                - statusBarHeight
    )
    view.destroyDrawingCache()
    return bp
}

/**
 * 获得状态栏的高度
 *
 * @param context
 * @return
 */
fun Context?.getStatusHeight(): Int {
    var statusHeight = -1
    this ?: return statusHeight
    try {
        val clazz = Class.forName("com.android.internal.R\$dimen")
        val `object` = clazz.newInstance()
        val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
        statusHeight = resources.getDimensionPixelSize(height)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return statusHeight
}