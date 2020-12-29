package com.zhangteng.base.mvp.base

class BaseLoadingPresenter<V : BaseLoadingView<*>?> : BasePresenter<V?>() {
    fun getBaseLoadingView(): V? {
        return if (isAttach()) {
            view?.get()
        } else {
            null
        }
    }
}