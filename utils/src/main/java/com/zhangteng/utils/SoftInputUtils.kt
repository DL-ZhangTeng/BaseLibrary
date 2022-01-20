package com.zhangteng.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 打开软键盘
 */
fun Activity?.openKeyboard() {
    this ?: return
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE)
    imm ?: return
    imm as InputMethodManager
    imm.showSoftInput(window.decorView, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 打开软键盘
 *
 * @param mEditText 输入框
 */
fun EditText?.openKeyboard() {
    this ?: return
    val imm = context
        .getSystemService(Context.INPUT_METHOD_SERVICE)
    imm ?: return
    imm as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 关闭软键盘
 */
fun Activity?.closeKeyboard() {
    this ?: return
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE)
    imm ?: return
    imm as InputMethodManager
    if (currentFocus != null) imm.hideSoftInputFromWindow(
        currentFocus!!.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

/**
 * 关闭软键盘
 *
 * @param mEditText 输入框
 */
fun EditText?.closeKeyboard() {
    this ?: return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
    imm ?: return
    imm as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * 隐藏或显示软键盘
 * 如果现在是显示调用后则隐藏 反之则显示
 */
fun Activity?.openOrCloseSoftKeyboard() {
    this ?: return
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE)
    imm ?: return
    imm as InputMethodManager
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * 判断软键盘是否显示方法
 *
 * @return 是否显示键盘
 */
fun Activity?.isSoftShowing(): Boolean? {
    this ?: return null
    //获取当屏幕内容的高度
    val screenHeight = window.decorView.height
    //获取View可见区域的bottom
    val rect = Rect()
    //DecorView即为activity的顶级view
    window.decorView.getWindowVisibleDisplayFrame(rect)
    //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
    //选取screenHeight*2/3进行判断
    return screenHeight * 2 / 3 > rect.bottom + getSoftButtonsBarHeight()
}

/**
 * 底部虚拟按键栏的高度
 *
 * @return 高度
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun Activity?.getSoftButtonsBarHeight(): Int {
    this ?: return 0
    val metrics = DisplayMetrics()
    //这个方法获取可能不是真实屏幕的高度
    windowManager.defaultDisplay.getMetrics(metrics)
    val usableHeight = metrics.heightPixels
    //获取当前屏幕的真实高度
    windowManager.defaultDisplay.getRealMetrics(metrics)
    val realHeight = metrics.heightPixels
    return if (realHeight > usableHeight) {
        realHeight - usableHeight
    } else {
        0
    }
}