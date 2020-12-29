package com.zhangteng.base.mvp.base

import java.lang.ref.WeakReference

open class BasePresenter<V : BaseView?> {
    protected var view: WeakReference<V?>? = null
    fun attachView(view: V?) {
        if (!isAttach()) {
            this.view = WeakReference(view)
        }
    }

    fun detachView() {
        if (isAttach()) {
            view?.clear()
            view = null
        }
    }

    fun isAttach(): Boolean {
        return view != null &&
                view!!.get() != null
    }
}