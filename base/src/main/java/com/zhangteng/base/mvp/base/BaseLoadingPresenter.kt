package com.zhangteng.base.mvp.base

open class BaseLoadingPresenter<V : BaseLoadingView<*>?> : BasePresenter<V?>() {
    fun getBaseLoadingView(): V? {
        return if (isAttach()) {
            mView?.get()
        } else {
            null
        }
    }
}