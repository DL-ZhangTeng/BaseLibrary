package com.zhangteng.base.recyclerview.layout.slide

import androidx.recyclerview.widget.RecyclerView

/**
 * description: 侧滑回调
 * author: Swing
 * date: 2022/11/26
 */
interface OnSlideListener<T> {
    fun onSliding(viewHolder: RecyclerView.ViewHolder?, ratio: Float, direction: Int)
    fun onSlided(viewHolder: RecyclerView.ViewHolder?, t: T, direction: Int)
    fun onClear()
}