package com.zhangteng.base.utils

import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.widget.TextView

/**
 * Created by swing on 2019/7/31 0031.
 */
object TextUtil {
    /**
     * 计算textview中文本高度
     */
    fun getTextHeight(textView: TextView?): Int? {
        textView ?: return null
        val bounds = Rect()
        val mTextPaint: Paint? = textView.paint
        val mText = textView.text.toString()
        mTextPaint?.getTextBounds(mText, 0, mText.length, bounds)
        return bounds.height()
    }

    /**
     * 计算textview中文本宽度
     */
    fun getTextWidthF(textView: TextView?): Float? {
        textView ?: return null
        return textView.paint.measureText(textView.text.toString())
    }

    /**
     * 计算textview中文本宽度
     */
    fun getTextWidth(textView: TextView?): Int? {
        textView ?: return null
        return Layout.getDesiredWidth(textView.text, textView.paint).toInt()
    }
}