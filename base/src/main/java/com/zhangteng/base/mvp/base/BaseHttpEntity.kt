package com.zhangteng.base.mvp.base

/**
 * model到presenter的回调
 * Created by swing on 2019/7/2 0002.
 */
abstract class BaseHttpEntity<T> {
    private var iView: IView? = null

    constructor() {}
    constructor(iView: IView?) {
        this.iView = iView
    }

    /**
     * 请求开始
     */
    fun onStart() {
        if (iView != null && iView is BaseLoadingView<*>) (iView as BaseLoadingView<*>?)?.showLoadingView()
    }

    abstract fun onSuccess(data: T?)

    /**
     * 无网络
     */
    fun onNoNetworkError() {
        if (iView != null && iView is BaseNoNetworkView<*>) (iView as BaseNoNetworkView<*>?)?.showNoNetwork()
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
        if (iView != null && iView is BaseNoNetworkView<*>) (iView as BaseNoNetworkView<*>?)?.hideNoNetwork()
        if (iView != null && iView is BaseRefreshView<*>) (iView as BaseRefreshView<*>?)?.finishRefreshOrLoadMore()
        if (iView != null && iView is BaseLoadingView<*>) (iView as BaseLoadingView<*>?)?.dismissLoadingView()
    }
}