package com.zhangteng.base.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object SoftInputUtils {
    /**
     * 打开软键盘
     *
     * @param activity 当前活动
     */
    fun openKeybord(activity: Activity?) {
        activity ?: return
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE)
        imm ?: return
        imm as InputMethodManager
        imm.showSoftInput(activity.getWindow().decorView, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 打开软键盘
     *
     * @param mEditText 输入框
     */
    fun openKeybord(mEditText: EditText?) {
        mEditText ?: return
        val imm = mEditText.context
                .getSystemService(Context.INPUT_METHOD_SERVICE)
        imm ?: return
        imm as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 关闭软键盘
     *
     * @param activity 当前活动
     */
    fun closeKeybord(activity: Activity?) {
        activity ?: return
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE)
        imm ?: return
        imm as InputMethodManager
        if (activity.currentFocus != null) imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     */
    fun closeKeybord(mEditText: EditText?) {
        mEditText ?: return
        val imm = mEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE)
        imm ?: return
        imm as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 隐藏或显示软键盘
     * 如果现在是显示调用后则隐藏 反之则显示
     *
     * @param activity 当前活动
     */
    fun openOrCloseSoftKeyboard(activity: Activity?) {
        activity ?: return
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE)
        imm ?: return
        imm as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 判断软键盘是否显示方法
     *
     * @param activity 当前活动
     * @return 是否显示键盘
     */
    fun isSoftShowing(activity: Activity?): Boolean? {
        activity ?: return null
        //获取当屏幕内容的高度
        val screenHeight = activity.window.decorView.height
        //获取View可见区域的bottom
        val rect = Rect()
        //DecorView即为activity的顶级view
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom + getSoftButtonsBarHeight(activity)
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return 高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun getSoftButtonsBarHeight(activity: Activity?): Int {
        activity ?: return 0
        val metrics = DisplayMetrics()
        //这个方法获取可能不是真实屏幕的高度
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        //获取当前屏幕的真实高度
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight) {
            realHeight - usableHeight
        } else {
            0
        }
    }
}