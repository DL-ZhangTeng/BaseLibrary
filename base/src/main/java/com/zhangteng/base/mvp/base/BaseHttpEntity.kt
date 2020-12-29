package com.zhangteng.base.mvp.base

/**
 * model到presenter的回调
 * Created by swing on 2019/7/2 0002.
 */
abstract class BaseHttpEntity<T> {
    private var baseView: BaseView? = null

    constructor() {}
    constructor(baseView: BaseView?) {
        this.baseView = baseView
    }

    /**
     * 请求开始
     */
    fun onStart() {
        if (baseView != null && baseView is BaseLoadingView<*>) (baseView as BaseLoadingView<*>?)?.showLoadingView()
    }

    abstract fun onSuccess(data: T?)

    /**
     * 无网络
     */
    fun onNoNetworkError() {
        if (baseView != null && baseView is BaseNoNetworkView<*>) (baseView as BaseNoNetworkView<*>?)?.showNoNetwork()
    }

    /**
     * 业务异常处理
     */
    fun onError(code: Int, error: String?) {}

    /**
     * http异常处理
     */
    fun onHttpError(code: Int, error: String?) {}

    /**
     * 请求完成
     */
    fun onFinish() {
        if (baseView != null && baseView is BaseNoNetworkView<*>) (baseView as BaseNoNetworkView<*>?)?.hideNoNetwork()
        if (baseView != null && baseView is BaseRefreshView<*>) (baseView as BaseRefreshView<*>?)?.finishRefreshOrLoadMore()
        if (baseView != null && baseView is BaseLoadingView<*>) (baseView as BaseLoadingView<*>?)?.dismissLoadingView()
    }
}