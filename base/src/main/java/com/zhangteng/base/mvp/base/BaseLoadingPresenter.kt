package com.zhangteng.base.mvp.base

open class BaseLoadingPresenter<V : BaseLoadingView<*>, M : IModel> : BasePresenter<V, M>() {
    open fun getBaseLoadingView(): V? {
        return if (isAttach()) {
            mView?.get()
        } else {
            null
        }
    }
}