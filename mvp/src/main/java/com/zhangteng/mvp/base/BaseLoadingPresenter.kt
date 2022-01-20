package com.zhangteng.mvp.base

open class BaseLoadingPresenter<V : BaseLoadingView<*>, M : IModel> : BasePresenter<V, M>() {
    open fun getBaseLoadingView(): V? {
        return if (isAttach()) {
            mView?.get()
        } else {
            null
        }
    }
}