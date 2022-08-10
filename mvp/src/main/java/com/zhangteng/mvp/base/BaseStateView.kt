package com.zhangteng.mvp.base

/**
 * 无网络/无数据
 * Created by swing on 2019/7/25 0025.
 */
interface BaseStateView<T> : BaseLoadingView<T?> {
    fun showNoNetView()
    fun showTimeOutView()
    fun showEmptyView()
    fun showErrorView()
    fun showNoLoginView()
    fun showContentView()
}