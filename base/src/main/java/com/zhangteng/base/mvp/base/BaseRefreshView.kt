package com.zhangteng.base.mvp.base

/**
 * 无网络/无数据
 * Created by swing on 2019/7/25 0025.
 */
interface BaseRefreshView<T> : BaseNoNetworkView<T?> {
    fun finishRefreshOrLoadMore()
}