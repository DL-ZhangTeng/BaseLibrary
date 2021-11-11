package com.zhangteng.base.utils

import android.app.Activity
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