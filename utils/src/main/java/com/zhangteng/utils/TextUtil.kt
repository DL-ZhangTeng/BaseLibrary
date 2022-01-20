package com.zhangteng.utils

import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.widget.TextView

/**
 * 计算textview中文本高度
 */
fun TextView?.getTextHeight(): Int? {
    this ?: return null
    val bounds = Rect()
    val mTextPaint: Paint? = paint
    val mText = text.toString()
    mTextPaint?.getTextBounds(mText, 0, mText.length, bounds)
    return bounds.height()
}

/**
 * 计算textview中文本宽度
 */
fun TextView?.getTextWidthF(): Float? {
    this ?: return null
    return paint.measureText(text.toString())
}

/**
 * 计算textview中文本宽度
 */
fun TextView?.getTextWidth(): Int? {
    this ?: return null
    return Layout.getDesiredWidth(text, paint).toInt()
}