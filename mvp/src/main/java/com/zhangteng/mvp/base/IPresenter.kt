package com.zhangteng.mvp.base

interface IPresenter<V : IView, M : IModel> {
    fun attachView(view: V?) {}

    fun onStart() {}

    fun detachView()

    fun onDestroy() {}

    fun isAttach(): Boolean {
        return true
    }
}