package com.zhangteng.base.recyclerview.layout.viewpager

/**
 * description: ViewPager监听
 * author: Swing
 * date: 2022/11/26
 */
interface OnViewPagerListener {
    /**
     * description 初始化完成
     */
    fun onInitComplete()

    /**
     * description: 释放的监听
     */
    fun onPageRelease(isNext: Boolean, position: Int)

    /**
     * description 选中的监听以及判断是否滑动到底部
     */
    fun onPageSelected(position: Int, isBottom: Boolean)
}