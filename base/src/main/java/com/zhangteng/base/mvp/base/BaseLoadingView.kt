package com.zhangteng.base.mvp.base

/**
 * Created by swing on 2019/7/16 0016.
 */
interface BaseLoadingView<T> : IView {
    open fun showLoadingView()
    open fun dismissLoadingView()
    open fun inflateView(data: T?)
}