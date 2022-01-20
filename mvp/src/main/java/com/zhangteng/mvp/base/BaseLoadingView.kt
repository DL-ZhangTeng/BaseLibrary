package com.zhangteng.mvp.base

/**
 * Created by swing on 2019/7/16 0016.
 */
interface BaseLoadingView<T> : IView {
    fun showLoadingView()
    fun dismissLoadingView()
    fun inflateView(data: T?)
}