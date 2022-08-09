package com.zhangteng.mvp.base

/**
 * 无网络/无数据
 * Created by swing on 2019/7/25 0025.
 */
interface BaseStateView<T> : BaseLoadingView<T?> {
    fun showNoNetView()
    fun hideNoNetView()
    fun showTimeOutView()
    fun hideTimeOutView()
    fun showEmptyView()
    fun hideEmptyView()
    fun showErrorView()
    fun hideErrorView()
    fun showNoLoginView()
    fun hideNoLoginView()
}