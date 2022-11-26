package com.zhangteng.base.recyclerview.layout.picker

import android.view.View

/**
 * description: 停止时，显示在中间的View的监听
 * author: Swing
 * date: 2022/11/26
 */
interface OnSelectedViewListener {
    fun onSelectedView(view: View?, position: Int)
}